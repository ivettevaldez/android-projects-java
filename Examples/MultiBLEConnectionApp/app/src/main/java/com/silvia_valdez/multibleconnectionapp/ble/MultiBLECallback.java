package com.silvia_valdez.multibleconnectionapp.ble;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

import java.util.List;

/**
 * Custom INR-Band implementation of the Bluetooth GATT Callbacks
 * for connection with multiple devices.
 * Created by silvia.valdez@hunabsys.com on 16/07/16.
 */
@SuppressLint("NewApi")
public class MultiBLECallback extends BluetoothGattCallback {

    private static final String TAG = MultiBLECallback.class.getSimpleName();

    /**
     * State Machine Tracking
     */
    private int mBleServiceId;

    private SparseArray<Integer> mCurrents;

    private List<Integer> mBleSensors;
    private Handler mHandler;


    /**
     * Multi BLE Callback public constructor.
     *
     * @param handler the handler.
     */
    public MultiBLECallback(Handler handler) {
        mCurrents = new SparseArray<>();
        mHandler = handler;

//        mBleSensors = new ArrayList<>();
//        // TODO: mBleSensors.add(BLEServiceType.MAGNETOMETER_SERVICE);
//        mBleSensors.add(BLEServiceType.GALVANIC_SERVICE);
//        mBleSensors.add(BLEServiceType.ACCELEROMETER_SERVICE);
//        mBleSensors.add(BLEServiceType.BATTERY_SERVICE);
//        mBleSensors.add(BLEServiceType.PULSE_SERVICE);
//        mBleSensors.add(BLEServiceType.TEMPERATURE_SERVICE);
//
//        mBleServiceId = mBleSensors.get(0);
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        Log.e(TAG, "Connection State Change: " + status + " -> " + connectionState(newState));
        if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                /*
                 * Once successfully connected, we must next discover all the services on the
                 * device before we can read and write their characteristics.
                 */
            gatt.discoverServices();
            mHandler.sendMessage(Message.obtain(
                    null, IMessageType.PROGRESS, "Discovering Services..."));
        } else if (status == BluetoothGatt.GATT_SUCCESS
                && newState == BluetoothProfile.STATE_DISCONNECTED) {
                /*
                 * If at any point we disconnect, send a message to clear the weather values
                 * out of the UI
                 */
            mHandler.sendEmptyMessage(IMessageType.CLEAR);
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
        mHandler.sendMessage(Message.obtain(null, IMessageType.PROGRESS, "Enabling Sensors..."));
            /*
             * With services discovered, we are going to bleServiceReset our state machine and start
             * working through the sensors we need to enable
             */
        bleServiceReset();
//        enableNextSensor(gatt);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt,
                                     BluetoothGattCharacteristic characteristic,
                                     int status) {
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt,
                                      BluetoothGattCharacteristic characteristic,
                                      int status) {
        // After writing the enable flag, next we read the initial value
        // readNextSensor(gatt);
//        setNotifyNextSensor(gatt);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt,
                                        BluetoothGattCharacteristic characteristic) {
        /*
         * After notifications are enabled, all updates from the device on characteristic
         * value changes will be posted here.  Similar to read, we hand these up to the
         * UI thread to update the display.
         */
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt,
                                  BluetoothGattDescriptor descriptor,
                                  int status) {
        // Once notifications are enabled, we move to the next sensor and start over with enable
        bleNextService(gatt);
//        enableNextSensor(gatt);
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        Log.d(TAG, "Remote RSSI: " + rssi);
    }
//
//    /**
//     * Send an enable command to each sensor by writing a configuration
//     * characteristic.  This is specific to the SensorTag to keep power
//     * low by disabling sensors you aren't using.
//     */
//    private void enableNextSensor(BluetoothGatt gatt) {
//        BluetoothGattCharacteristic characteristic;
//
//        switch (mBleServiceId) {
//            case BLEServiceType.GALVANIC_SERVICE:
//                Log.e(TAG, "Enabling galvanic service...");
//                characteristic = gatt.getService(SensorMessageType.GALVANIC_SERVICE)
//                        .getCharacteristic(SensorMessageType.GALVANIC_CONF_CHAR);
//                characteristic.setValue(new byte[]{0x01});
//                break;
//
////            case BLEServiceType.MAGNETOMETER_SERVICE:
////                Log.e(TAG, "Enabling magnetometer service");
////                characteristic = gatt.getService(SensorMessageType.MAGNETOMETER_SERVICE)
////                      .getCharacteristic(SensorMessageType.MAGNETOMETER_CONFIG_CHAR);
////                characteristic.setValue(new byte[]{0x01});
////                break;
//
//            case BLEServiceType.PULSE_SERVICE:
//                Log.e(TAG, "Enabling pulse service...");
//                characteristic = gatt.getService(SensorMessageType.PULSE_SERVICE)
//                        .getCharacteristic(SensorMessageType.PULSE_CONFIG_CHAR);
//                characteristic.setValue(new byte[]{0x01});
//                break;
//
//            case BLEServiceType.TEMPERATURE_SERVICE:
//                Log.e(TAG, "Enabling temperature service...");
//                characteristic = gatt.getService(SensorMessageType.TEMP_SERVICE)
//                        .getCharacteristic(SensorMessageType.TEMP_CONFIG_CHAR);
//                characteristic.setValue(new byte[]{0x01});
//                break;
//
//            case BLEServiceType.BATTERY_SERVICE:
//                Log.e(TAG, "Enabling battery service...");
//                characteristic = gatt.getService(SensorMessageType.BATT_SERVICE)
//                        .getCharacteristic(SensorMessageType.BATT_CONFIG_CHAR);
//                characteristic.setValue(new byte[]{0x01});
//                break;
//
//            case BLEServiceType.ACCELEROMETER_SERVICE:
//                Log.e(TAG, "Enabling accelerometer service...");
//                characteristic = gatt.getService(SensorMessageType.ACEL_SERVICE)
//                        .getCharacteristic(SensorMessageType.ACEL_CONFIG_CHAR);
//                characteristic.setValue(new byte[]{0x01});
//                break;
//
//            case BLEServiceType.ACCELEROMETER_FREQ_SERVICE:
//                Log.e(TAG, "Enabling freq. accelerometer service...");
//                characteristic = gatt.getService(SensorMessageType.ACEL_SERVICE)
//                        .getCharacteristic(SensorMessageType.ACEL_CONFIG_PERIOD);
//                characteristic.setValue(new byte[]{0x0A});
//                break;
//
//            default:
//                mHandler.sendEmptyMessage(IMessageType.DISMISS);
//                Log.e(TAG, String.format("All Sensors Enabled for %s!", gatt.getDevice()));
//                return;
//        }
//
//        gatt.writeCharacteristic(characteristic);
//    }
//
//    /**
//     * Read the data characteristic's value for each sensor explicitly
//     */
//    private void readNextSensor(BluetoothGatt gatt) {
//        BluetoothGattCharacteristic characteristic = null;
//
//        switch (mBleServiceId) {
//            case BLEServiceType.TEMPERATURE_SERVICE:
//                Log.e(TAG, "Reading temperature data");
//                characteristic = gatt.getService(SensorMessageType.TEMP_SERVICE)
//                        .getCharacteristic(SensorMessageType.TEMP_CONFIG_CHAR);
//                break;
//
////            case BLEServiceType.MAGNETOMETER_SERVICE:
////                Log.e(TAG, "Reading magnetometer data");
////                characteristic = gatt.getService(SensorMessageType.MAGNETOMETER_SERVICE)
////                      .getCharacteristic(SensorMessageType.MAGNETOMETER_CONFIG_CHAR);
////                break;
//
//            case BLEServiceType.PULSE_SERVICE:
//                Log.e(TAG, "Reading pulse data");
//                characteristic = gatt.getService(SensorMessageType.PULSE_SERVICE)
//                        .getCharacteristic(SensorMessageType.PULSE_CONFIG_CHAR);
//                break;
//
//            case BLEServiceType.GALVANIC_SERVICE:
//                Log.e(TAG, "Reading galvanic data");
//                characteristic = gatt.getService(SensorMessageType.GALVANIC_SERVICE)
//                        .getCharacteristic(SensorMessageType.GALVANIC_CONF_CHAR);
//                break;
//
//            case BLEServiceType.BATTERY_SERVICE:
//                Log.e(TAG, "Reading battery data");
//                characteristic = gatt.getService(SensorMessageType.BATT_SERVICE)
//                        .getCharacteristic(SensorMessageType.BATT_CONFIG_CHAR);
//                break;
//
//            case BLEServiceType.ACCELEROMETER_SERVICE:
//                Log.e(TAG, "Reading acel. data");
//                characteristic = gatt.getService(SensorMessageType.ACEL_SERVICE)
//                        .getCharacteristic(SensorMessageType.ACEL_CONFIG_CHAR);
//                break;
//
//            case BLEServiceType.ACCELEROMETER_FREQ_SERVICE:
//                break;
//
//            default:
//                mHandler.sendEmptyMessage(IMessageType.DISMISS);
//                Log.e(TAG, "All Sensors readed");
//                return;
//        }
//
//        gatt.readCharacteristic(characteristic);
//    }
//
//    /**
//     * Enable notification of changes on the data characteristic for each sensor
//     * by writing the ENABLE_NOTIFICATION_VALUE flag to that characteristic's
//     * configuration descriptor.
//     */
//    private void setNotifyNextSensor(BluetoothGatt gatt) {
//        BluetoothGattCharacteristic characteristic;
//        switch (mBleServiceId) {
//            case BLEServiceType.GALVANIC_SERVICE:
//                Log.e(TAG, "Set notify galvanic sensor.");
//                characteristic = gatt.getService(SensorMessageType.GALVANIC_SERVICE)
//                        .getCharacteristic(SensorMessageType.GALVANIC_DATA_CHAR);
//                break;
//
//            case BLEServiceType.TEMPERATURE_SERVICE:
//                Log.e(TAG, "Set notify temperature sensor.");
//                characteristic = gatt.getService(SensorMessageType.TEMP_SERVICE)
//                        .getCharacteristic(SensorMessageType.TEMP_DATA_CHAR);
//                break;
//
////            case BLEServiceType.MAGNETOMETER_SERVICE:
////                Log.e(TAG, "Set notify magnetometer");
////                characteristic = gatt.getService(SensorMessageType.MAGNETOMETER_SERVICE)
////                      .getCharacteristic(SensorMessageType.MAGNETOMETER_DATA_CHAR);
////                break;
//
//            case BLEServiceType.PULSE_SERVICE:
//                Log.e(TAG, "Set notify pulse sensor.");
//                characteristic = gatt.getService(SensorMessageType.PULSE_SERVICE)
//                        .getCharacteristic(SensorMessageType.PULSE_DATA_CHAR);
//                break;
//
//            case BLEServiceType.BATTERY_SERVICE:
//                Log.e(TAG, "Set notify battery sensor.");
//                characteristic = gatt.getService(SensorMessageType.BATT_SERVICE)
//                        .getCharacteristic(SensorMessageType.BATT_DATA_CHAR);
//                break;
//
//            case BLEServiceType.ACCELEROMETER_SERVICE:
//                Log.e(TAG, "Set notify accelerometer sensor.");
//                characteristic = gatt.getService(SensorMessageType.ACEL_SERVICE)
//                        .getCharacteristic(SensorMessageType.ACEL_DATA_CHAR);
//                break;
//
//            case BLEServiceType.ACCELEROMETER_FREQ_SERVICE:
//                Log.e(TAG, "Set notify freq. accelerometer.");
//                characteristic = gatt.getService(SensorMessageType.ACEL_SERVICE)
//                        .getCharacteristic(SensorMessageType.ACEL_CONFIG_PERIOD);
//                break;
//
//            default:
//                mHandler.sendEmptyMessage(IMessageType.DISMISS);
//                Log.e(TAG, String.format("All Sensors Notified for %s!", gatt.getDevice()));
//                return;
//        }
//
//        //Enable local notifications
//        gatt.setCharacteristicNotification(characteristic, true);
//
//        //Enabled remote notifications
//        BluetoothGattDescriptor descriptor =
//                characteristic.getDescriptor(SensorMessageType.CONFIG_DESCRIPTOR);
//        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//
//        gatt.writeDescriptor(descriptor);
//    }

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
        mCurrents.clear();
        mBleServiceId = mBleSensors.get(0);
    }

    private void bleNextService(BluetoothGatt gatt) {
        Integer currentValue;
        int gattCode = gatt.hashCode();

        if ((currentValue = mCurrents.get(gattCode)) != null) {
            mCurrents.put(gattCode, ++currentValue);
        } else {
            currentValue = 0;
            mCurrents.put(gattCode, currentValue);
        }

        if (currentValue < mBleSensors.size()) {
            mBleServiceId = mBleSensors.get(mCurrents.get(gattCode));
        } else {
            mBleServiceId = 100;
        }
    }

}
