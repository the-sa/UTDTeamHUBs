package com.teamhub.utd.hub;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class User implements Serializable {
    int id, role;
    String username, password;
    String stringRole;

    public void User (int i, String u, String p, int r){
        id = i;
        username = u;
        password = p;
        role = r;
    }

    public void User () { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
        setRoleWord(role);
    }

    public void setRoleWord(int role) {
        if (role == 0) {
            this.stringRole = "normal";
        } else if (role == 1) {
            this.stringRole = "admin";
        } else {
            this.stringRole = "nothing";
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString () {
        return username + "           role: " + stringRole;
    }
}
