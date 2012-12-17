package com.sparcedge.android.defender.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sparcedge.android.defender.R;
import com.sparcedge.android.defender.model.Defender;

import java.util.List;

/**
 * User: Dayel Ostraco
 * Date: 12/17/12
 * Time: 4:35 PM
 */
public class DefenderAdapter extends ArrayAdapter {

    int resource;
    String response;
    Context context;

    public DefenderAdapter(Context context, int resource, List<Defender> defenders) {
        super(context, resource, defenders);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout defenderView;
        //Get the current alert object
        Defender defender = (Defender) getItem(position);

        //Inflate the view
        if (convertView == null) {
            defenderView = new RelativeLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, defenderView, true);
        } else {
            defenderView = (RelativeLayout) convertView;
        }

        //Get the text boxes from the select_child_list_item.xml file
        TextView deviceName = (TextView) defenderView.findViewById(R.id.deviceName);
        TextView deviceMac = (TextView) defenderView.findViewById(R.id.deviceMac);

        //Assign the appropriate data from our defender object above
        deviceName.setText(defender.getName());
        deviceMac.setText(defender.getMacAddress());

        return defenderView;
    }
}
