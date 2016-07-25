package com.silvia_valdez.multibleconnectionapp.ble;

import android.bluetooth.BluetoothGatt;

/**
 * Handler's Accelerometer Data Receiver Delegate for multiple devices.
 * Created by silvia.valdez@hunabsys.com on 7/20/16.
 */
public interface IMultiBLEAccelDataReceiverDelegate {

    /**
     * Method to process each data received from the accelerometer/gyroscope sensors.
     *
     * @param gatt   the device which sends the message.
     * @param accelX value received for the accelerometer's x axis.
     * @param accelY value received for the accelerometer's y axis.
     * @param accelZ value received for the accelerometer's z axis.
     * @param gyroX  value received for the gyroscope's x axis.
     * @param gyroY  value received for the gyroscope's y axis.
     * @param gyroZ  value received for the gyroscope's z axis.
     */
    void updateAccelerometer(BluetoothGatt gatt, int accelX, int accelY, int accelZ,
                             int gyroX, int gyroY, int gyroZ);

}
