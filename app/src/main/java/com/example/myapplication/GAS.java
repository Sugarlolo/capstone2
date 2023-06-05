package com.example.myapplication;

import java.time.LocalDateTime;

public class GAS {


    public int gas_pk;


    public int getGas_pk() {return gas_pk;}
    public void setGas_pk(int gas_pk) {
        this.gas_pk = gas_pk;
    }


    public LocalDateTime TIMESTAMP;
    public LocalDateTime getTIMESTAMP() { return TIMESTAMP;}
    public void setTIMESTAMP(LocalDateTime TIMESTAMP) {this.TIMESTAMP = TIMESTAMP;}



    public int METAINE;
    public int getMETAINE() { return METAINE;  }
    public void setMETAINE(int METAINE) { this.METAINE = METAINE;  }


    public int CO;
    public int getCO() { return CO;  }
    public void setCO(int CO) { this.CO = CO;  }

    public int LPGLNG;
    public void setLPGLNG(int LPGLNG) { this.LPGLNG = LPGLNG; }
    public int getLPGLNG() {return LPGLNG; }


}
