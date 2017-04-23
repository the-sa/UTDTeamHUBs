package com.teamhub.utd.hub;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DeviceInfoActivity extends AppCompatActivity {

    TextView name, address, battery, signal, userID, userIDLable;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        Intent intent = getIntent();
        final Devices device = (Devices) intent.getSerializableExtra("Device");
        int role = intent.getIntExtra("UserRole", 0);

        name = (TextView) findViewById(R.id.deviceUsername);
        address = (TextView) findViewById(R.id.deviceAddress);
        battery = (TextView) findViewById(R.id.deviceBattery);
        signal = (TextView) findViewById(R.id.deviceSignal);
        userID = (TextView) findViewById(R.id.deviceUserID);
        userIDLable = (TextView) findViewById(R.id.textView8);
        delete = (Button) findViewById(R.id.deleteDevice);

        if (role == 1) {
            delete.setVisibility(View.VISIBLE);
            userID.setText(device.getUserID()+"");
            userIDLable.setVisibility(View.VISIBLE);
        }

        name.setText(device.getName());
        address.setText(device.getMacaddress());
        battery.setText(device.getBatteryLife()+"");
        signal.setText("This is where signal will be displayed");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(DeviceInfoActivity.this, "Device Deleted",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(DeviceInfoActivity.this, AdminActivity.class);
                                DeviceInfoActivity.this.startActivity(intent);
                            } else {
                                Toast.makeText(DeviceInfoActivity.this, "Device Deleted failure",
                                        Toast.LENGTH_LONG).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(DeviceInfoActivity.this);
                                builder.setMessage("Device Deleted Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                RemoveDeviceRequest removeDeviceRequest = new RemoveDeviceRequest(device.getId(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(DeviceInfoActivity.this);
                queue.add(removeDeviceRequest);
            }
        });

    }
}
