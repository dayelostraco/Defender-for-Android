package com.sparcedge.android.defender.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sparcedge.android.defender.R;

import java.util.List;

/**
 * User: Dayel Ostraco
 * Date: 12/17/12
 * Time: 4:35 PM
 */
public class BluetoothDeviceAdapter extends ArrayAdapter {

    int resource;
    String response;
    Context context;

    public BluetoothDeviceAdapter(Context context, int resource, List<BluetoothDevice> bluetoothDevices) {
        super(context, resource, bluetoothDevices);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout deviceView;
        //Get the current alert object
        BluetoothDevice device = (BluetoothDevice) getItem(position);

        //Inflate the view
        if (convertView == null) {
            deviceView = new RelativeLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, deviceView, true);
        } else {
            deviceView = (RelativeLayout) convertView;
        }

        //Get the text boxes from the select_child_list_item.xml file
        TextView deviceName = (TextView) deviceView.findViewById(R.id.deviceName);
        TextView deviceMac = (TextView) deviceView.findViewById(R.id.deviceMac);

        //Assign the appropriate data from our defender object above
        deviceName.setText(device.getName());
        deviceMac.setText(device.getAddress());

        return deviceView;
    }
}
