package com.teamhub.utd.hub;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
                //   final String name = etName.getText().toString();
                final String username = etUsername.getText().toString();
                //    final int age = Integer.parseInt(etAge.getText().toString());
                final String password = etPassword.getText().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if((username.length() != 0) && (password.length() != 0)){
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(RegisterActivity.this, "Data registered",
                                                Toast.LENGTH_LONG).show();
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
                                }
                                else{
                                    Toast.makeText(RegisterActivity.this, "Username and Password must have a value",
                                            Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };




                RegisterRequest registerRequest = new RegisterRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }
}
