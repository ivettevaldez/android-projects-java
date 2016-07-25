package com.silvia_valdez.multibleconnectionapp.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.silvia_valdez.multibleconnectionapp.R;
import com.silvia_valdez.multibleconnectionapp.ble.MultiBLEService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_LOCATION_REQUEST_CODE = 1001;

    private Context mContext;
    private MultiBLEService mMultiBleService;

    private Button mButtonScan;
    private Button mButtonDisconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();
        checkForPermissions();
//        addItemsToList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Enable Bluetooth Service if it's not enabled yet.
        if (mMultiBleService.getBluetoothAdapter() == null
                || !mMultiBleService.getBluetoothAdapter().isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);
            return;
        }

        // Check for BLE Support.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            String message = getResources().getString(R.string.error_no_ble_support);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        addItemsToList();
        mMultiBleService.disconnectFromDevices();
    }

    private void initVariables() {
        mContext = MainActivity.this;
        mMultiBleService = new MultiBLEService(mContext);

        mButtonScan = (Button) findViewById(R.id.main_button_scan);
        mButtonScan.setOnClickListener(showAvailableDevices);
        mButtonDisconnect = (Button) findViewById(R.id.main_button_disconnect);
        mButtonDisconnect.setOnClickListener(disconnectFromDevices);
    }

    private void addItemsToList() {
        ListView devicesList = (ListView) findViewById(R.id.main_list_devices);
        List<Map<String, String>> data = new ArrayList<>();

        ArrayList<String> names = new ArrayList<>();
        names.add("Device 1");
        names.add("Device 2");
        names.add("Device 3");

        ArrayList<String> addresses = new ArrayList<>();
        addresses.add("00:00:00:00");
        addresses.add("00:00:00:00");
        addresses.add("00:00:00:00");

        for (int i = 0; i < names.size(); i++) {
            Map<String, String> values = new HashMap<>(2);
            values.put("name", names.get(i));
            values.put("address", addresses.get(i));
            data.add(values);
        }

        SimpleAdapter adapter = new SimpleAdapter(this,
                data,
                android.R.layout.simple_list_item_2,
                new String[]{"name", "address"},
                new int[]{android.R.id.text1, android.R.id.text2});

        devicesList.setAdapter(adapter);
    }

    private void checkForPermissions() {
        if (!hasPermissions(this)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_LOCATION_REQUEST_CODE);
            setUpBLEService();
        } else {
            setUpBLEService();
        }
    }

    public boolean hasPermissions(final Context context) {
        return ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void setUpBLEService() {
        mMultiBleService.setupBluetoothConnection();
    }


    private void showAvailableBands() {
        String title = getResources().getString(R.string.dialog_title_select_devices);
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle(title);

        // ArrayList to keep the selected devices
        final ArrayList<Integer> selectedItems = new ArrayList<>();
        ArrayList<String> devicesList = new ArrayList<>();

        // Get the list of available INR-Bands
        for (int i = 0; i < mMultiBleService.getBluetoothDevices().size(); i++) {
            BluetoothDevice device = mMultiBleService.getBluetoothDevices().valueAt(i);
            devicesList.add(device.getName() + " " + device.getAddress());
        }
        CharSequence[] devicesArray = devicesList.toArray(new CharSequence[devicesList.size()]);

        // Create alert dialog with multi-choice items
        dialogBuilder.setMultiChoiceItems(devicesArray, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            selectedItems.add(indexSelected);
                        } else if (selectedItems.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            selectedItems.remove(Integer.valueOf(indexSelected));
                        }
                    }
                }).setPositiveButton(getString(R.string.action_connect),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // Save the selected items' references
                ArrayList<BluetoothDevice> selectedDevices = new ArrayList<>();
                for (int i = 0; i < selectedItems.size(); i++) {
                    selectedDevices.add(mMultiBleService.getBluetoothDevices()
                            .valueAt(selectedItems.get(i)));
                }
                Log.i(TAG, String.format("Selected devices: %s", selectedDevices.toString()));
                // Connect with the INR-Bands
                mMultiBleService.connectToDevices(selectedDevices);
                mButtonScan.setEnabled(false);
                mButtonDisconnect.setEnabled(true);
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }).create();
        dialogBuilder.show();
    }

    /*****
     * LISTENERS.
     *****/
    private Button.OnClickListener showAvailableDevices = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String title = mContext.getResources().getString(R.string.dialog_title_inr_scan);
            String message = mContext.getResources().getString(R.string.action_scanning_devices);
            final ProgressDialog progressDialog =
                    ProgressDialog.show(MainActivity.this,
                            title,
                            message,
                            true);

            mMultiBleService.disconnectFromDevices();
            mMultiBleService.startScan(new Runnable() {
                @Override
                public void run() {
                    mMultiBleService.stopScan();
                    progressDialog.dismiss();
                    showAvailableBands();
                }
            });
        }
    };

    private Button.OnClickListener disconnectFromDevices = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMultiBleService.disconnectFromDevices();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
//                    addItemsToList();
                }
            }, 300);

            v.setEnabled(false);
            mButtonScan.setEnabled(true);
        }
    };

}
