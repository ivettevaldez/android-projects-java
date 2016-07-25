package com.silvia_valdez.multibleconnectionapp.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

/**
 * DTO for BluetoothGatt devices.
 * Created by silvia.valdez@hunabsys.com on 7/20/16.
 */
public class BluetoothGattDto {
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic mBluetoothCharacteristic;

    /**
     * Constructor fot the DTO.
     *
     * @param gatt           The BLE connected device.
     * @param characteristic A characteristic read.
     */
    public BluetoothGattDto(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        this.mBluetoothGatt = gatt;
        this.mBluetoothCharacteristic = characteristic;
    }

    public BluetoothGatt getBluetoothGatt() {
        return mBluetoothGatt;
    }

    public BluetoothGattCharacteristic getBluetoothCharacteristic() {
        return mBluetoothCharacteristic;
    }
}
