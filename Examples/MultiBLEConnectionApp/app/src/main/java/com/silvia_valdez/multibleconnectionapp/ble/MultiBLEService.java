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
public class MultiBLEService implements BluetoothAdapter.LeScanCallback {

    private static final String TAG = MultiBLEService.class.getSimpleName();

    private Context mContext;
    private Activity mActivity;

    // BLE Components
    private BluetoothAdapter mBluetoothAdapter;

    private MultiBLECallback mMultiBleCallback;
    private MultiBLEHandler mMultiBleHandler;

    private ArrayList<BluetoothGatt> mConnectedGatts;
    private ArrayList<BluetoothDevice> mUBandSelectedDevices;
    private SparseArray<BluetoothDevice> mBluetoothDevices;

    /**
     * Start scan convenient callback class.
     */
    public abstract class StartScanCallback implements Runnable {
        @Override
        public void run() {
            stopScan();
            callback();
        }

        public abstract void callback();
    }

    /**
     * MultiBLEService context's constructor.
     *
     * @param context the context.
     */
    public MultiBLEService(Context context) {
        this.mContext = context;
        this.mActivity = (Activity) context;
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

    /**
     * Connect to devices.
     *
     * @param devices the array list containing the ble devices.
     */
    public void connectToDevices(ArrayList<BluetoothDevice> devices) {
        mUBandSelectedDevices = devices;
        for (BluetoothDevice device : devices) connectToDevice(device);
    }

    /**
     * Connect to device.
     */
    public void connectToDevice(BluetoothDevice device) {
        if (null != device) {
            Log.e(TAG, String.format("Connecting to %s %s...",
                    device.getName(), device.getAddress()));

            mConnectedGatts.add(device.connectGatt(mContext, false, mMultiBleCallback));
            mMultiBleHandler.sendMessage(Message.obtain(null, IMessageType.PROGRESS,
                    String.format("Connecting to %s %s...",
                            device.getName(), device.getAddress())));
        }
    }

    /**
     * Disconnect from devices.
     */
    public void disconnectFromDevices() {
        if (!mConnectedGatts.isEmpty()) {
            for (BluetoothGatt gatt : mConnectedGatts) gatt.disconnect();
            mConnectedGatts.clear();
        }
    }

    /**
     * Init setup and start bluetooth connection.
     */
    public void initBluetoothConnection() {
        setupBluetoothConnection();
        startScan();
    }

    /**
     * Setup bluetooth connection.
     */
    public void setupBluetoothConnection() {
        ProgressDialog messageNotifier = new ProgressDialog(mContext);
        mBluetoothDevices = new SparseArray<>();
        mMultiBleHandler = new MultiBLEHandler(messageNotifier);
        mMultiBleCallback = new MultiBLECallback(mMultiBleHandler);
        mBluetoothAdapter = ((BluetoothManager)
                mContext.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
    }

    /**
     * Start scan, followed by stop scan and connect to default device.
     */
    public void startScan() {
        startScan(new StartScanCallback() {
            @Override
            public void run() {
                stopScanAndConnect();
            }

            @Override
            public void callback() {

            }
        });
    }

    /**
     * Start scan with a callback at the end.
     *
     * @param callback the callback.
     */
    public void startScan(Runnable callback) {
        Log.e(TAG, mContext.getString(R.string.action_scanning_devices));
        getBluetoothDevices().clear();
        getBluetoothAdapter().startLeScan(this);
        mActivity.setProgressBarIndeterminateVisibility(true);
        mMultiBleHandler.postDelayed(callback, 3000L);
    }

    private void stopScanAndConnect() {
        stopScan();
        connectToDevices(mUBandSelectedDevices);
    }

    /**
     * Stop scan.
     */
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

    public ArrayList<BluetoothDevice> getUBandDevices() {
        return mUBandSelectedDevices;
    }

}
