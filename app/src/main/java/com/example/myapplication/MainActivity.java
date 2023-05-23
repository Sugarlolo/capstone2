package com.example.myapplication;

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
import android.widget.TextView;
import java.net.ConnectException;

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


public class MainActivity extends AppCompatActivity {

    private MqttAndroidClient mqttAndroidClient;

    private static final String MQTT_BROKER = "tcp://broker.mqtt-dashboard.com:1883";
    private static final String MQTT_CLIENT_ID = "android-client";
    private static final String MQTT_TOPIC = "myeongseung";
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

}
