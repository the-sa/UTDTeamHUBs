package com.teamhub.utd.hub;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // final EditText etAge = (EditText) findViewById(R.id.etAge);
        // final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etUsername = (EditText) findViewById(R.id.editUsername);
        final EditText etPassword = (EditText) findViewById(R.id.editPassword);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                Response.Listener<String> responseListenerUsernameCheck = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (!success) {
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {

                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {

                                                Intent intent = new Intent(RegisterActivity.this, AdminActivity.class);
                                                RegisterActivity.this.startActivity(intent);
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Data registration failure",
                                                        Toast.LENGTH_LONG).show();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                                builder.setMessage("Register Failed")
                                                        .setNegativeButton("Retry", null)
                                                        .create()
                                                        .show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                if((username.length() != 0) && (password.length() != 0)){
                                    RegisterRequest registerRequest = new RegisterRequest(username, password, responseListener);
                                    Toast.makeText(RegisterActivity.this, "Data registered",
                                            Toast.LENGTH_LONG).show();
                                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                                    queue.add(registerRequest);
                                }
                                else{
                                    Toast.makeText(RegisterActivity.this, "Username and Password must have a value",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Username/Password already exist",
                                        Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest registerRequestUsernameCheck = new LoginRequest(username, password, responseListenerUsernameCheck);
                RequestQueue queue2 = Volley.newRequestQueue(RegisterActivity.this);
                queue2.add(registerRequestUsernameCheck);



            }
        });
    }
}
