package com.dtodorov.magtune.activities;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.dtodorov.androlib.services.*;
import com.dtodorov.magtune.R;
import com.dtodorov.magtune.adapters.BluetoothDeviceAdapter;
import com.dtodorov.magtune.controllers.MainController;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private MainController _mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        GraphView graph = (GraphView) findViewById(R.id.graph);
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph.addSeries(series);

        IStringResolver stringResolver = new StringResolver(getResources());

        DialogService dialogService = new DialogService();
        dialogService.initialize( getFragmentManager(), R.string.button_ok, R.string.button_cancel);

        IPermissionService permissionService = new PermissionService(
                new PermissionRequester(this),
                dialogService);

        IIntentService intentService = new IntentService(this);

        _mainController = new MainController(
                stringResolver,
                permissionService);

        IBluetoothService bluetoothService = new BluetoothService(intentService);
        if(bluetoothService.isEnabled() == false)
        {
            bluetoothService.enableBluetooth(new IBluetoothEnableListener()
            {
                @Override
                public void onOk()
                {

                }

                @Override
                public void onCancelled()
                {

                }
            });
        }

        ArrayList<BluetoothDevice> devices = bluetoothService.getBondedDevices();
        BluetoothDeviceAdapter adapter = new BluetoothDeviceAdapter(this, devices);
        ListView listView = (ListView) findViewById(R.id.lvDevices);
        listView.setAdapter(adapter);
    }
}
