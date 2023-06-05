
package com.example.myapplication;

import android.annotation.SuppressLint;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.net.ConnectException;
import java.util.ArrayList;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private DataBaseHelper dbHelper;
    private MqttAndroidClient mqttAndroidClient;


    private ListView listView;
    private ArrayAdapter<String> adapter;

    private ImageView refreshButton;
    private AnimationDrawable refreshAnimation;

    private static final String MQTT_BROKER = "tcp://broker.mqtt-dashboard.com:1883"; // MQTT 브로커 주소
    private static final String MQTT_CLIENT_ID = "android-client";
    private static final String MQTT_TOPIC = "myeongseung"; // 구독할 토픽
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView wifiNameTextView = findViewById(R.id.wifiNameTextView);

        String wifiName = getWifiName();
        wifiNameTextView.setText(wifiName);

        String clientId = MQTT_CLIENT_ID + System.currentTimeMillis();
        mqttAndroidClient = new MqttAndroidClient(this, MQTT_BROKER, clientId, new MemoryPersistence());

        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                // Called when the MQTT connection is established
                // Subscribe to the topic
                subscribeToTopic();
            }

            @Override
            public void connectionLost(Throwable cause) {
                // Called when the MQTT connection is lost
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                // Called when a new MQTT message is received
                String payload = new String(message.getPayload());
                // Process the received message
                // TODO: Handle the received value
                parseJson(payload);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // Called when a message has been successfully delivered
            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        // Set the username and password if required
        // mqttConnectOptions.setUserName("your-username");
        // mqttConnectOptions.setPassword("your-password".toCharArray());
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // Called when the MQTT connection is successfully established
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Called when the MQTT connection fails
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);


        refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the refresh animation
                startRefreshAnimation();

                // Trigger the MQTT data reload
                reloadMqttData();
            }
        });

        dbHelper = new DataBaseHelper(this);

        // 데이터 삽입 예시
        int gas1 = 10;
        int gas2 = 200;
        int gas3 = 300;
        dbHelper.insertData(gas1, gas2, gas3);




    }


    private void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(MQTT_TOPIC, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // Called when a new MQTT message is received
                    String payload = new String(message.getPayload());
                    // Process the received JSON message
                    parseJson(payload);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void parseJson(String json) {
        // Parse the JSON string and process the received values
        // TODO: Implement JSON parsing and value handling logic
        try{
            JSONObject jsonObject = new JSONObject(json);

            String gasSensor = jsonObject.getString("Gas_sensor");
            int value = jsonObject.getInt("value");

            if (gasSensor.equals("LPG")){
                String item = "Gas Sensor: " + gasSensor + ", Value: " + value;  // JSON 데이터를 리스트뷰에 출력하기에서 JSON 데이터 넣는 곳
                adapter.add(item);
                adapter.notifyDataSetChanged();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getWifiName() {
        String wifiName = "";
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network network = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    network = connectivityManager.getActiveNetwork();
                }
                if (network != null) {
                    NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                    if (networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        wifiName = getWifiNameFromWifiManager();
                    }
                }
            } else {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    wifiName = getWifiNameFromWifiManager();
                }
            }
        }
        return wifiName;
    }

    private String getWifiNameFromWifiManager() {
        String wifiName = "";
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            wifiName = wifiInfo.getSSID();
            // Remove double quotes from the SSID
            wifiName = wifiName.replace("\"", "");
        }
        return wifiName;
    }

    private void startRefreshAnimation() {
        refreshButton.setImageResource(R.drawable.refresh_animation);
        refreshAnimation = (AnimationDrawable) refreshButton.getDrawable();
        refreshAnimation.start();

        // Stop the refresh animation after a certain duration
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopRefreshAnimation();
            }
        }, 2000); // Adjust the duration as needed (e.g., 2000 milliseconds = 2 seconds)
    }

    private void stopRefreshAnimation() {
        if (refreshAnimation != null) {
            refreshAnimation.stop();
            refreshButton.setImageResource(R.drawable.refresh);
            refreshAnimation = null;
        }
    }

    private void reloadMqttData() {
        // Clear the existing adapter data
        adapter.clear();
        adapter.notifyDataSetChanged();

        // Unsubscribe from the MQTT topic
        try {
            mqttAndroidClient.unsubscribe(MQTT_TOPIC);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Subscribe to the MQTT topic again
        subscribeToTopic();
    }


}

