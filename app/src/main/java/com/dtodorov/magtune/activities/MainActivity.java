package com.dtodorov.magtune.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.dtodorov.androlib.eventdispatcher.EventDispatcher;
import com.dtodorov.androlib.eventdispatcher.IEventDispatcher;
import com.dtodorov.androlib.eventdispatcher.IEventListener;
import com.dtodorov.androlib.eventdispatcher.IViewEventExtensions;
import com.dtodorov.androlib.eventdispatcher.ViewEventExtensions;
import com.dtodorov.androlib.services.*;
import com.dtodorov.magtune.R;
import com.dtodorov.magtune.adapters.BluetoothDeviceAdapter;
import com.dtodorov.magtune.controllers.MainController;
import com.dtodorov.magtune.protocol.MagTuneParser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver _broadcastReceiverBluetoothService;
    private BroadcastReceiver _broadcastReceiverBluetoothDevice;

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

        final IBluetoothService bluetoothService = new BluetoothService(_intentService);

        _broadcastReceiverBluetoothService = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                bluetoothService.onReceive(context, intent);
            }
        };

        registerReceiver(_broadcastReceiverBluetoothService, bluetoothService.getFilter());

        _mainController = new MainController(
                stringResolver,
                permissionService,
                _eventDispatcher,
                new Toaster(context, stringResolver),
                bluetoothService,
                new MagTuneParser());

        final ListView listView = (ListView) findViewById(R.id.lvDevices);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _mainController.fire(
                        MainController.Trigger.Connect,
                        listView.getItemAtPosition(position));
            }
        });

        Button buttonLeft = (Button) findViewById(R.id.button_left);
        buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Spinner spinner = (Spinner) findViewById(R.id.spinner_speed);
                int speed = spinner.getSelectedItemPosition();

                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        _eventDispatcher.emit(MainController.RotateLeft, speed);
                        break;
                    case MotionEvent.ACTION_UP:
                        _eventDispatcher.emit(MainController.RotateStop, null);
                        break;
                }
                return true;
            }
        });

        Button buttonRight = (Button) findViewById(R.id.button_right);
        buttonRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Spinner spinner = (Spinner) findViewById(R.id.spinner_speed);
                int speed = spinner.getSelectedItemPosition();

                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        _eventDispatcher.emit(MainController.RotateRight, speed);
                        break;
                    case MotionEvent.ACTION_UP:
                        _eventDispatcher.emit(MainController.RotateStop, null);
                        break;
                }
                return true;
            }
        });

        Button buttonDisconnect = (Button) findViewById(R.id.button_disconnect);
        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mainController.fire(MainController.Trigger.Disconnect);           }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(_broadcastReceiverBluetoothService);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        _intentService.onActivityResult(requestCode, resultCode, data);
    }
}
