package com.teamhub.utd.hub;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
    This is the normal User Activity.
 */

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<Devices> devicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        int user_id = intent.getIntExtra("UserID", 0);

        final ArrayList<Devices> devices = new ArrayList<Devices>();

        ListView list = (ListView) findViewById(R.id.deviceUserList);
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.deviceRefrest);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonResponse = null;
                boolean success;
                try {
                    jsonResponse = new JSONObject(response);
                    //success = jsonResponse.getBoolean("result");

                    if (true) {
                        JSONArray array = jsonResponse.getJSONArray("result");
                        for (int i = 0; i < array.length(); i++) {
                            Devices device = new Devices();
                            device.setId(array.getJSONObject(i).getInt("id"));
                            device.setName(array.getJSONObject(i).getString("device_name"));
                            device.setMacaddress(array.getJSONObject(i).getString("mac_address"));
                            device.setBatteryLife(array.getJSONObject(i).getInt("battery_level"));
                            device.setUserID(array.getJSONObject(i).getInt("user_id"));
                            devices.add(device);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        // call request
        GetDeviceUserRequest getDeviceUserRequest = new GetDeviceUserRequest(user_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(getDeviceUserRequest);

        // array adapter
        devicesArrayAdapter = new ArrayAdapter<Devices>(MainActivity.this, android.R.layout.simple_list_item_1, devices);
        list.setAdapter(devicesArrayAdapter);
        list.invalidate();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResume();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DeviceInfoActivity.class);
                Devices device = (Devices) parent.getItemAtPosition(position);
                intent.putExtra("Device", device);
                intent.putExtra("UserRole", 0);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume () {
        super.onResume();
        devicesArrayAdapter.notifyDataSetChanged();

    }
}
