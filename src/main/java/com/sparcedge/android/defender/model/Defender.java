package com.sparcedge.android.defender.model;

import android.bluetooth.BluetoothDevice;

import java.io.Serializable;

/**
 * User: Dayel Ostraco
 * Date: 12/17/12
 * Time: 4:06 PM
 */
public class Defender implements Serializable {

    private String name;
    private String macAddress;
    private BluetoothDevice device;

    public Defender(String name, String macAddress, BluetoothDevice device) {
        this.name = name;
        this.macAddress = macAddress;
        this.device = device;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }
}
