package com.silvia_valdez.multibleconnectionapp.ble;

import android.bluetooth.BluetoothGatt;

import java.util.ArrayList;

/**
 * Accelerometer/gyroscope's Service Delegate for multiple BLE devices.
 * Created by silvia.valdez@hunabsys.com on 7/20/16.
 */
public interface IMultiBLEAccelServiceDelegate {

    /**
     * Method to update the view with the current connected devices.
     *
     * @param gatts ArrayList with the current connected devices.
     */
    void updateConnectedDevices(ArrayList<BluetoothGatt> gatts);

    /**
     * Method to process each data received from the accelerometer/gyroscope sensors.
     *
     * @param gatt   the device which sends the message.
     * @param accelX value received for the accelerometer´s x axis.
     * @param accelY value received for the accelerometer´s y axis.
     * @param accelZ value received for the accelerometer´s z axis.
     * @param gyroX  value received for the gyroscope´s x axis.
     * @param gyroY  value received for the gyroscope´s y axis.
     * @param gyroZ  value received for the gyroscope´s z axis.
     */
    void updateAccelerometerValues(BluetoothGatt gatt, int accelX, int accelY, int accelZ,
                                   int gyroX, int gyroY, int gyroZ);

}
