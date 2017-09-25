package com.wanny.workease.system.framework_ui.business_UI.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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
import com.wanny.workease.system.framework_uikite.dialog.IOSDialogView;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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


    //导航
    @BindView(R.id.location_daohang)
    ImageView locationDaohang;

    //位置信息
    @BindView(R.id.location_view_RelativeLayout)
    RelativeLayout propertyLocationViewRelativeLayout;


    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    //物业定位地址
    LatLng propertyLocation = null;
    //待查勘对象ID
    private BaiduMap mBaiduMap;
    public static final int MODE_SHOW = 0x0001;
    public static final int MODE_EDIT = 0x0002;
    private int mode = MODE_EDIT;
    private String objectAddress = "";


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
            mode = MODE_SHOW;
        } else {
            titleRightText.setText("定位");
            AppUtils.showView(titleRightText);
            mode = MODE_EDIT;
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
            if (mode == MODE_SHOW) {
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
                titleRightText.setText("重新选定");
            } else {
                //缩放地图等级，将地图移动到当前传递过来的位置
                MapStatus mapStatus = new MapStatus.Builder().target(propertyLocation).zoom(18)
                        .build();
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                        .newMapStatus(mapStatus);
                mBaiduMap.setMapStatus(mapStatusUpdate);
                titleRightText.setText("定位");
                //setMapStatusChangeListener();
                //清除当前maker
                mBaiduMap.clear();
                // 开启定位图标
                openLocationMarker();
            }
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
        mLocationClient.registerNotifyLocationListener(myListener);    //注册监听函数
        initLocation();
        mLocationClient.start();
    }

    private void initData() {
        if (getIntent().hasExtra("location")) {
            propertyLocation = getIntent().getParcelableExtra("location");
        }
        if (getIntent().hasExtra("address")) {
            objectAddress = getIntent().getStringExtra("address");
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

//        if ("定位".equals(titleRightText.getText().toString())) {
//            if (mBaiduMap != null) {
//                propertyLocation = mBaiduMap.getMapStatus().target;
//            }
//            if (propertyLocation != null) {
//                Intent intent = new Intent();
//                if (propertyLocation != null) {
//                    intent.putExtra("location", propertyLocation);
//                }
//                setResult(0x0002, intent);
//                ActivityStackManager.getInstance().exitActivity(mActivity);
//            } else {
//                new HiFoToast(getApplicationContext(), "请先等待定位或者手动修改定位。");
//            }
//        } else if ("重新选定".equals(titleRightText.getText().toString())) {
//            titleRightText.setText("定位");
//            //setMapStatusChangeListener();
//            //清除当前maker
//            mBaiduMap.clear();
//            // 开启定位图标
//            openLocationMarker();
//        }
    }


    @OnClick(R.id.location_daohang)
    void startDaohang(View view) {
        Intent intent = new Intent(LocationActivity.this, BusLineActivity.class);
        intent.putExtra("objectAddress", objectAddress);
        startActivity(intent);
//        createIosDialog();
    }


    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    private IOSDialogView iosDialogView;
    private ArrayList<String> mapArray;

    private void createIosDialog() {
        if (mapArray == null) {
            mapArray = new ArrayList<>();
        } else {
            mapArray.clear();
        }
        if (isAvilible(mContext, "com.baidu.BaiduMap")) {
            mapArray.add("百度地图");
        }
        if (isAvilible(mContext, "com.autonavi.minimap")) {
            mapArray.add("高德地图");
        }
        if (iosDialogView == null && mapArray.size() > 0) {
            int height = AppUtils.getScreenHeight(mContext) / 4;
            iosDialogView = new IOSDialogView(mActivity, R.style.dialog, mapArray, "选择地图", height);
        } else {
            Intent intent = new Intent(LocationActivity.this, BusLineActivity.class);
            intent.putExtra("objectAddress", objectAddress);
            startActivity(intent);
            return;
        }
        iosDialogView.setIosDialogSelectListener(new IOSDialogView.IosDialogSelectListener() {
            @Override
            public void onItemClick(int position) {
                if (iosDialogView != null) {
                    if (iosDialogView.isShowing()) {
                        iosDialogView.dismiss();
                        iosDialogView = null;
                    }
                }
                if (mapArray.get(position).equals("百度地图")) {
                    try {
//intent = Intent.getIntent("intent://map/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving®ion=西安&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                        Intent intent = Intent.parseUri("intent://map/direction?" +
                                //"origin=latlng:"+"34.264642646862,108.95108518068&" +   //起点  此处不传值默认选择当前位置
                                "destination=latlng:" + propertyLocation.latitude + "," + propertyLocation.longitude + "|name:我的目的地" +        //终点
                                "&mode=driving&" +          //导航路线方式
                                "region=北京" +           //
                                "&src=慧医#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end", Intent.URI_ANDROID_APP_SCHEME);
                        startActivity(intent); //启动调用
                    } catch (URISyntaxException e) {
                        Log.e("intent", e.getMessage());
                    }
                } else if (mapArray.get(position).equals("高德地图")) {
                    try {
                        Intent intent = Intent.parseUri("androidamap://navi?sourceApplication=易知工&poiname=我的目的地&lat=" + propertyLocation.latitude + "&lon=" + propertyLocation.longitude + "&dev=0", Intent.URI_ANDROID_APP_SCHEME);
                        startActivity(intent);
                    } catch (URISyntaxException e) {
                        Log.e("intent", e.getMessage());
                    }
                }
            }

            @Override
            public void cancel() {
                if (iosDialogView != null) {
                    if (iosDialogView.isShowing()) {
                        iosDialogView.dismiss();
                        iosDialogView = null;
                    }
                }
            }
        });
        iosDialogView.show();
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


