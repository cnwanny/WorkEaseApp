package com.wanny.workease.system.framework_ui.business_UI.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.NewPremissionUtils;
import com.wanny.workease.system.framework_care.AppContent;
import com.wanny.workease.system.framework_mvpbasic.MvpFragment;
import com.wanny.workease.system.framework_ui.business_UI.activity.SearchWokerActivity;
import com.wanny.workease.system.framework_uikite.dialog.HiFoToast;
import com.wanny.workease.system.framework_uikite.dialog.MyDialog;
import com.wanny.workease.system.framework_uikite.recycler.ListViewItemDecotion;
import com.wanny.workease.system.workease_business.business.bus_main_mvp.BusMainAdapter;
import com.wanny.workease.system.workease_business.business.bus_main_mvp.BusMainImpl;
import com.wanny.workease.system.workease_business.business.bus_main_mvp.BusMainPresenter;
import com.wanny.workease.system.workease_business.business.bus_main_mvp.WordPeopleResult;
import com.wanny.workease.system.workease_business.business.bus_main_mvp.WorkPeopleEntity;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 文件名： BusMainFragment
 * 功能：
 * 作者： wanny
 * 时间： 17:49 2017/8/8
 */
public class BusMainFragment extends MvpFragment<BusMainPresenter> implements BusMainImpl ,SwipeRefreshLayout.OnRefreshListener {

    //城市选择
    @BindView(R.id.city_name)
    TextView cityName;
    //城市检索
    @BindView(R.id.main_search)
    TextView mainSearch;
    //滚动
    @BindView(R.id.ordinal_recycler)
    RecyclerView ordinalRecycler;
    //下拉刷新
    @BindView(R.id.ordinal_refresh)
    SwipeRefreshLayout ordinalRefresh;

    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private LinearLayoutManager layoutManager;
    private int pageSize = 10;
    private ArrayList<WorkPeopleEntity> dataList;
    private BusMainAdapter adapter;

    private void initView() {
        requesetLocation();
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        ordinalRecycler.setHasFixedSize(true);
        ordinalRefresh.setOnRefreshListener(this);
        layoutManager = new LinearLayoutManager(mContext);
        ordinalRecycler.setLayoutManager(layoutManager);
        adapter = new BusMainAdapter(dataList, mContext);
        if (adapter != null) {
            ordinalRecycler.setAdapter(adapter);
        }
        adapter.setWorkListListener(workListListener);
        ordinalRecycler.addOnScrollListener(onScrollListener);
        ordinalRecycler.addItemDecoration(new ListViewItemDecotion(mContext, ListViewItemDecotion.ORIVATION_VERCAL, R.drawable.listview_itemdec_drawable));
    }





    private boolean hasRunnin = false;
    //滚动监听
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            int totalItemCount = layoutManager.getItemCount();
            //lastVisibleItem >= totalItemCount - 2 表示剩下4个item自动加载，各位自由选择 ,总数大于已经显示的条数的话
//            // dy>0 表示向下滑动、
            if(totalItemCount >= pageSize){
                if (lastVisibleItem >= totalItemCount - 2 && dy > 0) {
                    if (!hasRunnin) {
                        //加载更多
                        pageIndex = (totalItemCount / pageSize) + 1;
                        loadMoreData();
                    }
                }
            }
        }
    };
    private int pageIndex = 1;
    //    private int pageSize = 10;
    private void upLoad() {
        flag = AppContent.MODE_UPLOAD;
        pageIndex = 1;
        if (mvpPresenter != null) {
            mvpPresenter.getWorkList(pageIndex,"","", "正在加载");
            hasRunnin = true;
        }
    }


    @OnClick(R.id.city_name)
    void citySelect(View view){
        new HiFoToast(mContext,"目前只开放当前城市");
    }

//    //
//    @Override
//    public void startPrice(int position) {
//        if (type == 0) {
//            Intent intent = new Intent(getActivity(), CallBackActivity.class);
//            intent.putExtra("projectId", dataList.get(position).getProjectId());
//            intent.putExtra("objectId", dataList.get(position).getObjectId());
//            intent.putExtra("type", CallBackActivity.MODE_CALL);
//            startActivityForResult(intent, 0x0002);
//        } else if (type == 1) {
//            Intent intent = new Intent(getActivity(), CallPriceDetailActivity.class);
//            intent.putExtra("projectId", dataList.get(position).getProjectId());
//            intent.putExtra("objectId", dataList.get(position).getObjectId());
//            intent.putExtra("entity", dataList.get(position));
//            startActivity(intent);
//        } else {
//            new HiFoToast(mContext, "该对象已经终止");
//        }
//    }


    private String flag = "";
    private void loadMoreData() {
        hasRunnin = true;
        flag = AppContent.MODE_LOADMORE;
        if (mvpPresenter != null) {
            mvpPresenter.getWorkList(pageIndex,"","", "正在加载");
            hasRunnin = true;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        if(mvpPresenter != null){
            mvpPresenter.getWorkList(1,"","","正在加载");
        }
    }



    @Override
    public void fail(String errorMessage) {
        hasRunnin = false;
        if(ordinalRefresh != null){
            if(ordinalRefresh.isRefreshing()){
                ordinalRefresh.setRefreshing(false);
            }
        }
    }

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
            if (!TextUtils.isEmpty(location.getCity())) {
                if(cityName != null ){
                    cityName.setText(location.getCity().toString());
                }
            }
            curretnLocation = loc;
            mLocationClient.stop();
//            new HiFoToast(getActivity(), sb.toString());
        }
    }


    // 分页查询数据 首先返回一个总的数目,首先判断是不是数据有重复的情况
    private void operateData(ArrayList<WorkPeopleEntity> addinfo) {
        if (flag == AppContent.MODE_UPLOAD) {
            dataList.clear();
            dataList.addAll(0, addinfo);
        } else if (flag == AppContent.MODE_LOADMORE) {
            addinfo.removeAll(getRepertData(dataList, addinfo));
            dataList.addAll(dataList.size(), addinfo);
        } else {
            dataList.addAll(addinfo);
        }
    }


    @OnClick(R.id.main_search)
    void startSearch(View view){
        Intent intent = new Intent(getActivity(), SearchWokerActivity.class);
        startActivity(intent);
    }

    @Override
    public void workType(WorkTypeResult entity) {

    }

    //获取重复的数据
    private ArrayList<WorkPeopleEntity> getRepertData(ArrayList<WorkPeopleEntity> olddata, ArrayList<WorkPeopleEntity> newdata) {
        ArrayList<WorkPeopleEntity> listdata = new ArrayList<>();
        if (newdata.size() > 0) {
            for (int i = 0; i < newdata.size(); i++) {
                WorkPeopleEntity newentity = newdata.get(i);
                for (int j = 0; j < olddata.size(); j++) {
                    WorkPeopleEntity oldentity = olddata.get(j);
                    if (oldentity.getUserId().equals(newentity.getUserId())) {
                        listdata.add(newentity);
                        break;
                    }
                }
            }
        }
        return listdata;
    }



   private String currentPhone = "";
    private BusMainAdapter.WorkListListener workListListener = new BusMainAdapter.WorkListListener() {
        @Override
        public void click(int position) {
            currentPhone = dataList.get(position).getMobile();
            requsetPhone();
        }
    };

    @Override
    public void onRefresh() {
        if (!hasRunnin) {
            hasRunnin = true;
            upLoad();
            if (ordinalRefresh != null) {
                if (!ordinalRefresh.isRefreshing()) {
                    ordinalRefresh.setRefreshing(true);
                }
            }
        }
    }



    private NewPremissionUtils newPremissionUtils;

    private boolean hasNeed = false;
    private boolean hasPhone = false;

    private void requsetPhone() {
        if (newPremissionUtils == null) {
            newPremissionUtils = new NewPremissionUtils(mActivity);
        }
        hasNeed = newPremissionUtils.hasNeedReqset();
        if (hasNeed) {
            hasPhone = newPremissionUtils.requesCallPhonePermissions(AppContent.PHONE_REQUESTCODE);
            if (hasPhone) {
                startPhone();
            }
        } else {
            startPhone();
        }
    }

    boolean dialogMode = false;

    private void startPhone() {
        createMyDialog();
    }

    private MyDialog myDialog;


    private void createMyDialog() {
        if (myDialog == null) {
            myDialog = new MyDialog(mActivity, R.style.dialog, "确定拨打该电话吗", "", mActivity);
        }
        myDialog.setClickListener(clickListenerInterface);
        myDialog.show();
    }


    private MyDialog.ClickListenerInterface clickListenerInterface = new MyDialog.ClickListenerInterface() {
        @Override
        public void cancel() {
            if (myDialog != null) {
                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                    myDialog = null;
                }
            }
        }

        @Override
        public void sure(String editdata, String pricecallback) {
            if (myDialog != null) {
                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                    myDialog = null;
                }
            }
            try {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + currentPhone));
                startActivity(intent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
        if (requestCode == AppContent.PHONE_REQUESTCODE) {
            if (paramArrayOfInt[0] == PackageManager.PERMISSION_GRANTED) {
                hasPhone = true;
                startPhone();
            } else {
                new HiFoToast(mActivity, "请手动授权打电话权限");
            }
        }
    }


    @Override
    public void success(WordPeopleResult wordPeopleResult) {
        hasRunnin = false;
        if(ordinalRefresh != null){
            if(ordinalRefresh.isRefreshing()){
                ordinalRefresh.setRefreshing(false);
            }
        }
        if (wordPeopleResult.isSuccess()) {
            if (wordPeopleResult.getData() != null) {
                operateData(wordPeopleResult.getData());
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }

        } else {
            if (!TextUtils.isEmpty(wordPeopleResult.getMsg())) {
                new HiFoToast(mContext, wordPeopleResult.getMsg());
            }
        }
    }



    @Override
    public void loadIng(String title) {

    }

    @Override
    public void hide() {

    }

    @Override
    protected BusMainPresenter createPresenter() {
        return new BusMainPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
