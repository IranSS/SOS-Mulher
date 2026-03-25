package com.example.sos_mulher.service;

import android.app.Service;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;


import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.sos_mulher.R;

public class SosService extends Service {

    private static final String CHANNEL_ID = "SOS_CHANNEL";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        criarCanalNotificacao();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("SOS ativo")
                .setContentText("Enviando alerta de emergência")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        startForeground(1, notification);

        return START_STICKY;
    }

    private void criarCanalNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Canal SOS",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager =
                    getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}