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

    public double getNatural() {
        return natural;
    }

    public void setNatural(double natural) {
        this.natural = natural;
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
    public double natural;
    public double CO;
    public double LPGLNG;
    public LocalDateTime TIMESTAMP;
}
