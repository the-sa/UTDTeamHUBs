package com.teamhub.utd.hub;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

public class AddDeviceActivity extends AppCompatActivity {

    EditText name, userID;
    TextView address;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        Bundle bundle = this.getIntent().getExtras();
        BluetoothDevice bluetoothDevice = bundle.getParcelable("Device");

        name = (EditText) findViewById(R.id.dName);
        address = (TextView) findViewById(R.id.maView);
        userID = (EditText) findViewById(R.id.userid);
        add = (Button) findViewById(R.id.button4);

        if (bluetoothDevice.getName() == null) {
            name.setText("null");
        } else {
            name.setText(bluetoothDevice.getName());
        }

        address.setText(bluetoothDevice.getAddress());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Toast.makeText(AddDeviceActivity.this, "Device added",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AddDeviceActivity.this, AdminActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(AddDeviceActivity.this, "Unavailable to add device",
                                        Toast.LENGTH_LONG).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddDeviceActivity.this);
                                builder.setMessage("Device Adding Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                AddDeviceRequest addDevice = new AddDeviceRequest(name.getText().toString(),
                        address.getText().toString(), Integer.parseInt(userID.getText().toString()), responseListener);
                RequestQueue queue2 = Volley.newRequestQueue(AddDeviceActivity.this);
                queue2.add(addDevice);
            }
        });

    }
}
