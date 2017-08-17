package com.wanny.workease.system.framework_ui.business_UI.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.AppUtils;
import com.wanny.workease.system.framework_basicutils.NewPremissionUtils;
import com.wanny.workease.system.framework_care.ActivityStackManager;
import com.wanny.workease.system.framework_care.AppContent;
import com.wanny.workease.system.framework_mvpbasic.BasePresenter;
import com.wanny.workease.system.framework_mvpbasic.MvpActivity;
import com.wanny.workease.system.framework_uikite.dialog.HiFoToast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by jery on 2016/1/14.
 */
public class LocationActivity extends MvpActivity<BasePresenter> {

    @BindView(R.id.title_left)
    TextView titleLeft;
    //标题
    @BindView(R.id.title_title)
    TextView titleTitleText;
    //右侧
    @BindView(R.id.title_right_text)
    TextView titleRightText;
    //mapView
    @BindView(R.id.bmapView)
    MapView bmapView;
    //位置信息
    @BindView(R.id.location_view_RelativeLayout)
    RelativeLayout propertyLocationViewRelativeLayout;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    //物业定位地址
    LatLng propertyLocation = null;
    //待查勘对象ID
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        setContentView(R.layout.location_activity_view);
        ButterKnife.bind(this);
        //获取地图控件引用
        initData();
        //判断是否为预览页面
        if (getIntent().hasExtra("isScan")) {
            titleRightText.setText("定位");
            AppUtils.notShowView(titleRightText);
        } else {
            titleRightText.setText("定位");
            AppUtils.showView(titleRightText);
        }
        if (titleTitleText != null) {
            titleTitleText.setText("工地定位");
        }
        mBaiduMap = bmapView.getMap();
        questRepremisson();

    }

    private NewPremissionUtils premissionUtils;
    private boolean hasLocation = false;

    private void questRepremisson() {
        if (premissionUtils == null) {
            premissionUtils = new NewPremissionUtils(mActivity);
        }
        boolean isneed = premissionUtils.hasNeedReqset();
        if (isneed) {
            hasLocation = premissionUtils.requestLocationPermissions(AppContent.LOCATION_REQUESTCODE);
            if (hasLocation) {
                setLocation();
            }
        } else {
            setLocation();
        }
    }

    private void setLocation() {
        if (propertyLocation == null) {
            startAction();
            return;
        } else {
            //已有地理位置，直接标注，当点击重新定位，则标注在中心点（）
            //1.重新设置地图中心点
            MapStatus mapStatus = new MapStatus.Builder().target(propertyLocation).zoom(18)
                    .build();
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                    .newMapStatus(mapStatus);
            mBaiduMap.setMapStatus(mapStatusUpdate);
            //2.构建Marker图标  //定义Maker坐标点
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.icon_property_location);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions ooption = new MarkerOptions()
                    .position(propertyLocation)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(ooption);
            titleRightText.setText("重新定位");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        operatePremission(requestCode, grantResults);
    }

    /**
     * 执行请求操作
     *
     * @param requestCode
     * @param grantResults
     */
    private void operatePremission(int requestCode, int[] grantResults) {
        if (requestCode == AppContent.LOCATION_REQUESTCODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setLocation();
            } else {
                new HiFoToast(mActivity, "请手动授权位置，不然定不到位");
            }
        }
    }

    private void startAction() {
        new HiFoToast(getApplicationContext(), "系统正在定位，请稍等");
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
        mLocationClient.start();
    }

    private void initData() {
        if (getIntent().hasExtra("location")) {
            propertyLocation = getIntent().getParcelableExtra("location");
        }
    }

    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        if (bmapView != null) {
            bmapView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        if (bmapView != null) {
            bmapView.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        if (bmapView != null) {
            bmapView.onPause();
        }
        super.onPause();

    }

    @OnClick(R.id.title_left)
    void goBack(View view) {
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }


    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        //int span = 1000;
        //option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }


    /**
     * 定位成功后将定位点设置为中心点，如果未成功，则默认中心点
     */
    public class MyLocationListener implements BDLocationListener {
        boolean isSuccessForLocation = false;

        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuffer sb = new StringBuffer(256);
            LatLng loc = null;
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("gps定位成功");
                isSuccessForLocation = true;

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("网络定位成功");
                isSuccessForLocation = true;
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                sb.append("离线定位成功，离线定位结果也是有效的");
                sb.append("离线定位成功");
                isSuccessForLocation = true;
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                isSuccessForLocation = false;
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
                isSuccessForLocation = false;
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                isSuccessForLocation = false;
            } else {
                sb.append("未知错误");
                isSuccessForLocation = false;
            }
            if (isSuccessForLocation) {
                if (location != null) {
                    if (location.getLocationDescribe() != null) {
                        sb.append(":" + location.getLocationDescribe());// 位置语义化信息
                    } else if (location.getAddrStr() != null) {
                        sb.append(":" + location.getAddrStr());
                    }
//                    else{
//                        sb.append(":"+location.getLatitude() +","+location.getLongitude());
//                    }
                }
                ////定位成功后设施坐标
                loc = new LatLng(location.getLatitude(), location.getLongitude());
            }

            //如果未成功定位，设置重庆默认坐标
            if (loc == null) {
                loc = new LatLng(28.564151, 106.580207);
            }
            MapStatus mapStatus = new MapStatus.Builder().target(loc).zoom(18)
                    .build();
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                    .newMapStatus(mapStatus);
            mBaiduMap.setMapStatus(mapStatusUpdate);
            //开启定位图标
            openLocationMarker();

            //GPS或者网络定位成功后设置坐标（）
            propertyLocation = loc;
            mLocationClient.stop();
            new HiFoToast(getApplicationContext(), sb.toString());

        }
    }

    //物业定位上传状态
    Boolean isSccucess = false;

    /**
     * 手动点击定位按钮提交定位数据
     */
    @OnClick(R.id.title_right_text)
    protected void propertyLocation(View view) {
        if ("定位".equals(titleRightText.getText().toString())) {
            if (mBaiduMap != null) {
                propertyLocation = mBaiduMap.getMapStatus().target;
            }
            if (propertyLocation != null) {
                Intent intent = new Intent();
                if (propertyLocation != null) {
                    intent.putExtra("location", propertyLocation);
                }
                setResult(0x0002, intent);
                ActivityStackManager.getInstance().exitActivity(mActivity);
            } else {
                new HiFoToast(getApplicationContext(), "请先等待定位或者手动修改定位。");
            }
        } else if ("重新定位".equals(titleRightText.getText().toString())) {
            titleRightText.setText("定位");
            //setMapStatusChangeListener();
            //清除当前maker
            mBaiduMap.clear();
            // 开启定位图标
            openLocationMarker();
        }
    }

    //开启定位图标
    private void openLocationMarker() {
        ImageView mImageView = new ImageView(this);
        mImageView.setImageResource(R.mipmap.icon_property_location_remark);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(108, 108);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        mImageView.setLayoutParams(params);
        propertyLocationViewRelativeLayout.addView(mImageView);
    }

    @Override
    protected BasePresenter createPresenter() {
        return new BasePresenter();
    }
}


