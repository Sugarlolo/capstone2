package com.example.myapplication;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class GAS {
    public int getGas_pk() {
        return gas_pk;
    }

    public void setGas_pk(int gas_pk) {
        this.gas_pk = gas_pk;
    }


    public double getCO() {
        return CO;
    }

    public void setCO(double CO) {
        this.CO = CO;
    }

    public double getLPGLNG() {
        return LPGLNG;
    }

    public void setLPGLNG(double LPGLNG) {
        this.LPGLNG = LPGLNG;
    }

    public LocalDateTime getTIMESTAMP() {
        return TIMESTAMP;
    }

    public void setTIMESTAMP(LocalDateTime TIMESTAMP) {
        this.TIMESTAMP = TIMESTAMP;
    }

    public int gas_pk;

    public double getMatan() {
        return matan;
    }

    public void setMatan(double matan) {
        this.matan = matan;
    }

    public double matan;
    public double CO;
    public double LPGLNG;
    public LocalDateTime TIMESTAMP;
}
