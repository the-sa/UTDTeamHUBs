package com.teamhub.utd.hub;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

/*
- need to get prompt for location permission
- repurpose bluetooth switch
- use a form field for device addition to access this activity so it can return address and set
the field
 */

public class UnPairedActivity extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;


    /*Reference for action bar buttons. Not appropriate for scan since it's not
    * being initialized at startup */
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scanmenu, menu);
        return true;
    }
    */

    /*
    - when a device is clicked, gets the mac address and sends it in Intent and closes the activity
     */
    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            bluetoothAdapter.cancelDiscovery();



            String info = ((TextView)view).getText().toString();
            String address = info.substring(info.length()-17);


            Intent intent = new Intent();
            intent.putExtra("MACADDRESS", address);

            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //skip if it's already paired
                if(device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress(); // MAC address

                    Log.i("Mac address:", deviceHardwareAddress + " " + deviceName);
                    mNewDevicesArrayAdapter.add(deviceName+"\n"+deviceHardwareAddress);
                }
            }
            else{
                Log.i("NOT FOUND HERE", "This");
            }
        }
    };

    /*
    cancel discovery and unregister receiver
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*stop discovery*/
        if(bluetoothAdapter != null){
            bluetoothAdapter.cancelDiscovery();
        }

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }

    /*start discovery*/
    public void doScanning(){
        Log.i("START","I DID");
        /*stop scanning if already scanning*/
        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }

        /*Request scanning*/
        bluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaired);
        // set up bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        bluetoothOnOff();

        /*scan button for starting discovery */
        FloatingActionButton scanButton = (FloatingActionButton) findViewById(R.id.searchDevicesButton);
        if(scanButton != null){
            scanButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    doScanning();
                }
            });
        }

        /*
        - New device array to store paired devices

         */
        ArrayAdapter<String> pairedDevicesArrayAdapter =
                new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.list_paired_bluetooth);
        pairedListView.setAdapter(pairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = (ListView) findViewById(R.id.list_unpaired_bluetooth);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // Get the local Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        Log.i("Hello",pairedDevices.size()+"");

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            findViewById(R.id.paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.no_devices).toString();
            pairedDevicesArrayAdapter.add(noDevices);
        }

    }


    /*
     bluetooth on off switch.
    */
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
        // if it is already enabled then disable it
        /* disabling the switch for a while
        if(bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();

            IntentFilter BTIntent2 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mReceiver, BTIntent2);
        } 
        */
    }
}
