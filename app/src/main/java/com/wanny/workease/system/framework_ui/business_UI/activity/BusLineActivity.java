package com.wanny.workease.system.framework_ui.business_UI.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.MassTransitRoutePlanOption;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.SuggestAddrInfo;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.AppUtils;
import com.wanny.workease.system.framework_basicutils.NewPremissionUtils;
import com.wanny.workease.system.framework_care.ActivityStackManager;
import com.wanny.workease.system.framework_care.AppContent;
import com.wanny.workease.system.framework_mvpbasic.BasePresenter;
import com.wanny.workease.system.framework_mvpbasic.MvpActivity;
import com.wanny.workease.system.framework_ui.business_UI.baimap.MassTransitRouteOverlay;
import com.wanny.workease.system.framework_ui.business_UI.baimap.TransitRouteOverlay;
import com.wanny.workease.system.framework_ui.business_UI.fragment.BusSendInfoFragment;
import com.wanny.workease.system.framework_uikite.dialog.IOSDialogView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件名： BusLineActivity
 * 功能：
 * 作者： wanny
 * 时间： 11:10 2017/9/25
 */
public class BusLineActivity extends MvpActivity<BasePresenter> {


    @BindView(R.id.title_left)
    TextView titleLeft;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.title_right_text)
    TextView titleRightText;
    @BindView(R.id.bus_line_map)
    MapView busLineMap;


    @BindView(R.id.bus_line_guidance)
    TextView busLineGuidance;

    private String objectAddress;
    private RoutePlanSearch mSearch;

    private BaiduMap mBaidumap;

    private String currentCity = "";
    private LatLng location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_activity_view);
        ButterKnife.bind(this);
        initView();
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(routeListener);
        mBaidumap = busLineMap.getMap();
        mBaidumap.getUiSettings().setZoomGesturesEnabled(true);
        MapStatus mapStatus = new MapStatus.Builder().target(location).zoom(18)
                .build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(mapStatus);
        mBaidumap.setMapStatus(mapStatusUpdate);
        requesetLocation();
    }


    private void initView() {
        if (titleLeft != null) {
            titleLeft.setText("返回");
        }
        if (titleTitle != null) {
            titleTitle.setText("工地所在地公交线路");
        }
        if (getIntent() != null) {
            objectAddress = getIntent().getStringExtra("objectAddress");
            location = getIntent().getParcelableExtra("location");
        }
    }

    private void startShow() {
        PlanNode stMassNode = PlanNode.withCityNameAndPlaceName(currentCity.replace("市", ""), currentAddres);
        PlanNode enMassNode = PlanNode.withCityNameAndPlaceName(currentCity.replace("市", ""), objectAddress);
        mSearch.masstransitSearch(new MassTransitRoutePlanOption().from(stMassNode).to(enMassNode));
    }


    private ArrayList<PoiInfo> currentPoi = new ArrayList<>();

    OnGetRoutePlanResultListener routeListener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult result) {
            //获取跨城综合公共交通线路规划结果
            if (result == null) {
                //未找到结果
                return;
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                AppUtils.notShowView(busLineGuidance);
                //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                SuggestAddrInfo suggestAddrInfo = result.getSuggestAddrInfo();
                if (suggestAddrInfo != null) {
                    if (suggestAddrInfo.getSuggestEndNode() != null && suggestAddrInfo.getSuggestEndNode().size() > 0) {
                        currentPoi.clear();
                        currentPoi.addAll(suggestAddrInfo.getSuggestEndNode());
                        ArrayList<String> value = new ArrayList<>();
                        for (PoiInfo entity : currentPoi) {
                            value.add(entity.name);
                        }
                        createIOS(value, "请选择一个对应地址再查询");
                    }
                }
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                AppUtils.showView(busLineGuidance);
                StringBuffer stringBuffer = new StringBuffer();
                MassTransitRouteLine route = result.getRouteLines().get(0);
                if (route != null) {
                    List<List<MassTransitRouteLine.TransitStep>> value = route.getNewSteps();
                    for(List<MassTransitRouteLine.TransitStep> entity : value){
                        for(MassTransitRouteLine.TransitStep data : entity){
                            if(!TextUtils.isEmpty(data.getInstructions())){
                                stringBuffer.append(data.getInstructions()).append(",");
                            }
                        }
                    }
                }
//                for (MassTransitRouteLine.TransitStep value : route.getAllStep()) {
//                    stringBuffer.append(value.getName()).append(",");
//                }
                if (!TextUtils.isEmpty(stringBuffer.toString())) {
                    busLineGuidance.setText(stringBuffer.toString());
                }
                //创建公交路线规划线路覆盖物
//                TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
//                //设置公交路线规划数据
//                overlay.setData(route);

                MyMassTransitRouteOverlay overlay = new MyMassTransitRouteOverlay(mBaidumap);
                //设置公交路线规划数据
                overlay.setData(route);
                //将公交路线规划覆盖物添加到地图中
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }

        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
            if (transitRouteResult == null || transitRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                //未找到结果
                return;
            }
            if (transitRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                //result.getSuggestAddrInfo()
                return;
            }
            if (transitRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                TransitRouteLine route = transitRouteResult.getRouteLines().get(0);
                //创建公交路线规划线路覆盖物
                TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
                //设置公交路线规划数据
                overlay.setData(route);
                //将公交路线规划覆盖物添加到地图中
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    };


    @OnClick(R.id.title_left)
    void backActivity(View view){
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }


    private class MyMassTransitRouteOverlay extends MassTransitRouteOverlay {
        public MyMassTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_st);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_en);
        }


    }

    @Override
    protected BasePresenter createPresenter() {
        return new BasePresenter();
    }


    private NewPremissionUtils newPremissionUtils;
    private boolean need = false;
    private boolean hasLocation = false;

    private void requesetLocation() {
        if (newPremissionUtils == null) {
            newPremissionUtils = new NewPremissionUtils(mActivity);
        }
        boolean isneed = newPremissionUtils.hasNeedReqset();
        if (isneed) {
            hasLocation = newPremissionUtils.requestLocationPermissions(AppContent.LOCATION_REQUESTCODE);
            if (hasLocation) {
                startAction();
            }
        } else {
            startAction();
        }
    }


    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_st);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {

            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_en);
        }
    }

    public LocationClient mLocationClient = null;
    public BDAbstractLocationListener myListener = new MyLocationListener();
    //当前定位到的位置信息
    String currentAddres = "";

    private void startAction() {
        mLocationClient = new LocationClient(mContext);     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
        mLocationClient.start();
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
    public class MyLocationListener extends BDAbstractLocationListener {
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
                }
                ////定位成功后设施坐标
                loc = new LatLng(location.getLatitude(), location.getLongitude());
            }

            //如果未成功定位，设置重庆默认坐标
            if (loc == null) {
                loc = new LatLng(28.564151, 106.580207);
            }
            if (!TextUtils.isEmpty(location.getAddrStr())) {
                if (currentAddres != null) {
                    currentAddres = location.getAddrStr().toString();
                }
            }
            if (!TextUtils.isEmpty(location.getCity())) {
                currentCity = location.getCity();
            } else {
                currentCity = "重庆";
            }
            startShow();
            mLocationClient.stop();
        }
    }


    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        if (busLineMap != null) {
            busLineMap.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (busLineMap != null) {
            busLineMap.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        if (busLineMap != null) {
            busLineMap.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private IOSDialogView iosDialogView;

    //创建对话框
    private void createIOS(ArrayList<String> data, String titlename) {
        if (iosDialogView == null) {
            iosDialogView = new IOSDialogView(mActivity, R.style.dialog, data, titlename, 0);
            iosDialogView.setIosDialogSelectListener(iosDialogSelectListener);
            iosDialogView.setOnCancelListener(onCancelListener);
            iosDialogView.show();
        } else {
            if (!iosDialogView.isShowing()) {
                iosDialogView.show();
            }
        }
    }

    private Dialog.OnCancelListener onCancelListener = new Dialog.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            if (iosDialogView != null) {
                if (iosDialogView.isShowing()) {
                    iosDialogView.dismiss();
                    iosDialogView = null;
                }
            }

        }
    };

    private IOSDialogView.IosDialogSelectListener iosDialogSelectListener = new IOSDialogView.IosDialogSelectListener() {
        @Override
        public void onItemClick(int position) {
            if (iosDialogView != null) {
                if (iosDialogView.isShowing()) {
                    iosDialogView.dismiss();
                    iosDialogView = null;
                }
            }
            objectAddress = currentPoi.get(position).name;
            startShow();
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
    };

}
