package com.jikexueyuan.locationshareclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements ServiceConnection {

    private static final String TAG = "MapActivity";

    // 是否绑定了服务
    private boolean isBind = false;
    private SocketService.SocketBinder binder = null;

    private TextureMapView mapView;
    private BaiduMap baiduMap;

    private LocationClient locClient;
    boolean isFirstLoc = true; // 是否首次定位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MyLocationApplication.addActivity(this);
        initView();
    }

    private void initView() {
        // 地图初始化
        mapView = (TextureMapView) findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);

        // 定位初始化
        locClient = new LocationClient(getApplicationContext());
        locClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        // 设置是否使用GPS
        option.setOpenGps(true);
        // 定位模式 - 默认高精度
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 坐标类型
        option.setCoorType("bd0911");
        // 发起定位请求的时间间隔3s
        option.setScanSpan(3000);
        //设置需要地址信息
        option.setIsNeedAddress(true);

        locClient.setLocOption(option);
        locClient.start();

        // 绑定服务
        Intent intentService = new Intent(MapActivity.this, SocketService.class);
        isBind = bindService(intentService, this, Context.BIND_AUTO_CREATE);
    }

    //定位监听器
    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            /**
             * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
             * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
             */
            sb.append(bdLocation.getTime());
            sb.append("\nlocType : ");// 定位类型
            sb.append(bdLocation.getLocType());
            sb.append("\nlocType description : ");// *****对应的定位类型说明*****
            sb.append(bdLocation.getLocTypeDescription());
            sb.append("\nlatitude : ");// 纬度
            sb.append(bdLocation.getLatitude());
            sb.append("\nlontitude : ");// 经度
            sb.append(bdLocation.getLongitude());
            System.out.println(sb);

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())   //设置定位数据的精度，单位米
                    .direction(100)                     //设定定位数据的方向
                    .latitude(bdLocation.getLatitude()) //设定定位数据的纬度
                    .longitude(bdLocation.getLongitude())//设定定位数据的经度
                    .build();   //构建对象

            baiduMap.setMyLocationData(locData);

            if (isFirstLoc) {
                isFirstLoc = false;
                //地理坐标基本数据结构：经度和纬度
                LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());

                //描述地图状态将要发生的变化，newLatLngZoom设置地图新中心点
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(ll, 16.0f);
                //以动画方式更新地图状态
                baiduMap.animateMapStatus(mapStatusUpdate);
            }

            // 发送坐标到服务器
            binder.send(makeLocationMsg(bdLocation.getLatitude(), bdLocation.getLongitude()).toString());
        }
    };

    // 存放地图覆盖物
    private Map<MarkerOptions, Overlay> overlays = new HashMap<>();

    /**
     * 在地图上标记出位置信息
     * @param name
     * @param la
     * @param lo
     */
    public void addUserOverlay(String name, double la, double lo) {
        Log.d(TAG, "addUserOverlay");
        LatLng ll = new LatLng(la, lo);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.user_maker, null);
        TextView tvName = (TextView) view.findViewById(R.id.tvUserName);
        tvName.setText(name);

        // 自定义覆盖物
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(view);
        MarkerOptions options = null;
        Overlay overlay = null;

        // 同一个覆盖物  删掉旧的 添加新的
        Iterator iterator = overlays.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            options = (MarkerOptions) entry.getKey();
            overlay = (Overlay) entry.getValue();
            if (name.equals(options.getTitle())) {
                overlay.remove();

                // 如果客户端关闭 删掉此客户端用户的覆盖物
                if ((la == -1) && (lo == -1)) {
                    Log.d(TAG, "deleteOverlay");
                    iterator.remove();
                    return;
                }

                Log.d(TAG, "updateOverlay");
                options.position(ll);
                overlay = baiduMap.addOverlay(options);
                entry.setValue(overlay);
                return;
            }
        }

        // 新的覆盖物
        options = new MarkerOptions()
                .title(name)
                .position(ll)
                .icon(bitmap);
        overlay = baiduMap.addOverlay(options);
        overlays.put(options, overlay);
        Log.d(TAG, "addNewOverlay");
    }

    /**
     * 制作位置信息
     * @param la
     * @param lo
     * @return
     */
    private JSONObject makeLocationMsg(double la, double lo) {
        JSONObject root = new JSONObject();
        try {
            root.put(ClientUtil.JSON_FLAG, ClientUtil.DATA_FLAG_MAP);
            root.put(ClientUtil.JSON_NAME, ClientUtil.name);
            root.put(ClientUtil.JSON_LATITUDE, la);
            root.put(ClientUtil.JSON_LONGITUDE, lo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return root;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        locClient.stop();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        if (isBind) {
            unbindService(this);
        }
    }

    /**
     * 在这地图界面返回时 是否退出应用
     */
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Log.d(TAG, "onBackPressed");
        new AlertDialog.Builder(this)
                .setTitle("确定退出？")
                .setMessage("您要退出此应用吗？")
                .setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyLocationApplication.exitApp();
            }
        }).show();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder = (SocketService.SocketBinder) service;
        binder.getService().setMapCallBack(new SocketService.SocketMapCallBack() {
            @Override
            public void onMapMsgCallback(String msg) {
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

            // 格式：{"flag":2,"name":"user1","latitude":22.295347,"longitude":113.505356}
            JSONObject root = null;
            String name = null;
            double la = 0;
            double lo = 0;
            try {
                root = new JSONObject(msg.getData().getString("msg"));
                name = root.getString(ClientUtil.JSON_NAME);
                la = root.getDouble(ClientUtil.JSON_LATITUDE);
                lo = root.getDouble(ClientUtil.JSON_LONGITUDE);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

//            la = la + (0.01 * Math.random());
//            System.out.println("la:" + la);

            addUserOverlay(name, la, lo);
        }
    };
}
