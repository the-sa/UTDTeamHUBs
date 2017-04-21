package com.teamhub.utd.hub;


public class User {
    int id;
    String username, password, role;

    public void User (int i, String u, String p, String r){
        id = i;
        username = u;
        password = p;
        role = r;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(int role) {
        if (role == 0) {
            this.role = "normal";
        } else if (role == 1) {
            this.role = "admin";
        } else {
            this.role = "nothing";
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
        return username + "          role: " + role;
    }
}
