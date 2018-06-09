package com.closechat.closechat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.SortedSet;
import java.util.TreeSet;

public class FriendsNearby {
    public static final String TAG = "BT";
    public static final String DELIM = ">=<";
    public static final String MARKER = "CLOSECHAT";

    private SortedSet<Friend> friendsNearby = new TreeSet<>();
    private volatile boolean discoveryFinished;

    private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                if (deviceName != null && deviceName.startsWith(MARKER + DELIM)) {
                    String deviceHardwareAddress = device.getAddress(); // MAC address
                    String text = deviceName + ":" + deviceHardwareAddress;
                    String[] split = deviceName.split(DELIM);
                    String name = split[1];
                    String avatarUrl = split[2];
                    Friend friend = new Friend(name, avatarUrl);
                    friendsNearby.add(friend);
                    Log.e(TAG, "DISCOVERED: " + friend + " : " + device.getAddress());
                }
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                discoveryFinished = true;
            }
        }
    };

    public void setup(String name, String avatarUrl, Activity activity) {
        mBluetoothAdapter.setName(MARKER + DELIM + name + DELIM + avatarUrl);

        // Permissions
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }

        // Make discoverable
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
        activity.startActivity(discoverableIntent);

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        activity.registerReceiver(mReceiver, filter);
    }

    public void teardown(Activity activity) {
        activity.unregisterReceiver(mReceiver);
    }

    /**
     * Blocking call, use it within a separate thread
     * Usually lasts ~12s
     * @return Friends discovered nearby
     */
    public SortedSet<Friend> discoverFriends() {
        if (mBluetoothAdapter.isDiscovering())
            mBluetoothAdapter.cancelDiscovery();

        friendsNearby.clear();
        discoveryFinished = false;
        mBluetoothAdapter.startDiscovery();
        while (!discoveryFinished) {
            //wait
        }
        return friendsNearby;
    }
}
