package com.dtodorov.magtune.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dtodorov.androlib.services.IBluetoothConnectableDevice;
import com.dtodorov.magtune.R;

import java.util.List;

public class BluetoothDeviceAdapter extends ArrayAdapter<IBluetoothConnectableDevice>
{
    public BluetoothDeviceAdapter(@NonNull Context context, @NonNull List<IBluetoothConnectableDevice> devices)
    {
        super(context, 0, devices);
    }

    private class ViewHolder
    {
        TextView Name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        IBluetoothConnectableDevice connectableDevice = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_device, parent, false);
            viewHolder.Name = (TextView) convertView.findViewById(R.id.tvName);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        String deviceName = connectableDevice.getName();
        viewHolder.Name.setText(deviceName);

        // Return the completed view to render on screen
        return convertView;
    }
}
