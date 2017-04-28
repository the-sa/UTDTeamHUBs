package com.teamhub.utd.hub;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@TargetApi(21)
public class BLEActivity extends ActionBarActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private BluetoothGatt mGatt;
    private BluetoothDevice mDevice;
    private ListView bleDevicesListView;
    private ArrayAdapter<String> bleDevicesArrayAdapter;
    private ArrayList<BluetoothDevice> bluetoothDevices;
    private int batterylevel;
    private static final UUID Battery_Service_UUID = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb");
    private static final UUID Battery_Level_UUID = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");
    String mDeviceAddress;
    private final static int MY_PERMISSION_RESPONSE=2;
    // setup UI handler
    private final static int UPDATE_DEVICE = 0;
    private final static int UPDATE_VALUE = 1;
    private final Handler uiHandler = new Handler() {
        public void handleMessage(Message msg) {
            final int what = msg.what;
            final String value = (String) msg.obj;
            switch(what) {
                case UPDATE_DEVICE: updateDevice(value); break;
                case UPDATE_VALUE: updateValue(value); break;
            }
        }
    };

    private void updateDevice(String devName){
        TextView t=(TextView)findViewById(R.id.dev_type);
        t.setText(devName);
    }

    private void updateValue(String value){
        TextView t=(TextView)findViewById(R.id.value_read);
        t.setText(value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        mHandler = new Handler();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        bleDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        bleDevicesListView = (ListView)findViewById(R.id.ble_list_view);
        bleDevicesListView.setAdapter(bleDevicesArrayAdapter);
        bleDevicesListView.setOnItemClickListener(mDeviceClickListener);
        bluetoothDevices = new ArrayList<>();
        //listener
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                filters = new ArrayList<ScanFilter>();
            }
            scanLeDevice(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scanmenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.scan_devices:
                scanLeDevice(true);
                return true;
            case R.id.stop_scan_devices:
                scanLeDevice(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            scanLeDevice(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGatt == null) {
            return;
        }
        mGatt.close();
        mGatt = null;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                //Bluetooth not enabled.
                finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void scanLeDevice(final boolean enable) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            Log.w("Unpaired Activity", "Location access not granted!");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_RESPONSE);
        }
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT < 21) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    } else {
                        mLEScanner.stopScan(mScanCallback);

                    }
                }
            }, SCAN_PERIOD);
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mLEScanner.startScan(filters, settings, mScanCallback);
            }
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else {
                mLEScanner.stopScan(mScanCallback);
            }
        }
    }

    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //bluetoothAdapter.cancelDiscovery();
            final BluetoothDevice device = bluetoothDevices.get(i);


            connectToDevice(device.getAddress().toString());
            updateDevice(device.getName());
            updateValue("-");
            //String info = ((TextView)view).getText().toString();
            //Log.i("INfo",info+i);
        }
    };

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i("callbackType", String.valueOf(callbackType));
            String devicename = result.getDevice().getName();
            Log.i("devicename", ""+devicename);
            if (devicename != null){
                //if (devicename.startsWith("TAIDOC")){
                Log.i("result", "Device name: "+devicename);
                //HERE IS THE RESULT
                Log.i("result", result.toString());
                BluetoothDevice btDevice = result.getDevice();

                if(!bluetoothDevices.contains(btDevice)) {
                    bleDevicesArrayAdapter.add(btDevice.getName()+"\n"+btDevice.getAddress());
                    bluetoothDevices.add(btDevice);
                    //connectToDevice(btDevice);
                }
                //}
            }

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.i("onLeScan", device.toString());
                            //connectToDevice(device);
                        }
                    });
                }
            };

    public void connectToDevice(String deviceAddress) {

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceAddress);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {


                if (device != null) {

                    mGatt = device.connectGatt(getApplicationContext(), false, gattCallback);
                    scanLeDevice(false);// will stop after first device detection
                }
            }
        });
    }
    /*
    public void connectToDevice(BluetoothDevice device) {
        //if (mGatt == null) {
        Log.d("connectToDevice", "connecting to device: "+device.toString());
        this.mDevice = device;
        mGatt = device.connectGatt(this, false, gattCallback);
        scanLeDevice(false);// will stop after first device selection
    //}
    }
    */
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {


        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");

                    //update UI
                    Message msg = Message.obtain();

                    String deviceName = gatt.getDevice().getName();

                    /*
                    switch (deviceName){
                        case "TAIDOC TD1261":
                            deviceName = "Thermo";
                            break;
                        case "TAIDOC TD8255":
                            deviceName = "SPO2";
                            break;
                        case "TAIDOC TD4279":
                            deviceName = "SPO2";
                            break;
                    }
                    */

                    msg.obj = deviceName;
                    msg.what = 0;
                    msg.setTarget(uiHandler);
                    msg.sendToTarget();

                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    Log.i("gattCallback", "reconnecting...");
                    mDevice = gatt.getDevice();
                    mGatt = null;

                    connectToDevice(mDevice.getAddress().toString());
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            mGatt = gatt;
            List<BluetoothGattService> services = gatt.getServices();
            Log.i("onServicesDiscovered", services.size()+ services.toString());
            int serviceNo = 0;
            for(int i=0; i<services.size();i++){
                if(services.get(i).getUuid().equals(Battery_Service_UUID)){
                    serviceNo = i;
                }
            }

            BluetoothGattCharacteristic therm_char = services.get(serviceNo).getCharacteristics().get(0);

            /*
            for (BluetoothGattDescriptor descriptor : therm_char.getDescriptors()) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mGatt.writeDescriptor(descriptor);
            }
            */
            //gatt.readCharacteristic(therm_char);
            //int level = therm_char.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8,0);
            //Log.i("The battery level is: ",level+"");
            mGatt.readCharacteristic(therm_char);
            //gatt.setCharacteristicNotification(therm_char, true);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic
                                                 characteristic, int status) {

            Log.i("onCharacteristicRead", characteristic.getUuid().toString());
            //byte[] value=characteristic.getValue();
            byte[] value = characteristic.getValue();
            int flag =characteristic.getProperties();

            //int value1 =characteristic.getIntValue(characteristic.FORMAT_FLOAT, 0);
            //String v = new String(value);
            String v = value+"";
            Log.i("onCharacteristicRead", "Value: " + v+" "+flag);
            //gatt.disconnect();
            final StringBuilder stringBuilder = new StringBuilder(value.length);
            for(byte byteChar : value)
                stringBuilder.append(String.format("%02X ", byteChar));

            //final   int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d("THE FORM", " format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d("THE FORM", "  UINT8.");
            }
            batterylevel = characteristic.getIntValue(format, 0);
            Log.i("THE BATTERY",batterylevel+"");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateValue(batterylevel+"");
                    if(mGatt != null) {
                        mGatt.disconnect();
                    }
                }
            });



        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic
                                                    characteristic) {
            //float char_float_value = characteristic.getFloatValue(BluetoothGattCharacteristic.FORMAT_FLOAT,1);
            float char_float_value = characteristic.getFloatValue(BluetoothGattCharacteristic.FORMAT_FLOAT,0);
            Log.i("onCharacteristicChanged", Float.toString(char_float_value));


            String deviceName = gatt.getDevice().getName();
            Log.i("device name", deviceName);
            String value = null;
            /*
            switch (deviceName){
                case "TAIDOC TD1261":
                    value = Float.toString(char_float_value);
                    break;
                case "TAIDOC TD8255":
                    value = Float.toString(char_float_value*10);
                    break;
            }*/

            //update UI
            Message msg = Message.obtain();
            msg.obj = value;
            msg.what = 1;
            msg.setTarget(uiHandler);
            msg.sendToTarget();

            //gatt.disconnect();
        }



    };
}