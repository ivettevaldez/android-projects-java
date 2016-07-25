package com.silvia_valdez.multibleconnectionapp.ble;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Custom Handler implementation to process Message and Runnable
 * of the BLE Callbacks on multiple devices.
 * Created by silvia.valdez@hunabsys.com on 7/20/16.
 */
public class MultiBLEHandler extends Handler {

    private static final String TAG = MultiBLEHandler.class.getSimpleName();

    private IMultiBLEAccelDataReceiverDelegate mDelegate;
    private ProgressDialog mProgressDialog;
    private boolean mStopCapture;

    // Public constructor of Multi BLE Handler.
    public MultiBLEHandler(ProgressDialog dialog, IMultiBLEAccelDataReceiverDelegate delegate) {
        // Delegate of the IMultiBLEAccelDataReceiverDelegate class to update the view.
        this.mDelegate = delegate;

        this.mProgressDialog = dialog;
        this.mStopCapture = false;
    }

    @Override
    public void handleMessage(Message msg) {
        BluetoothGattDto bluetoothGattDto;
        BluetoothGatt gatt = null;
        BluetoothGattCharacteristic characteristic = null;
        int subject = msg.what;

        if (subject == IMultiBLEMessageType.STOP_CAPTURE && !mStopCapture) {
            mStopCapture = true;
        } else if (subject == IMultiBLEMessageType.RESUME_CAPTURE) {
            mStopCapture = false;
        }

        if (!mStopCapture) {
            if (msg.obj instanceof BluetoothGattDto) {
                // Custom DTO to save the data of the gatt and the characteristic read
                bluetoothGattDto = (BluetoothGattDto) msg.obj;
                gatt = bluetoothGattDto.getBluetoothGatt();
                characteristic = bluetoothGattDto.getBluetoothCharacteristic();
            }

            switch (msg.what) {
                case IMultiBLEMessageType.PROGRESS:
                    mProgressDialog.setMessage((String) msg.obj);
                    if (!mProgressDialog.isShowing()) {
                        mProgressDialog.show();
                    }
                    break;

                case IMultiBLEMessageType.ACCELEROMETER_MESSAGE:
                    if (characteristic == null || characteristic.getValue() == null) {
                        Log.w(TAG, "Error obtaining accelerometer value");
                        return;
                    }
                    updateAccelerometerValue16(gatt, characteristic);
                    break;

                case IMultiBLEMessageType.DISMISS:
                    mProgressDialog.hide();
                    break;
            }
        }
    }

    /**
     * Update accelerometer value base 16, if implemented.
     *
     * @param characteristic the characteristic read.
     */
    private void updateAccelerometerValue16(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
        if (mDelegate != null) {
            int[] accelerometer = getAccelData16(characteristic);
            mDelegate.updateAccelerometer(gatt, accelerometer[0], accelerometer[1],
                    accelerometer[2], accelerometer[3], accelerometer[4], accelerometer[5]);
        }
    }

    public int[] getAccelData16(BluetoothGattCharacteristic characteristic) {
        int[] result = new int[6];
        byte[] value = characteristic.getValue();

        // Three first values for the accelerometer data
        result[0] = getIntFromByteArray(value, 0);
        result[1] = getIntFromByteArray(value, 2);
        result[2] = getIntFromByteArray(value, 4);
        // Three last values for the gyroscope data
        result[3] = getIntFromByteArray(value, 6);
        result[4] = getIntFromByteArray(value, 8);
        result[5] = getIntFromByteArray(value, 10);

        return result;
    }

    private int getIntFromByteArray(byte[] byteArray, int offset) {
        int result, high, low;

        high = byteArray[offset];
        low = byteArray[offset + 1];
        result = ((high & 0x000000FF) << 8) | (low & 0x000000FF);
        result = (result > 32767) ? result - 65536 : result;

        return result;
    }

}
