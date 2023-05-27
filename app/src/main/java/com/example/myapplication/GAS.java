package com.example.myapplication;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class GAS {


    public int gas_pk;
    public int getGas_pk() {
        return gas_pk;
    }
    public void setGas_pk(int gas_pk) {
        this.gas_pk = gas_pk;
    }


    public LocalDateTime TIMESTAMP;
    public LocalDateTime getTIMESTAMP() { return TIMESTAMP;}
    public void setTIMESTAMP(LocalDateTime TIMESTAMP) {this.TIMESTAMP = TIMESTAMP;}



    public int matan;
    public int getMatan() { return matan;  }
    public void setMatan(int matan) { this.matan = matan;  }


    public int CO;
    public int getCO() { return CO;  }
    public void setCO(int CO) { this.CO = CO;  }


    public void setLPGLNG(int LPGLNG) { this.LPGLNG = LPGLNG; }
    public int getLPGLNG() {return LPGLNG; }
    public int LPGLNG;


}
