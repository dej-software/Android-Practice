package com.jikexueyuan.locationshareclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class UserActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    private static final String TAG = "UserActivity";

    // 是否绑定了服务
    private boolean isBind = false;
    private SocketService.SocketBinder binder = null;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private EditText etUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        MyLocationApplication.addActivity(this);
        initView();
    }

    private void initView() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        etUserName = (EditText) findViewById(R.id.etUserName);
        etUserName.setText(preferences.getString(ClientUtil.USER_NAME, ""));

        // 绑定服务
        Intent intentService = new Intent(UserActivity.this, SocketService.class);
        isBind = bindService(intentService, this, Context.BIND_AUTO_CREATE);

        findViewById(R.id.btnSetting).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (isBind) {
            unbindService(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSetting) {
            String name = etUserName.getText().toString().trim();
            if ("".equals(name)) {
                Toast.makeText(UserActivity.this, "请不要输入空信息", Toast.LENGTH_SHORT).show();
                return;
            }

            editor.putString(ClientUtil.USER_NAME, name);
            editor.commit();
            ClientUtil.name = name;

            JSONObject root = new JSONObject();
            try {
                root.put(ClientUtil.JSON_FLAG, ClientUtil.DATA_FLAG_USER);
                root.put(ClientUtil.JSON_NAME, name);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // 发送数据到服务器
            binder.send(root.toString());
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder = (SocketService.SocketBinder) service;
        binder.getService().setUserCallBack(new SocketService.SocketUserCallBack() {
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

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 格式：{"flag":0,"message":"@user_success"}
            JSONObject root = null;
            String server_msg = null;
            try {
                root = new JSONObject(msg.getData().getString("msg"));
                server_msg = root.getString(ClientUtil.JSON_MSG);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (server_msg.equals("@user_error")) {
                Toast.makeText(UserActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
            } else if (server_msg.equals("@user_success")) {
                Toast.makeText(UserActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserActivity.this, MapActivity.class));
            }
        }
    };
}
