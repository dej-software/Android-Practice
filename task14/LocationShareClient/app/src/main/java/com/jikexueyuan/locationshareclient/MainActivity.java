package com.jikexueyuan.locationshareclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    private static final String TAG = "MainActivity";

    // 是否绑定了服务
    private boolean isBind = false;
    private SocketService.SocketBinder binder = null;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private EditText etServerIP, etServerPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyLocationApplication.addActivity(this);
        initView();
    }

    private void initView() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        etServerIP = (EditText) findViewById(R.id.etServerIP);
        etServerPort = (EditText) findViewById(R.id.etServerPort);

        etServerIP.setText(preferences.getString(ClientUtil.SERVER_IP, ""));
        etServerPort.setText(preferences.getString(ClientUtil.SERVER_PORT, ""));

        // 绑定服务
        Intent intentService = new Intent(MainActivity.this, SocketService.class);
        isBind = bindService(intentService, this, Context.BIND_AUTO_CREATE);

        findViewById(R.id.btnConnect).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (isBind) {
            unbindService(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnConnect) {
            String ip = etServerIP.getText().toString().trim();
            String port = etServerPort.getText().toString().trim();
            Log.d(TAG, "IP:" + ip + ",Port:" + port);
            if ("".equals(ip) || "".equals(port)) {
                Toast.makeText(MainActivity.this, "请不要输入空信息", Toast.LENGTH_SHORT).show();
                return;
            }

            int portInt = 0;
            try {
                portInt = Integer.parseInt(port);
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "请输入正确的数字", Toast.LENGTH_SHORT).show();
                return;
//                e.printStackTrace();
            }
            Log.d(TAG, "Int Port:" + portInt);

            // 把输入的数据缓存 下次启动应用时不用重复输入
            editor.putString(ClientUtil.SERVER_IP, ip);
            editor.putString(ClientUtil.SERVER_PORT, port);
            editor.commit();

            // socket 连接
            binder.connect(ip, portInt);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "onServiceConnected");
        binder = (SocketService.SocketBinder) service;
        binder.getService().setConnectCallBack(new SocketService.SocketConnectCallBack() {
            @Override
            public void onSocketMsgCallback(String msg) {
                Message message = new Message();
                Bundle b = new Bundle();
                b.putString("msg", msg);
                message.setData(b);
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "onServiceDisconnected");
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 格式：{"flag":0,"message":"@success"}
            JSONObject root = null;
            String server_msg = null;
            try {
                root = new JSONObject(msg.getData().getString("msg"));
                server_msg = root.getString(ClientUtil.JSON_MSG);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (server_msg.equals("@error")) {
                Toast.makeText(MainActivity.this, "无法建立连接，请检查网络", Toast.LENGTH_SHORT).show();
            } else if (server_msg.equals("@success")) {
                Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, UserActivity.class));
            }
        }
    };
}
