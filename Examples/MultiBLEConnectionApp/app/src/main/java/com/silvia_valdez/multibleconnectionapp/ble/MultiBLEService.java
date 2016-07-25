package com.silvia_valdez.multibleconnectionapp.ble;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

import com.silvia_valdez.multibleconnectionapp.R;

import java.util.ArrayList;

/**
 * BLE Service Reference Implementation, with BLE Scan
 * and Data Receiver functionality for multiple devices.
 * Created by silvia.valdez@hunabsys.com on 20/07/16.
 */
public class MultiBLEService implements BluetoothAdapter.LeScanCallback,
        IMultiBLEAccelDataReceiverDelegate {

    private static final String TAG = MultiBLEService.class.getSimpleName();

    private Context mContext;
    private Activity mActivity;
    private IMultiBLEAccelServiceDelegate mDelegate;

    // BLE Components
    private BluetoothAdapter mBluetoothAdapter;
    private MultiBLECallback mMultiBleCallback;
    private MultiBLEHandler mMultiBleHandler;

    private ArrayList<BluetoothGatt> mConnectedGatts;
    private ArrayList<BluetoothDevice> mSelectedDevices;
    private SparseArray<BluetoothDevice> mBluetoothDevices;


    // MultiBLEService context's constructor.
    public MultiBLEService(Context context) {
        this.mContext = context;
        this.mActivity = (Activity) context;
        this.mDelegate = (IMultiBLEAccelServiceDelegate) context;
        this.mConnectedGatts = new ArrayList<>();
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (device != null) {
            if (device.getName() != null) {
                Log.e(TAG, mContext.getString(R.string.action_new_ble_device)
                        + device.getName() + " @ " + rssi);
                getBluetoothDevices().put(device.hashCode(), device);
            } else {
                Log.e(TAG, mContext.getString(R.string.error_not_valid_device) + device.getName());
            }
        } else {
            Log.e(TAG, mContext.getString(R.string.error_null_device));
        }
    }

    // Method to connect to devices.
    public void connectToDevices(ArrayList<BluetoothDevice> devices) {
        mSelectedDevices = devices;
        for (BluetoothDevice device : devices) connectToDevice(device);
        mDelegate.updateConnectedDevices(mConnectedGatts);
    }

    // Method to connect to a specific device.
    public void connectToDevice(BluetoothDevice device) {
        if (null != device) {
            Log.e(TAG, String.format("Connecting to %s %s...",
                    device.getName(), device.getAddress()));

            mConnectedGatts.add(device.connectGatt(mContext, false, mMultiBleCallback));
            mMultiBleHandler.sendMessage(Message.obtain(null, IMultiBLEMessageType.PROGRESS,
                    String.format("Connecting to %s %s...",
                            device.getName(), device.getAddress())));
        }
    }

    // Disconnect from all devices.
    public void disconnectFromDevices() {
        if (!mConnectedGatts.isEmpty()) {
            for (BluetoothGatt gatt : mConnectedGatts) gatt.disconnect();
            mConnectedGatts.clear();
        }
    }

    // Setup bluetooth connection.
    public void setupBluetoothConnection() {
        ProgressDialog messageNotifier = new ProgressDialog(mContext);
        mBluetoothDevices = new SparseArray<>();
        mMultiBleHandler = new MultiBLEHandler(messageNotifier, this);
        mMultiBleCallback = new MultiBLECallback(mMultiBleHandler);
        mBluetoothAdapter = ((BluetoothManager)
                mContext.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
    }

    // Start scan with a callback at the end.
    public void startScan(Runnable callback) {
        Log.e(TAG, mContext.getString(R.string.action_scanning_devices));
        getBluetoothDevices().clear();
        getBluetoothAdapter().startLeScan(this);
        mActivity.setProgressBarIndeterminateVisibility(true);
        mMultiBleHandler.postDelayed(callback, 3000L);
    }

    // Stop scan.
    public void stopScan() {
        getBluetoothAdapter().stopLeScan(this);
        mActivity.setProgressBarIndeterminateVisibility(false);
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public SparseArray<BluetoothDevice> getBluetoothDevices() {
        return mBluetoothDevices;
    }

    public ArrayList<BluetoothDevice> getSelectedDevices() {
        return mSelectedDevices;
    }


    @Override
    public void updateAccelerometer(BluetoothGatt gatt, int accelX, int accelY, int accelZ,
                                    int gyroX, int gyroY, int gyroZ) {
        if (mDelegate != null) {
            mDelegate.updateAccelerometerValues(gatt, accelX, accelY, accelZ, gyroX, gyroY, gyroZ);
        }
    }

}
