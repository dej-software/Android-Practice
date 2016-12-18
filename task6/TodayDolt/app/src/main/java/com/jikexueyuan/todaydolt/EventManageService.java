package com.jikexueyuan.todaydolt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class EventManageService extends Service {

    // 关于闹钟定时的一些对象声明
    private AlarmManager manager;
    private Intent intent;
    private PendingIntent pendingIntent;
    private List<Integer> list = new ArrayList<>();

    public EventManageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new EventBinder();
    }

    public class EventBinder extends android.os.Binder {

        public void addEvent(String data) {

        }

        public void deleteEvent(String data) {

        }

        public EventManageService getService() {
            return EventManageService.this;
        }
    }

    /**
     * 设置一个闹钟事件
     * @param requestCode
     * @param time
     * @param content
     */
    private void setAlarm(int requestCode, String time, String content) {
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // 设置开启EventReceiver这个BroadcastReceiver
        intent = new Intent(this, EventReceiver.class);
        intent.putExtra("alarm_id", requestCode);
        intent.putExtra("content", content);
        pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);

        // 计算时间差
//        long firstTime = SystemClock.elapsedRealtime(); // 开机后到现在的时间值
        long systemTime = System.currentTimeMillis(); // 自1970-1-1 00:00:00.000 到当前时刻的时间值

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time));
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long selectTime = calendar.getTimeInMillis(); // 设置要闹铃的时间值
        if (systemTime > selectTime) {
            // 如果当前时间大于设置的时间，加一天设定时间
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
        System.out.println("selectTime:" + format.format(new Date(selectTime)));
        System.out.println("currentTime:" + format.format(new Date(systemTime)));


        // 设置闹铃时间 开机时间+时间间隔
//        firstTime += (selectTime - systemTime);

        // RTC_WAKEUP 硬件闹钟，当闹钟发出时唤醒手机休眠 （RTC可修改手机时间触发）
//        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, selectTime, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /**
     * 删除一个闹钟事件
     * @param requestCode
     */
    private void delAlarm(int requestCode) {
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // 设置开启EventReceiver这个BroadcastReceiver
        intent = new Intent(this, EventReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);

        manager.cancel(pendingIntent);
    }

    /**
     * 更新闹钟事件
     */
    private void updateAlarm() {
        Cursor cursor = getContentResolver().query(EventProvider.URI, null, null, null, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            System.out.println(String.format("updateAlarm: id = %d, time = %s, content = %s", id, time, content));

            setAlarm(id, time, content);
            list.add(id);

            cursor.moveToNext();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("Service onCreate");
        // 注册一个观察EventProvider变化的对象EventObserver
        getContentResolver().registerContentObserver(EventProvider.URI, true, new EventObserver(new Handler()));

        updateAlarm();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service onDestroy");
    }

    /**
     * 用来观察EventProvider的变化并处理
     */
    private final class EventObserver extends ContentObserver {
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public EventObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            System.out.println("EventObserver onChange");

            // 清除所有闹钟事件
            for (int i = 0; i < list.size(); i++) {
                delAlarm(list.get(i));
            }
            list.clear();

            updateAlarm();
        }
    }
}
