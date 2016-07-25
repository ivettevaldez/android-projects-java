package com.silvia_valdez.multibleconnectionapp.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.silvia_valdez.multibleconnectionapp.R;
import com.silvia_valdez.multibleconnectionapp.ble.IMultiBLEAccelServiceDelegate;
import com.silvia_valdez.multibleconnectionapp.ble.MultiBLEService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements IMultiBLEAccelServiceDelegate {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_LOCATION_REQUEST_CODE = 1001;

    private Context mContext;
    private MultiBLEService mMultiBleService;
    private List<Map<String, String>> mDevicesData;

    private ListView mDevicesListView;
    private TextView mTextStatus;
    private Button mButtonScan;
    private Button mButtonDisconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();
        checkForPermissions();
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
        mMultiBleService.disconnectFromDevices();
    }

    private void initVariables() {
        mContext = MainActivity.this;
        mMultiBleService = new MultiBLEService(mContext);

        mTextStatus = (TextView) findViewById(R.id.main_text_status);
        mButtonScan = (Button) findViewById(R.id.main_button_scan);
        mButtonScan.setOnClickListener(showAvailableDevices);
        mButtonDisconnect = (Button) findViewById(R.id.main_button_disconnect);
        mButtonDisconnect.setOnClickListener(disconnectFromDevices);
    }

    /*
     * ListView with Item showing the device's name and mac address,
     * and SubItem showing the accelerometer's x, y and z values.
     * Parameter gatts is the list containing the connected devices.
     */
    private void addItemsToList(ArrayList<BluetoothGatt> gatts) {
        mDevicesListView = (ListView) findViewById(R.id.main_list_devices);
        mDevicesData = new ArrayList<>();

        for (int i = 0; i < gatts.size(); i++) {
            Map<String, String> values = new HashMap<>(2);
            values.put("name", String.format("%s - %s",
                    gatts.get(i).getDevice().getName(), gatts.get(i).getDevice().getAddress()));
            values.put("accelerometer", " ");   // Empty value until it's sensor is receiving data
            mDevicesData.add(values);
        }

        SimpleAdapter adapter = new SimpleAdapter(this,
                mDevicesData,
                android.R.layout.simple_list_item_2,
                new String[]{"name", "accelerometer"},
                new int[]{android.R.id.text1, android.R.id.text2});

        mDevicesListView.setAdapter(adapter);
    }

    private void checkForPermissions() {
        if (!hasPermissions(this)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_LOCATION_REQUEST_CODE);
            mMultiBleService.setupBluetoothConnection();
        } else {
            mMultiBleService.setupBluetoothConnection();
        }
    }

    public boolean hasPermissions(final Context context) {
        return ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void showAvailableBleDevices() {
        String title = getResources().getString(R.string.dialog_title_select_devices);
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle(title);

        // ArrayList to keep the selected devices
        final ArrayList<Integer> selectedItems = new ArrayList<>();
        ArrayList<String> devicesList = new ArrayList<>();

        // Get the list of available devices
        for (int i = 0; i < mMultiBleService.getBluetoothDevices().size(); i++) {
            BluetoothDevice device = mMultiBleService.getBluetoothDevices().valueAt(i);
            devicesList.add(String.format("%s %s", device.getName(), device.getAddress()));
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

                        // Connect with the devices
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
                    showAvailableBleDevices();
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
                    // Wait 200 milliseconds until accelerometer's is not sending more data
                    mDevicesData.clear();
                    ((BaseAdapter) mDevicesListView.getAdapter()).notifyDataSetChanged();
                }
            }, 200);

            mButtonDisconnect.setEnabled(false);
            mButtonScan.setEnabled(true);
            mTextStatus.setText(getString(R.string.no_connected_devices));
            mTextStatus.setTextColor(getResources().getColor(R.color.red));
        }
    };

    @Override
    public void updateAccelerometerValues(BluetoothGatt gatt, int accelX, int accelY, int accelZ,
                                          int gyroX, int gyroY, int gyroZ) {
        Log.d(TAG, String.format("DEVICE: %s ACCELEROMETER: X: %d Y: %d Z: %d",
                gatt.getDevice(), accelX, accelY, accelZ));

        Log.d(TAG, String.format("DEVICE: %s GYROSCOPE: X: %d Y: %d Z: %d",
                gatt.getDevice(), gyroX, gyroY, gyroZ));

        if (mMultiBleService.getSelectedDevices() != null) {
            // Get the position of the device in the connected devices' list
            int position = mMultiBleService.getSelectedDevices().indexOf(gatt.getDevice());

            // Update the accelerometer's value in the device's data and notify the listView adapter.
            mDevicesData.get(position).put("accelerometer", String.format(Locale.getDefault(),
                    "Accel. values: %d, %d, %d", accelX, accelY, accelZ));
            ((BaseAdapter) mDevicesListView.getAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public void updateConnectedDevices(ArrayList<BluetoothGatt> gatts) {
        // Update the number of connected devices
        String message;
        if (gatts.size() == 1) {
            message = getString(R.string.connected_device);
        } else {
            message = getString(R.string.connected_devices);
        }

        mTextStatus.setText(String.format(Locale.getDefault(),
                "%d %s", gatts.size(), message));
        mTextStatus.setTextColor(getResources().getColor(R.color.green));

        // Add the devices to the listView
        addItemsToList(gatts);
    }
}
