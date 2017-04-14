package com.teamhub.utd.hub;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class  LoginActivity extends AppCompatActivity {

    DBHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUsername = (EditText) findViewById(R.id.editUsername);
        final EditText etPassword = (EditText) findViewById(R.id.editPassword);
        final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);
        final Button bLogin = (Button) findViewById(R.id.bSignIn);
        db = new DBHandler(this);

        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get username and password from textedit
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                // check if the user exist and get password from database
                String dbPassword = db.readUserPassword(username);
                // check if password from database is equal to password from user
                if ((dbPassword != null) && (dbPassword.equals(password))) {
                    Log.e("This: ", dbPassword);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // if the user is not found in database or if password do not match
                    Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_LONG).show();
                }

                /*System.out.println("A");
                Log.e("Tag", "A");

                //checks to see login is in db
                boolean isVerified = loginRequest.isVerified(username, password);
                if(isVerified == true){
                    System.out.println("B");
                    Log.e("Tag", "B");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(intent);
                }
                else{
                    System.out.println("C");
                    Log.e("Tag", "C");
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Login Failed")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                }*/

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