package com.example.mo.allfind.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.mo.allfind.R;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    private MapView mapView = null;
    private BaiduMap mBaiduMap;
    boolean isFirstLoc = true;// 是否首次定位
    public LocationClient locationClient = null;
    private LatLng point;
    private LatLng point1;
    private List<LatLng> points = new ArrayList<LatLng>();
    private MarkerOptions options;
    private BitmapDescriptor bitmap, bitmap2;
    private Marker marker1;//自己标记
    private MarkerOnInfoWindowClickListener markerListener;

    private InfoWindow mInfowIndow;
    private View view;
    private TextView distence,tv_right;
    private Button dingwei,sao,cang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_second);
        init();

        locationClient = new LocationClient(getApplicationContext()); // 实例化LocationClient类
        locationClient.registerLocationListener(myListener); // 注册监听函数
        setLocationOption();    //设置定位参数
        locationClient.start(); // 开始定位
        onclick();
    }

    private void onclick() {
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng ll = marker.getPosition();
                Log.i("lanlat", ll.toString() + "a");
                Log.i("lanlat", points.get(0) + "b" + points.size());
                for (int i = 0; i < points.size(); i++) {
                    if (ll == points.get(i)) {
                        distence.setText("111");
                        mInfowIndow = new InfoWindow(view, ll, -47);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mBaiduMap.hideInfoWindow();
                            }
                        });
                        mBaiduMap.showInfoWindow(mInfowIndow);
                    }
                }
                return false;
            }
        });
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapActivity.this,"个人中心",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MapActivity.this,PersionalActivity.class);
                startActivity(i);

            }
        });
        sao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapActivity.this,SaoActivity.class);
                startActivity(i);
            }
        });
        cang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapActivity.this,SaoActivity.class);
                startActivity(i);
            }
        });
    }

    private final class MarkerOnInfoWindowClickListener implements InfoWindow.OnInfoWindowClickListener {

        @Override
        public void onInfoWindowClick() {
            //隐藏InfoWindow
            mBaiduMap.hideInfoWindow();
        }

    }

    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null || mapView == null)
                return;
            //29.917063   29.916063  29.918055
            //121.521415  121.520415 121.522315
            //29.892273   121.486969
            point = new LatLng(29.916063, 121.520415);
            points.add(point);
            point = new LatLng(29.918055, 121.522315);
            points.add(point);
            Log.i("地点",bdLocation.getLatitude()+"|||"+bdLocation.getLongitude());
            if (isFirstLoc) {
                isFirstLoc = false;
                point1 = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());

                options = new MarkerOptions()
                        .position(point1)
                        .icon(bitmap)
                        .zIndex(9)
                        .title("我的位置");
                marker1 = (Marker) mBaiduMap.addOverlay(options);
                for (int i = 0; i < points.size(); i++) {
                    double a = points.get(i).latitude;
                    Log.i("aaaa", a + "a");
                    options = new MarkerOptions()
                            .position(points.get(i))
                            .icon(bitmap2)
                            .zIndex(9)
                            .title("红包");
                    marker1 = (Marker) mBaiduMap.addOverlay(options);
                }
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(point1, 19);    //设置地图中心点以及缩放级别
                mBaiduMap.setMapStatus(u);
            }
        }
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    };
    private void init(){
        mapView = (MapView) findViewById(R.id.mapview);
        dingwei = (Button) findViewById(R.id.dingwei);
        sao = (Button) findViewById(R.id.sao);
        cang = (Button) findViewById(R.id.cang);
        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ren);
        bitmap2 = BitmapDescriptorFactory.fromResource(R.mipmap.redy);
        mBaiduMap = mapView.getMap();
        view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.hongbaoinfo_window, null);
        distence = (TextView) view.findViewById(R.id.distence);
        tv_right = (TextView) findViewById(R.id.tv_right);
        mapView.showZoomControls(false);//不显示地图缩放控件（按钮控制栏）
        mapView.removeViewAt(1);//去掉百度地图logo
    }
    /**
     * 设置定位参数
     */
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开GPS
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(1000); // 设置发起定位请求的间隔时间为1000ms
        option.setIsNeedAddress(true); // 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向
        locationClient.setLocOption(option);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }
}
