package com.wanny.workease.system.framework_ui.business_UI.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.AppUtils;
import com.wanny.workease.system.framework_basicutils.NewPremissionUtils;
import com.wanny.workease.system.framework_basicutils.PreferenceUtil;
import com.wanny.workease.system.framework_care.AppContent;
import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpFragment;
import com.wanny.workease.system.framework_ui.business_UI.activity.LocationActivity;
import com.wanny.workease.system.framework_ui.business_UI.activity.MySendWorkListActivity;
import com.wanny.workease.system.framework_uikite.WaitDialog;
import com.wanny.workease.system.framework_uikite.dialog.HiFoToast;
import com.wanny.workease.system.framework_uikite.dialog.IOSDialogView;
import com.wanny.workease.system.workease_business.business.bus_sendinfo_mvp.BusSendinfoImpl;
import com.wanny.workease.system.workease_business.business.bus_sendinfo_mvp.BusSendinfoPresenter;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityEntity;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WoryTypeEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 文件名： BusSendInfoFragment
 * 功能：
 * 作者： wanny
 * 时间： 17:49 2017/8/8
 */
public class BusSendInfoFragment extends MvpFragment<BusSendinfoPresenter> implements BusSendinfoImpl {


    @BindView(R.id.title_left)
    TextView titleLeft;

    @BindView(R.id.title_title)
    TextView titleTitle;

    @BindView(R.id.title_right_text)
    TextView titleRightText;

    @BindView(R.id.send_work_projectname_edit)
    TextView sendWorkProjectNameEdit;

    @BindView(R.id.send_work_areaselect)
    TextView sendWorkAreaselect;

    @BindView(R.id.send_work_worktypeselect)
    TextView sendWorkWorktypeselect;

    @BindView(R.id.send_work_neednumber)
    TextView sendWorkNeednumber;

    @BindView(R.id.send_work_price_edit)
    EditText sendWorkPriceEdit;

    @BindView(R.id.send_work_detail)
    EditText sendWorkDetail;

    @BindView(R.id.send_work_location_left)
    TextView sendWorkLocationLeft;

    @BindView(R.id.send_work_location_edit)
    EditText sendWorkLocationEdit;

    @BindView(R.id.send_work_location_map)
    TextView sendWorkLocationMap;

    @BindView(R.id.send_work)
    TextView sendWork;

    @BindView(R.id.send_work_hascomplete)
    TextView sendWorkHascomplete;
    Unbinder unbinder;
    private GeoCoder geoCoder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.send_workinfo_activity_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        geoCoder = GeoCoder.newInstance();
        initView();
        return view;
    }


    private void initView() {
        if (titleLeft != null) {
            AppUtils.notShowView(titleLeft);
        }
        titleTitle.setText("用工信息发布");
        AppUtils.showView(titleRightText);
        titleRightText.setText("清空数据");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requesetLocation();
        if (mvpPresenter != null) {
            mvpPresenter.getCityValue();
            mvpPresenter.getWorkType();
        }
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

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    //当前定位到的位置信息
    LatLng curretnLocation = null;

    private void startAction() {
        mLocationClient = new LocationClient(getActivity());     //声明LocationClient类
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x0002) {
            if (data != null) {
                curretnLocation = data.getParcelableExtra("location");
                latlngToAddress(curretnLocation);
            }
        }
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
                }
                ////定位成功后设施坐标
                loc = new LatLng(location.getLatitude(), location.getLongitude());
            }

            //如果未成功定位，设置重庆默认坐标
            if (loc == null) {
                loc = new LatLng(28.564151, 106.580207);
            }
            if (!TextUtils.isEmpty(location.getAddrStr())) {
                if(sendWorkLocationEdit != null ){
                    sendWorkLocationEdit.setText(location.getAddrStr().toString());
                }
            }
            curretnLocation = loc;
            mLocationClient.stop();
//            new HiFoToast(getActivity(), sb.toString());
        }
    }


    @Override
    public void success(OrdinalResultEntity ordinalResultEntity) {
        if (waitDialog != null) {
            if (waitDialog.isShowing()) {
                waitDialog.hide();
                waitDialog = null;
            }
        }
        if (ordinalResultEntity.isSuccess()) {
            //跳转到对应的
            Intent intent = new Intent(getActivity(),MySendWorkListActivity.class);
            startActivity(intent);
        } else {
            if (!TextUtils.isEmpty(ordinalResultEntity.getMsg())) {
                new HiFoToast(mContext, ordinalResultEntity.getMsg());
            }
        }

    }


    @OnClick(R.id.title_right_text)
    void startClearData(View view){
        sendWorkPriceEdit.setText("");
        sendWorkProjectNameEdit.setText("");
        sendWorkDetail.setText("");
        selectAreaId = "";
        selectWorkTypeId = "";
        sendWorkAreaselect.setText("请选择城市");
        sendWorkWorktypeselect.setText("请选择工种");
    }



    @Override
    public void fail(String errorMessage) {
        if (waitDialog != null) {
            if (waitDialog.isShowing()) {
                waitDialog.hide();
                waitDialog = null;
            }
        }
    }

    @Override
    public void loadIng(String title) {
        waitDialog(title);
    }


    @Override
    protected BusSendinfoPresenter createPresenter() {
        return new BusSendinfoPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //工种
    public static final int MODE_WORKTYPE = 0x0001;
    //选择省
    public static final int MODE_PROVICE = 0x0003;
    private ArrayList<String> provices = new ArrayList<>();

    @OnClick(R.id.send_work_areaselect)
    void startSelectProvice(View view) {
        mode = MODE_PROVICE;
        provices.clear();
        for (CityEntity entity : provice) {
            provices.add(entity.getName());
        }
        createIOS(provices, "选择省/市");
    }


    //点击选择工种
    @OnClick(R.id.send_work_worktypeselect)
    void setWorkTypeSelect(View view) {
        mode = MODE_WORKTYPE;
//        work_type.clear();
//        work_type.addAll(work_type);
        createIOS(work_type, "选择工种");
    }


    private int mode = MODE_WORKTYPE;
    private IOSDialogView iosDialogView;

    private void createIOS(ArrayList<String> data, String titlename) {
        if (iosDialogView == null) {
            iosDialogView = new IOSDialogView(mActivity, R.style.dialog, data, titlename);
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
    private String selectWorkTypeId = "";
    private String selectAreaId = "";
    private IOSDialogView.IosDialogSelectListener iosDialogSelectListener = new IOSDialogView.IosDialogSelectListener() {
        @Override
        public void onItemClick(int position) {
            if (iosDialogView != null) {
                if (iosDialogView.isShowing()) {
                    iosDialogView.dismiss();
                    iosDialogView = null;
                }
            }
            if (mode == MODE_PROVICE) {
                if (sendWorkAreaselect != null) {
                    if (!TextUtils.isEmpty(provices.get(position))) {
                        sendWorkAreaselect.setText(provices.get(position));
                        selectAreaId = provice.get(position).getId();
                    }
                }
            } else if (mode == MODE_WORKTYPE) {
                if (sendWorkWorktypeselect != null) {
                    if (!TextUtils.isEmpty(work_type.get(position))) {
                        sendWorkWorktypeselect.setText(work_type.get(position));
                        selectWorkTypeId = workTypeList.get(position).getId();
                    }
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
    };


    private void latlngToAddress(LatLng latlng) {
        // 设置反地理经纬度坐标,请求位置时,需要一个经纬度
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
        //设置地址或经纬度反编译后的监听,这里有两个回调方法,
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            //经纬度转换成地址
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    new HiFoToast(mContext, "找不到该地址");
                }
                sendWorkLocationEdit.setText(result.getAddress());
            }

            //把地址转换成经纬度
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                // 详细地址转换在经纬度
                String address = result.getAddress();
            }
        });
    }


    private ArrayList<CityEntity> provice = new ArrayList<>();
    private ArrayList<WoryTypeEntity> workTypeList = new ArrayList<>();
    private ArrayList<String> work_type = new ArrayList<>();

    @Override
    public void workType(WorkTypeResult entity) {
        if (entity.isSuccess()) {
            workTypeList.clear();
            work_type.clear();
            workTypeList.addAll(entity.getData());
            for (WoryTypeEntity value : workTypeList) {
                work_type.add(value.getName());
            }
        }
    }

    @Override
    public void getCityValue(CityResult cityResult) {
        if (cityResult.isSuccess()) {
            if (cityResult.getData() != null && cityResult.getData().size() > 0) {
                provice.addAll(cityResult.getData());
            }
        }
    }

    //发布
    @OnClick(R.id.send_work)
    void startSend(View view) {
        //启动发布
        if(TextUtils.isEmpty(sendWorkProjectNameEdit.getText().toString())){
            new HiFoToast(mContext,"请先输入项目名称");
            return;
        }
        if(TextUtils.isEmpty(sendWorkPriceEdit.getText().toString())){
            new HiFoToast(mContext,"请先输入工种对应的价格");
            return;
        }
        if(TextUtils.isEmpty(sendWorkLocationEdit.getText().toString())){
            new HiFoToast(mContext,"请先选定项目位置");
            return;
        }
        if(TextUtils.isEmpty(sendWorkDetail.getText().toString())){
            new HiFoToast(mContext,"请先描述项目详情");
            return;
        }
        if(TextUtils.isEmpty(selectWorkTypeId)){
            new HiFoToast(mContext,"请先选择工种");
            return;
        }
        if (mvpPresenter != null) {
            String userId = PreferenceUtil.getInstance(mContext).getString("bususerId", "");
            int number = 0;
            if (!TextUtils.isEmpty(sendWorkNeednumber.getText().toString())) {
                number = Integer.valueOf(sendWorkNeednumber.getText().toString());
            }
            mvpPresenter.relaseWork(userId, selectAreaId, selectWorkTypeId, number, sendWorkPriceEdit.getText().toString(), sendWorkProjectNameEdit.getText().toString(), sendWorkDetail.getText().toString(), sendWorkLocationEdit.getText().toString(), "", curretnLocation.latitude, curretnLocation.longitude, "正在提交");
        }
    }


    //已经完成发布的
    @OnClick(R.id.send_work_hascomplete)
    void complete(View view) {
        //返回已经回价的列表
        Intent intent = new Intent(getActivity(), MySendWorkListActivity.class);
        startActivity(intent);
    }


    //已经完成发布的
    @OnClick(R.id.send_work_location_map)
    void lookMap(View view) {
        Intent intent = new Intent(getActivity(), LocationActivity.class);
        if (curretnLocation != null) {
            intent.putExtra("location", curretnLocation);
        }
        startActivityForResult(intent, 0x0002);
    }


    private WaitDialog waitDialog;

    private void waitDialog(String loading) {
        waitDialog = new WaitDialog(mActivity, R.style.wait_dialog, loading);
        if (!mActivity.isFinishing()) {
            waitDialog.show();
        }

    }


    @Override
    public void hide() {
        if (waitDialog != null) {
            if (waitDialog.isShowing()) {
                waitDialog.hide();
                waitDialog = null;
            }
        }
    }

}
