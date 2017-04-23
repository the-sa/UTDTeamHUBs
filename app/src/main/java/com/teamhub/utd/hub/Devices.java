package com.teamhub.utd.hub;


import java.io.Serializable;

public class Devices implements Serializable {
    int id;
    int batteryLife;
    int userID;
    String name;
    String macaddress;

    public void Device (int i, String n, String m, int b, int u) {
        id = i;
        name = n;
        macaddress = m;
        userID = u;
        batteryLife = b;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBatteryLife() {
        return batteryLife;
    }

    public void setBatteryLife(int batteryLife) {
        this.batteryLife = batteryLife;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacaddress() {
        return macaddress;
    }

    public void setMacaddress(String macaddress) {
        this.macaddress = macaddress;
    }

    public String toString() {
        return name + " " + batteryLife;
    }

}