package com.teamhub.utd.hub;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "jdbc:sqlite:c://data/data/com.teamhub.utd.hub/databases/RMDB.db";

// //data/data/com.teamhub.utd.hub/databases/RMDB.db

    public LoginRequest(String username, String password, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);


    }

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://data/data/com.teamhub.utd.hub/databases/RMDB.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("FOUND DATABASE");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public boolean isVerified(String username, String password){
        String sql = "SELECT " + username + " AND " + password + " FROM User_Table";
        Log.e("isVerified Method", "AA");
        boolean exist = false;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            if(rs != null) {
                exist = true;
            }

            // loop through the result set
//            while (rs.next()) {
//                System.out.println(rs.getInt("id") +  "\t" +
//                        rs.getString("name") + "\t" +
//                        rs.getDouble("capacity"));
//            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return exist;
    }

    /**
     * select all rows in the warehouses table
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void selectAll(){
        String sql = "SELECT username AND password FROM User_Table";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\t" +
                        rs.getDouble("capacity"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



}
