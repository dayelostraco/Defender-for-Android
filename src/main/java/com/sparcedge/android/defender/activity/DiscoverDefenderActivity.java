package com.sparcedge.android.defender.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.sparcedge.android.defender.R;
import com.sparcedge.android.defender.adapter.DefenderAdapter;
import com.sparcedge.android.defender.model.Defender;

import java.util.ArrayList;
import java.util.List;

public class DiscoverDefenderActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 1000;

    private static String TAG = "Core Defender for Android";
    private BluetoothAdapter bluetoothAdapter;
    private DefenderAdapter deviceListAdapter;
    private List<Defender> bluetoothDeviceList = new ArrayList<Defender>();
    private Defender selectedDefender;

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.discover);

        enableBluetooth();
        setUpButtons();
        setUpListView();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(bluetoothBroadcastReceiver);
        bluetoothAdapter.cancelDiscovery();
    }

    private void setUpButtons() {

        final Button discoverDevicesButton = (Button) findViewById(R.id.discoverDevices);
        discoverDevicesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bluetoothDeviceList.clear();
                bluetoothAdapter.startDiscovery();

                // Register the BroadcastReceiver
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(bluetoothBroadcastReceiver, filter);
            }
        });
    }

    private void setUpListView() {
        final ListView deviceListView = (ListView) findViewById(R.id.devicesList);
        deviceListAdapter = new DefenderAdapter(this, R.layout.defenderlistview, bluetoothDeviceList);

        deviceListView.setAdapter(deviceListAdapter);

        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                selectedDefender = (Defender) (deviceListView.getItemAtPosition(myItemInt));
                setContentView(R.layout.connect);
            }
        });
    }

    private BluetoothAdapter getBluetoothAdapter(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.i(TAG, "No Bluetooth Adapter for Device");
        }

        return bluetoothAdapter;
    }

    private void enableBluetooth() {
        bluetoothAdapter = getBluetoothAdapter();
        if (bluetoothAdapter!=null && !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
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

                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Defender defender = new Defender(device.getName(), device.getAddress(), device);

                if(!bluetoothDeviceList.contains(defender)){
                    bluetoothDeviceList.add(defender);
                    deviceListAdapter.notifyDataSetChanged();
                }
            }
        }
    };
}