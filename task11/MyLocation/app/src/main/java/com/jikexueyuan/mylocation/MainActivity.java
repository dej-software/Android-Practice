package com.jikexueyuan.mylocation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private BaiduMap baiduMap;

    //    private LocationService locService;
    private LocationClient locClient;
    boolean isFirstLoc = true; // 是否首次定位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        // 地图初始化
        mapView = (MapView) findViewById(R.id.bmapView);
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

//        locService = ((MyLocationApplication) getApplication()).locationService;
//        locService.registerListener(mListener);
//        locService.setLocationOption(locService.getDefaultLocationClientOption());
//        locService.start();
    }

    private BDLocationListener mListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation != null) {
            }
        }
    };

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
            sb.append("\nradius : ");// 半径
            sb.append(bdLocation.getRadius());
            sb.append("\nCountryCode : ");// 国家码
            sb.append(bdLocation.getCountryCode());
            sb.append("\nCountry : ");// 国家名称
            sb.append(bdLocation.getCountry());
            sb.append("\ncitycode : ");// 城市编码
            sb.append(bdLocation.getCityCode());
            sb.append("\ncity : ");// 城市
            sb.append(bdLocation.getCity());
            sb.append("\nDistrict : ");// 区
            sb.append(bdLocation.getDistrict());
            sb.append("\nStreet : ");// 街道
            sb.append(bdLocation.getStreet());
            sb.append("\naddr : ");// 地址信息
            sb.append(bdLocation.getAddrStr());
            sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
            sb.append(bdLocation.getUserIndoorState());
            sb.append("\nDirection(not all devices have value): ");
            sb.append(bdLocation.getDirection());// 方向
            sb.append("\nbdLocationdescribe: ");
            sb.append(bdLocation.getLocationDescribe());// 位置语义化信息
            sb.append("\nPoi: ");// POI信息
            if (bdLocation.getPoiList() != null && !bdLocation.getPoiList().isEmpty()) {
                for (int i = 0; i < bdLocation.getPoiList().size(); i++) {
                    Poi poi = (Poi) bdLocation.getPoiList().get(i);
                    sb.append(poi.getName() + ";");
                }
            }
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(bdLocation.getSpeed());// 速度 单位：km/h
                sb.append("\nsatellite : ");
                sb.append(bdLocation.getSatelliteNumber());// 卫星数目
                sb.append("\nheight : ");
                sb.append(bdLocation.getAltitude());// 海拔高度 单位：米
                sb.append("\ngps status : ");
                sb.append(bdLocation.getGpsAccuracyStatus());// *****gps质量判断*****
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                // 运营商信息
                if (bdLocation.hasAltitude()) {// *****如果有海拔高度*****
                    sb.append("\nheight : ");
                    sb.append(bdLocation.getAltitude());// 单位：米
                }
                sb.append("\noperationers : ");// 运营商信息
                sb.append(bdLocation.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (bdLocation.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }

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
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(ll, 18.0f);
                //以动画方式更新地图状态
                baiduMap.animateMapStatus(mapStatusUpdate);
            }
        }
    };

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
        locClient.stop();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
    }
}
