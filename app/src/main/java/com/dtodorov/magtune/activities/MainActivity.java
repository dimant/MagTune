package com.dtodorov.magtune.activities;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dtodorov.androlib.asyncIO.AsyncIOStream;
import com.dtodorov.androlib.asyncIO.IAsyncIOListener;
import com.dtodorov.androlib.asyncIO.IAsyncIOStream;
import com.dtodorov.androlib.services.*;
import com.dtodorov.magtune.R;
import com.dtodorov.magtune.adapters.BluetoothDeviceAdapter;
import com.dtodorov.magtune.controllers.MainController;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private MainController _mainController;
    private IAsyncIOStream stream;

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

        final ArrayList<IBluetoothConnectableDevice> devices = bluetoothService.getBondedDevices();
        BluetoothDeviceAdapter adapter = new BluetoothDeviceAdapter(this, devices);
        ListView listView = (ListView) findViewById(R.id.lvDevices);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                IBluetoothConnectableDevice device = devices.get(position);
                stream = device.connect(new IAsyncIOListener()
                {
                    @Override
                    public void onError(IOException e)
                    {

                    }

                    @Override
                    public void onReceived(byte[] buffer, int bytes)
                    {

                    }

                    @Override
                    public void onClosed()
                    {

                    }
                }, 1024);
                while(true)
                {
                    stream.write(new byte[] {'h','i'});
                    try
                    {
                        Thread.sleep(300);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
        }
        });
    }
}
