package com.teamhub.utd.hub;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

public class DeviceDetailActivity extends AppCompatActivity {

    private final static String unpaired = "UNPAIRED_DEVICE";
    BluetoothDevice bluetoothDevice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        final Switch pairSwitch = (Switch) findViewById(R.id.pairswitch);
        final TextView signalStrength = (TextView) findViewById(R.id.signalStr);
        final TextView DeviceName = (TextView) findViewById(R.id.DeviceName);
        final TextView MacAddress = (TextView) findViewById(R.id.MacAddress);
        final TextView signalTag= (TextView) findViewById(R.id.signalStr);
        final TextView deviceTag= (TextView) findViewById(R.id.dname);
        final TextView MACTag = (TextView) findViewById(R.id.madd);
        final Button AssignButton = (Button) findViewById(R.id.AssignBut);



        AssignButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DeviceDetailActivity.this, AddDeviceActivity.class);
                Bundle b = new Bundle();
                if(bluetoothDevice.getBondState()==BluetoothDevice.BOND_BONDED)
                {
                    b.putParcelable("Device", bluetoothDevice);
                    i.putExtras(b);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(DeviceDetailActivity.this, "Device needs to be paired before it can be added.",
                            Toast.LENGTH_LONG).show();
                }



            }

        });
        // button to do something

        Bundle b = this.getIntent().getExtras();
        Intent intent = getIntent();
        //int intValue = intent.getIntExtra("hello", 0);

        //String DeviceNames=bluetoothDevice.getName();

/*
        Intent intent = this.getIntent();
        int  rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
        String pairedStr = String.valueOf(rssi);
        signalStrength.setText(pairedStr);*/


        if(b != null)
        {

            bluetoothDevice = b.getParcelable(unpaired);
            /*if(bluetoothDevice.getBondState() == BluetoothDevice.BOND_NONE){
                String unPairName = bluetoothDevice.getName();

                DeviceName.setText(unPairName);
            }*/

        }
        if(bluetoothDevice !=null)
        {

            deviceTag.setText("Device Name");
            signalTag.setText("Signal Strength");
            MACTag.setText("MAC Address");
            String DeviceNames = bluetoothDevice.getName();
            Log.d("Hi",DeviceNames);
            DeviceName.setText(DeviceNames);

            String MacAdresses = bluetoothDevice.getAddress();
            MacAddress.setText(MacAdresses);


            double rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
            String pairedStr = String.valueOf(rssi);
            signalStrength.setText(pairedStr);
            Log.d(" FOUND", "this");

            if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {



                pairSwitch.setChecked(true);
            }



        }

        else
            Log.d("NOT FOUND", "this");




        //button listener
        pairSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {



            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pairSwitch.setText("Paired");
                    pairDevice(bluetoothDevice);

                } else {
                    pairSwitch.setText("Unpaired");
                    unpairDevice(bluetoothDevice);


                }
            }
        });
    }
    /*
    private void getRssi(BluetoothDevice device){
        Intent intent ;
        intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
    }*/






    private void pairDevice(BluetoothDevice device) {
        try {
            Log.e("in pair", "yuh");
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
            Toast.makeText(DeviceDetailActivity.this, "Device is now pairing",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
            Toast.makeText(DeviceDetailActivity.this, "Device is now unpaired",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    Toast.makeText(MainActivity.this, "Paired", Toast.LENGTH_LONG).show();
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
                    Toast.makeText(MainActivity.this, "Unpaired", Toast.LENGTH_LONG).show();
                }

            }
        }
    };*/

}
