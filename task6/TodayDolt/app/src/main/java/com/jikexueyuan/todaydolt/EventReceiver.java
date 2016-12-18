package com.jikexueyuan.todaydolt;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

/**
 * Created by dej on 2016/9/25.
 */
public class EventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher); // 小图标
        builder.setContentTitle("记事本提醒"); // 消息标题
        builder.setContentText(intent.getStringExtra("content")); // 消息内容

        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(intent.getIntExtra("alarm_id",0), notification);
    }
}
