package com.example.sajithjayasekara.batterymanager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

public class BMBackgroundService extends Service {
    private BroadcastReceiver brPluggingState;
    private BroadcastReceiver brBatteryLow;
    private BroadcastReceiver brBatteryLevel;
    private boolean pluggingState;
    private int level;

    public BMBackgroundService() {
        brPluggingState = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                    pluggingState = true;
                    Toast.makeText(context, "The device is charging", Toast.LENGTH_SHORT).show();
                } else {
                    intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED);
                    pluggingState = false;

                }
            }
        };
        brBatteryLow = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                batteryLowNotification();
            }
        };

        brBatteryLevel = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                level = intent.getIntExtra("level", 0);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                registerReceiver(brPluggingState, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
                registerReceiver(brPluggingState, new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
                registerReceiver(brBatteryLow, new IntentFilter(Intent.ACTION_BATTERY_LOW));
                registerReceiver(brBatteryLevel, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(brPluggingState);
        unregisterReceiver(brBatteryLow);
        unregisterReceiver(brBatteryLevel);
//        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    public void batteryLowNotification() {

        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivities(BMBackgroundService.this, 0, new Intent[]{intent}, 0);
        Notification notification = new Notification.Builder(BMBackgroundService.this).setTicker("ticker").setContentTitle("Battery Warning").setContentText("Battery is low").setSmallIcon(R.drawable.ic_stat_name).setContentIntent(pendingIntent).getNotification();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }
}
