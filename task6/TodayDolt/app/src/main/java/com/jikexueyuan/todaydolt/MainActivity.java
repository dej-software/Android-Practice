package com.jikexueyuan.todaydolt;

import android.app.Service;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    private ListView lvEvents;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
    }

    /**
     * 一些MainActivity初始化
     */
    private void initView() {
        lvEvents = (ListView) findViewById(R.id.lvEvents);

        adapter = new SimpleCursorAdapter(this,
                R.layout.event_list_cell, null,
                new String[]{"time", "content"},
                new int[]{R.id.tvTime, R.id.tvContent}, 0);
        adapter.setViewBinder(viewBinder);
        lvEvents.setAdapter(adapter);

        lvEvents.setOnItemLongClickListener(this);

        refreshListView();

        // 启动服务
        startService(new Intent(MainActivity.this, EventManageService.class));

    }

    // 从添加事件界面返回时更新ListView
    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("MainActivity onResume");
        refreshListView();
    }

    // 更新ListView
    private void refreshListView() {
        Cursor cursor = getContentResolver().query(EventProvider.URI, null, null, null, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            System.out.println(String.format("id = %d, time = %s, content = %s", id, time, content));
            cursor.moveToNext();
        }

        adapter.changeCursor(cursor);
    }

    // 新声明一个ViewBinder重写要显示到ListView的数据格式
    private SimpleCursorAdapter.ViewBinder viewBinder = new SimpleCursorAdapter.ViewBinder() {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

            if (cursor.getColumnIndex("time") == columnIndex) {
                TextView time = (TextView) view.findViewById(R.id.tvTime);
                time.setText("定时时间：" + cursor.getString(cursor.getColumnIndex("time")));
                return true;
            }

            if (cursor.getColumnIndex("content") == columnIndex) {
                TextView time = (TextView) view.findViewById(R.id.tvContent);
                time.setText("事件内容：" + cursor.getString(cursor.getColumnIndex("content")));
                return true;
            }

            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, AddEvent.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 长按ListView中Item的处理
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("系统提示")
                .setMessage("是否要删除本数据？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor c = adapter.getCursor();
                        c.moveToPosition(position);
                        int itemId = c.getInt(c.getColumnIndex("_id"));
                        getContentResolver().delete(EventProvider.URI, "_id=?", new String[]{itemId + ""});

                        Toast.makeText(MainActivity.this, R.string.toastDelete, Toast.LENGTH_SHORT).show();
                        refreshListView();
                    }
                })
                .setNegativeButton("否",null)
                .show();

        return true;
    }
}
