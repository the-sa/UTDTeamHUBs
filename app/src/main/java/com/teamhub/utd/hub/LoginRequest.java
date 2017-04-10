package com.teamhub.utd.hub;

import android.annotation.TargetApi;
import android.os.Build;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "jdbc:sqlite:c://sqlite/db/RMDB.db";

// //data/data/<Your-Application-Package-Name>/databases/<your-database-name>

    public LoginRequest(String username, String password, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);


    }

    public static class SelectApp {

        /**
         * Connect to the test.db database
         * @return the Connection object
         */
        private Connection connect() {
            // SQLite connection string
            String url = "jdbc:sqlite:C://sqlite/db/RMDB.db";
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url);
                System.out.println("FOUND DATABASE");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return conn;
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


        /**
         * @param args the command line arguments
         */
        public static void main(String[] args) {
            SelectApp app = new SelectApp();
            app.selectAll();
        }

    }



}
