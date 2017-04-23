package com.teamhub.utd.hub;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUsername = (EditText) findViewById(R.id.editUsername);
        final EditText etPassword = (EditText) findViewById(R.id.editPassword);
        final Button bLogin = (Button) findViewById(R.id.bSignIn);


        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                // Response received from the server
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                String usernameReturned = jsonResponse.getString("username");
                                String passwordReturned = jsonResponse.getString("password");
                                Integer roleReturned = jsonResponse.getInt("role");
                                String idReturned = jsonResponse.getString("id");
                                //log comments for validation. Okay to remove
                                Log.e("username", usernameReturned);
                                Log.e("password", passwordReturned);
                                Log.e("id", idReturned);
                                Log.e("role", roleReturned.toString());
                                ///////
                                if ((usernameReturned.contentEquals(username)) && (passwordReturned.equals(password))){
                                    if(roleReturned == 1){
                                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                        LoginActivity.this.startActivity(intent);
                                    }
                                    else{
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("UserID", jsonResponse.getInt("id"));
                                        LoginActivity.this.startActivity(intent);
                                    }

                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "Username or password is incorrect",Toast.LENGTH_LONG).show();
                                }
                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }
}































//
//    // Response received from the server
//    Response.Listener<String> responseListener = new Response.Listener<String>() {
//        @Override
//        public void onResponse(String response) {
//            try {
//                JSONObject jsonResponse = new JSONObject(response);
//                boolean success = jsonResponse.getBoolean("success");
//
//                if (success) {
//                    String name = jsonResponse.getString("name");
//                    int age = jsonResponse.getInt("age");
//
//                    Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
//                    intent.putExtra("name", name);
//                    intent.putExtra("age", age);
//                    intent.putExtra("username", username);
//                    LoginActivity.this.startActivity(intent);
//                } else {
////                                Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
////                                LoginActivity.this.startActivity(intent);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                    builder.setMessage("Login Failed")
//                            .setNegativeButton("Retry", null)
//                            .create()
//                            .show();
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    };

// LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
// queue = Volley.newRequestQueue(LoginActivity.this);
// queue.add(loginRequest);