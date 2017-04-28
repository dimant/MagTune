package com.dtodorov.magtune.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.dtodorov.androlib.asyncIO.AsyncIOStream;
import com.dtodorov.androlib.asyncIO.IAsyncIOListener;
import com.dtodorov.androlib.asyncIO.IAsyncIOStream;
import com.dtodorov.androlib.eventdispatcher.EventDispatcher;
import com.dtodorov.androlib.eventdispatcher.IEventDispatcher;
import com.dtodorov.androlib.eventdispatcher.IEventListener;
import com.dtodorov.androlib.eventdispatcher.IViewEventExtensions;
import com.dtodorov.androlib.eventdispatcher.ViewEventExtensions;
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
    private IEventDispatcher _eventDispatcher;
    private IViewEventExtensions _viewEventExtensions;
    private IIntentService _intentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = getApplicationContext();

        _eventDispatcher = new EventDispatcher();
        _viewEventExtensions = new ViewEventExtensions(this);
        _viewEventExtensions.register(_eventDispatcher);

        IStringResolver stringResolver = new StringResolver(getResources());

        DialogService dialogService = new DialogService();
        dialogService.initialize( getFragmentManager(), R.string.button_ok, R.string.button_cancel);

        IPermissionService permissionService = new PermissionService(
                new PermissionRequester(this),
                dialogService);

        _intentService = new IntentService(this);

        _eventDispatcher.register(MainController.ShowBoundedDevices, new IEventListener() {
            @Override
            public void callback(Object param) {
                ArrayList<IBluetoothConnectableDevice> devices = (ArrayList<IBluetoothConnectableDevice>) param;
                BluetoothDeviceAdapter adapter = new BluetoothDeviceAdapter(MainActivity.this, devices);
                ListView listView = (ListView) findViewById(R.id.lvDevices);
                listView.setAdapter(adapter);
            }
        });

        _mainController = new MainController(
                stringResolver,
                permissionService,
                _eventDispatcher,
                new Toaster(context, stringResolver),
                new BluetoothService(_intentService));

        ListView listView = (ListView) findViewById(R.id.lvDevices);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _mainController.fire(MainController.Trigger.Connect, position);
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner_speed);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button buttonLeft = (Button) findViewById(R.id.button_left);
        buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN: break;
                    case MotionEvent.ACTION_UP: break;
                }
                return true;
            }
        });

        Button buttonRight = (Button) findViewById(R.id.button_right);
        buttonRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN: break;
                    case MotionEvent.ACTION_UP: break;
                }
                return true;
            }
        });

        Button buttonDisconnect = (Button) findViewById(R.id.button_disconnect);
        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        GraphView graph = (GraphView) findViewById(R.id.graph);
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph.addSeries(series);
//        if(bluetoothService.isEnabled() == false)
//        {
//            bluetoothService.enableBluetooth(new IBluetoothEnableListener()
//            {
//                @Override
//                public void onOk()
//                {
//
//                }
//
//                @Override
//                public void onCancelled()
//                {
//
//                }
//            });
//        }
//
//        final ArrayList<IBluetoothConnectableDevice> devices = bluetoothService.getBondedDevices();
//        BluetoothDeviceAdapter adapter = new BluetoothDeviceAdapter(this, devices);
//        ListView listView = (ListView) findViewById(R.id.lvDevices);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//            {
//                IBluetoothConnectableDevice device = devices.get(position);
//                stream = device.connect(new IAsyncIOListener()
//                {
//                    @Override
//                    public void onError(IOException e)
//                    {
//
//                    }
//
//                    @Override
//                    public void onReceived(byte[] buffer, int bytes)
//                    {
//
//                    }
//
//                    @Override
//                    public void onClosed()
//                    {
//
//                    }
//                }, 1024);
//                while(true)
//                {
//                    stream.write(new byte[] {'h','i'});
//                    try
//                    {
//                        Thread.sleep(300);
//                    } catch (InterruptedException e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//        }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        _intentService.onActivityResult(requestCode, resultCode, data);
    }
}
