package com.sparcedge.android.defender.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.sparcedge.android.defender.R;
import com.sparcedge.android.defender.adapter.BluetoothDeviceAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * TODO: Will need to multi-thread this process sine Bluetooth connections are slow and will freeze the UI.
 */
public class DiscoverDefenderActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 1000;
    private static final UUID APP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String TAG = "Core Defender for Android";

    //Bluetooth Members
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDeviceAdapter deviceListAdapter;
    private List<BluetoothDevice> bluetoothDeviceList = new ArrayList<BluetoothDevice>();
    private BluetoothDevice selectedBluetoothDevice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        enableBluetooth();
        inflateDiscoverInterface();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(bluetoothBroadcastReceiver);

        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }
    }

    private void inflateDiscoverInterface() {
        setContentView(R.layout.discover);
        setUpDiscoverButtons();
        setUpDiscoverListView();
    }

    private void inflateConnectionInterface() {
        setContentView(R.layout.connect);
        setUpConnectionButtons();
    }

    private void setUpDiscoverButtons() {

        final Button discoverDevicesButton = (Button) findViewById(R.id.discoverDevices);
        discoverDevicesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bluetoothDeviceList.clear();
                deviceListAdapter.notifyDataSetChanged();
                bluetoothAdapter.startDiscovery();

                // Register the BroadcastReceiver
                IntentFilter filter = new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_FOUND);
                registerReceiver(bluetoothBroadcastReceiver, filter);
            }
        });
    }

    private void setUpDiscoverListView() {
        final ListView deviceListView = (ListView) findViewById(R.id.devicesList);
        deviceListAdapter = new BluetoothDeviceAdapter(this, R.layout.devicelistview, bluetoothDeviceList);

        deviceListView.setAdapter(deviceListAdapter);

        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                selectedBluetoothDevice = (BluetoothDevice) (deviceListView.getItemAtPosition(myItemInt));
                inflateConnectionInterface();
            }
        });
    }

    private void setUpConnectionButtons() {
        final Button lockUnlockButton = (Button) findViewById(R.id.lockUnlock);
        lockUnlockButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try{
                    //Turn off discovery if it is still enabled
                    if(bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }

                    //Create socket
                    BluetoothSocket mSocket = selectedBluetoothDevice.createRfcommSocketToServiceRecord(APP_UUID);
                    mSocket.connect();

                    String hello = "Hello World";
                    OutputStream bluetoothOutputStream = mSocket.getOutputStream();
                    bluetoothOutputStream.write(hello.getBytes());
                    bluetoothOutputStream.close();

                    mSocket.close();

                } catch (IOException e){
                    Log.e(TAG, "Could not connect to device");
                }
            }
        });

        final Button billOfLadingButton = (Button) findViewById(R.id.billOfLading);
        billOfLadingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        final Button downloadActivityButton = (Button) findViewById(R.id.downloadActivity);
        downloadActivityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        final Button discoverOtherDevicesButton = (Button) findViewById(R.id.discoverOtherDevices);
        discoverOtherDevicesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inflateDiscoverInterface();
            }
        });
    }

    /**
     * Returns the BluetoothAdapter for the Android device.
     * @return
     */
    private BluetoothAdapter getBluetoothAdapter() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.i(TAG, "No Bluetooth Adapter for Device");
        }

        return bluetoothAdapter;
    }

    /**
     * Enables Bluetooth on the Android device and fires up the Intent for the user to enable Bluetooth if it is not.
     */
    private void enableBluetooth() {
        bluetoothAdapter = getBluetoothAdapter();
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    /**
     * Enables Discoverable mode ad fires up the Intent that allows the user to enable discoverability.
     */
    private void ensureDiscoverable() {
        Log.d(TAG, "ensure discoverable");
        if (bluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /*************
     * Receivers *
     ************/
    //TODO: Will need to unregister the listener
    private final BroadcastReceiver bluetoothBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                // Get the BluetoothDeviceAdapter object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(android.bluetooth.BluetoothDevice.EXTRA_DEVICE);

                if (!bluetoothDeviceList.contains(device)) {
                    bluetoothDeviceList.add(device);
                    deviceListAdapter.notifyDataSetChanged();
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                if (deviceListAdapter.getCount() == 0) {
                    //TODO - Update a label somewhere to alert the user that no devices we found.
                }
            }
        }
    };
}