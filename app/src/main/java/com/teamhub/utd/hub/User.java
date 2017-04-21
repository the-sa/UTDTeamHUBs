package com.teamhub.utd.hub;


public class User {
    int id, role;
    String username, password;

    public void User (int i, String u, String p, int r){
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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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
        return username + "         role: " + role;
    }
}
