package com.silvia_valdez.multibleconnectionapp.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Custom implementation of the Bluetooth GATT Callbacks for connection with multiple devices.
 * Created by silvia.valdez@hunabsys.com on 16/07/16.
 */
public class MultiBLECallback extends BluetoothGattCallback {

    private static final String TAG = MultiBLECallback.class.getSimpleName();

    // State Machine Tracking
    private Handler mHandler;
    private int mBleServiceId;
    private List<Integer> mBleSensors;
    private SparseArray<Integer> mCurrentSensors;


    // Multi BLE Callback public constructor.
    public MultiBLECallback(Handler handler) {
        mHandler = handler;
        mCurrentSensors = new SparseArray<>();

        // Array containing the device's available sensor(s) to read. In our case, only accelerometer.
        mBleSensors = new ArrayList<>();
        mBleSensors.add(IMultiBLEMessageType.ACCELEROMETER_SERVICE);
        // The ID of the first sensor.
        mBleServiceId = mBleSensors.get(0);
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        Log.e(TAG, String.format("Connection State Change: %d -> %s",
                status, connectionState(newState)));
        if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                /*
                 * Once successfully connected, we must next discover all the services on the
                 * device before we can read and write their characteristics.
                 */
            gatt.discoverServices();
            mHandler.sendMessage(Message.obtain(null,
                    IMultiBLEMessageType.PROGRESS,
                    "Discovering Services..."));
        } else if (status == BluetoothGatt.GATT_SUCCESS
                && newState == BluetoothProfile.STATE_DISCONNECTED) {
                /*
                 * If at any point we disconnect, send a message to clear the weather values
                 * out of the UI
                 */
            mHandler.sendEmptyMessage(IMultiBLEMessageType.CLEAR);
        } else if (status != BluetoothGatt.GATT_SUCCESS) {
                /*
                 * If there is a failure at any stage, simply disconnect
                 */
            gatt.disconnect();
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        Log.d(TAG, "Services Discovered: " + status);
        mHandler.sendMessage(Message.obtain(null, IMultiBLEMessageType.PROGRESS, "Enabling Sensors..."));
            /*
             * With services discovered, we are going to bleServiceReset our state machine
             * and start working through the sensors we need to enable
             */
        bleServiceReset();
        enableNextSensor(gatt);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt,
                                     BluetoothGattCharacteristic characteristic,
                                     int status) {
        // Nothing to do here
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt,
                                      BluetoothGattCharacteristic characteristic,
                                      int status) {
        // After writing the enable flag, next we read the initial value
        setNotifyNextSensor(gatt);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt,
                                        BluetoothGattCharacteristic characteristic) {
        /*
         * After notifications are enabled, all updates from the device on characteristic value
         * changes will be posted here. We hand these up to the UI thread to update the display.
         */
        BluetoothGattDto bluetoothGattDto = new BluetoothGattDto(gatt, characteristic);

        if (isAccelerometerChar(characteristic.getUuid())) {
            mHandler.sendMessage(Message.obtain(null,
                    IMultiBLEMessageType.ACCELEROMETER_MESSAGE, bluetoothGattDto));
        }
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt,
                                  BluetoothGattDescriptor descriptor,
                                  int status) {
        // Once notifications are enabled, we move to the next sensor and start over with enable
        bleNextService(gatt);
        enableNextSensor(gatt);
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        Log.d(TAG, "Remote RSSI: " + rssi);
    }

    /*
     * Send an enable command to each sensor by writing a configuration characteristic.
     * This is specific to the SensorTag to keep power low by disabling sensors you aren't using.
     */
    private void enableNextSensor(BluetoothGatt gatt) {
        BluetoothGattCharacteristic characteristic;
        switch (mBleServiceId) {
            case IMultiBLEMessageType.ACCELEROMETER_SERVICE:
                Log.e(TAG, "Enabling accelerometer service...");
                characteristic = gatt.getService(IMultiBLEMessageType.ACCEL_SERVICE)
                        .getCharacteristic(IMultiBLEMessageType.ACCEL_CONFIG_CHAR);
                characteristic.setValue(new byte[]{0x01});
                break;

            default:
                mHandler.sendEmptyMessage(IMultiBLEMessageType.DISMISS);
                Log.e(TAG, String.format("All Sensors Enabled for %s!", gatt.getDevice()));
                return;
        }
        gatt.writeCharacteristic(characteristic);
    }

    /*
     * Enable notification of changes on the data characteristic for each sensor by writing
     * the ENABLE_NOTIFICATION_VALUE flag to that characteristic's configuration descriptor.
     */
    private void setNotifyNextSensor(BluetoothGatt gatt) {
        BluetoothGattCharacteristic characteristic;
        switch (mBleServiceId) {
            case IMultiBLEMessageType.ACCELEROMETER_SERVICE:
                Log.e(TAG, "Set notify accelerometer sensor.");
                characteristic = gatt.getService(IMultiBLEMessageType.ACCEL_SERVICE)
                        .getCharacteristic(IMultiBLEMessageType.ACCEL_DATA_CHAR);
                break;

            default:
                mHandler.sendEmptyMessage(IMultiBLEMessageType.DISMISS);
                Log.e(TAG, String.format("All Sensors Notified for %s!", gatt.getDevice()));
                return;
        }

        // Enable local notifications
        gatt.setCharacteristicNotification(characteristic, true);

        // Enabled remote notifications
        BluetoothGattDescriptor descriptor =
                characteristic.getDescriptor(IMultiBLEMessageType.CONFIG_DESCRIPTOR);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        gatt.writeDescriptor(descriptor);
    }

    private String connectionState(int status) {
        switch (status) {
            case BluetoothProfile.STATE_CONNECTED:
                return "Connected";
            case BluetoothProfile.STATE_DISCONNECTED:
                return "Disconnected";
            case BluetoothProfile.STATE_CONNECTING:
                return "Connecting...";
            case BluetoothProfile.STATE_DISCONNECTING:
                return "Disconnecting...";
            default:
                return String.valueOf(status);
        }
    }

    private void bleServiceReset() {
        // Clear the list of read sensors and start again at the first one
        mCurrentSensors.clear();
        mBleServiceId = mBleSensors.get(0);
    }

    /*
     * For each connected device, save a list of all the sensors activated
     */
    private void bleNextService(BluetoothGatt gatt) {
        // Counter that indicates the sensor's position in our list of to-read sensors
        Integer currentSensor;
        // The hash code of the current device
        int gattCode = gatt.hashCode();

        if ((currentSensor = mCurrentSensors.get(gattCode)) != null) {
            // If the device is already in the list, increases the counter of it's read sensors
            mCurrentSensors.put(gattCode, ++currentSensor);
        } else {
            // If the device isn't in the list, initializes it's values
            currentSensor = 0;
            mCurrentSensors.put(gattCode, currentSensor);
        }

        // When all the sensors in a device were read, sets an unreachable value to mBleServiceId
        if (currentSensor < mBleSensors.size()) {
            mBleServiceId = mBleSensors.get(mCurrentSensors.get(gattCode));
        } else {
            mBleServiceId = 100;
        }
    }

    private boolean isAccelerometerChar(UUID UuidChar) {
        return UuidChar.toString().equals(IMultiBLEMessageType.ACCEL_DATA_CHAR.toString());
    }

}
