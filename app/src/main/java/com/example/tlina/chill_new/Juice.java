package com.example.tlina.chill_new;

public class Juice {

    String juiceID;
    String juiceName;
    String juiceType;
    String juiceTime;


    public Juice(){


    }

    public Juice(String juiceID, String juiceName, String juiceType, String juiceTime) {

        this.juiceID = juiceID;
        this.juiceName = juiceName;
        this.juiceType = juiceType;
        this.juiceTime = juiceTime;
    }

    public String getJuiceID() {
        return juiceID;
    }

    public String getJuiceName() {
        return juiceName;
    }

    public String getJuiceType() {
        return juiceType;
    }

    public String getJuiceTime() {
        return juiceTime;
    }
}
