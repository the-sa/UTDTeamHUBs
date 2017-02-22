package com.teamhub.utd.hub;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // create variables
    Button button;
    BluetoothAdapter bluetoothAdapter;
    ArrayAdapter<String> mNewDeviceListArrayAdapter;

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.i("Mac address:", deviceHardwareAddress + " " + deviceName);


                /*
                sa 2/22 - get list of devices that are not paired
                 */
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDeviceListArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }

            /*
             sa 2/22 - after scan is done and bluetooth device is not found add no new devices to list.
                Also now you can select device to connect to.
             */
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setTitle("Select device to connect");
                if (mNewDeviceListArrayAdapter.getCount() == 0) {
                    String noDevices = "No devices found";
                    mNewDeviceListArrayAdapter.add(noDevices);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // set up bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



        // button to do something
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothOnOff();
            }
        });
    }

    private void bluetoothOnOff() {
        if (bluetoothAdapter == null) {
            Log.i("Error:", "Not working");
        }
        // if it is not enable then enable
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mReceiver, BTIntent);
        }
        // if it is already enable then disable it
        if(bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();

            IntentFilter BTIntent2 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mReceiver, BTIntent2);
        }
    }
}
