package com.teamhub.utd.hub;


import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.lang.reflect.Method;

public class DeviceDetailActivity extends AppCompatActivity {

    private final static String unpaired = "UNPAIRED_DEVICE";
    BluetoothDevice bluetoothDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        final Switch pairSwitch = (Switch) findViewById(R.id.pairswitch);

        // button to do something

        Bundle b = this.getIntent().getExtras();
        if(b != null)
        {
            bluetoothDevice = b.getParcelable(unpaired);
        }
        if(bluetoothDevice !=null)
        {
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
    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

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
