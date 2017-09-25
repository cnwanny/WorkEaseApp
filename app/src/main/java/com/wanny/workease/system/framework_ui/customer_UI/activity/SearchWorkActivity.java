package com.wanny.workease.system.framework_ui.customer_UI.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_care.ActivityStackManager;
import com.wanny.workease.system.framework_care.AppContent;
import com.wanny.workease.system.framework_mvpbasic.MvpActivity;
import com.wanny.workease.system.framework_uikite.dialog.HiFoToast;
import com.wanny.workease.system.framework_uikite.dialog.IOSDialogView;
import com.wanny.workease.system.framework_uikite.recycler.ListViewItemDecotion;
import com.wanny.workease.system.workease_business.customer.main_mvp.WordListAdapter;
import com.wanny.workease.system.workease_business.customer.main_mvp.WorkInfoEntity;
import com.wanny.workease.system.workease_business.customer.main_mvp.WorkResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.City;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityEntity;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WoryTypeEntity;
import com.wanny.workease.system.workease_business.customer.search_work_mvp.SearchWorkImpl;
import com.wanny.workease.system.workease_business.customer.search_work_mvp.SearchWorkPresenter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.wanny.workease.system.framework_ui.customer_UI.activity.RegisterActivity.MODE_AREA;
import static com.wanny.workease.system.framework_ui.customer_UI.activity.RegisterActivity.MODE_PROVICE;
import static com.wanny.workease.system.framework_ui.customer_UI.activity.RegisterActivity.MODE_WORKTYPE;

/**
 * 文件名： SearchWorkActivity
 * 功能：
 * 作者： wanny
 * 时间： 16:06 2017/7/21
 */
public class SearchWorkActivity extends MvpActivity<SearchWorkPresenter> implements SearchWorkImpl,SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.title_left)
    TextView titleLeft;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.search_work_provice)
    TextView searchWorkProvice;
    @BindView(R.id.search_work_area)
    TextView searchWorkArea;
    @BindView(R.id.search_work_typeselect)
    TextView searchWorkTypeselect;
    @BindView(R.id.search_work_check)
    TextView searchWorkCheck;
    @BindView(R.id.ordinal_recycler)
    RecyclerView ordinalRecycler;
    @BindView(R.id.ordinal_refresh)
    SwipeRefreshLayout ordinalRefresh;
    //
    private String selectWorkTypeId = "";
    //
    private String selectAreaId = "";

    private WordListAdapter adapter;
    private ArrayList<WorkInfoEntity> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_work_activity_view);
        ButterKnife.bind(this);
        initView();
        initViewRecyclerView();
    }



    private LinearLayoutManager layoutManager;
    private int pageSize = 10;
    private void initViewRecyclerView() {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        ordinalRecycler.setHasFixedSize(true);
        ordinalRefresh.setOnRefreshListener(this);
        layoutManager = new LinearLayoutManager(mContext);
        ordinalRecycler.setLayoutManager(layoutManager);
        adapter = new WordListAdapter(dataList, mContext);
        if (adapter != null) {
            ordinalRecycler.setAdapter(adapter);
        }
//        adapter.setPriceListeListener(this);
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
        getValue();
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
        getValue();
    }


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

    @Override
    public void success(WorkResult workResult) {
        hasRunnin = false;
        if(ordinalRefresh != null){
            if(ordinalRefresh.isRefreshing()){
                ordinalRefresh.setRefreshing(false);
            }
        }
        if (workResult.isSuccess()) {
            if (workResult.getData() != null) {
                operateData(workResult.getData());
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }

        } else {
            if (!TextUtils.isEmpty(workResult.getMsg())) {
                new HiFoToast(mContext, workResult.getMsg());
            }
        }
    }


    // 分页查询数据 首先返回一个总的数目,首先判断是不是数据有重复的情况
    private void operateData(ArrayList<WorkInfoEntity> addinfo) {
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
    //获取重复的数据
    private ArrayList<WorkInfoEntity> getRepertData(ArrayList<WorkInfoEntity> olddata, ArrayList<WorkInfoEntity> newdata) {
        ArrayList<WorkInfoEntity> listdata = new ArrayList<>();
        if (newdata.size() > 0) {
            for (int i = 0; i < newdata.size(); i++) {
                WorkInfoEntity newentity = newdata.get(i);
                for (int j = 0; j < olddata.size(); j++) {
                    WorkInfoEntity oldentity = olddata.get(j);
                    if (oldentity.getId().equals(newentity.getId())) {
                        listdata.add(newentity);
                        break;
                    }
                }
            }
        }
        return listdata;
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



    @Override
    public void loadIng(String title) {

    }

    @Override
    public void hide() {

    }




    @Override
    protected SearchWorkPresenter createPresenter() {
        return new SearchWorkPresenter(this);
    }


    private int mode = MODE_PROVICE;

    @OnClick(R.id.search_work_provice)
    void startSelectProvice(View view){
        mode = MODE_PROVICE;
        provices.clear();
        for(CityEntity entity : proviceList){
            provices.add(entity.getName());
        }
        createIOS(provices,"选择省/市");
    }

    private String selectCityId = "";

    @OnClick(R.id.search_work_area)
    void startSelectArea(View view){
        mode = MODE_AREA;
        areas.clear();
        areadList.clear();
        for(CityEntity entity : proviceList){
            if(entity.getId().equals(selectCityId)){
                areadList.addAll(entity.getSubCitys());
                break;
            }
        }
        for(City entity : areadList){
            areas.add(entity.getName());
        }
        createIOS(areas,"选择市/区");
    }



    private ArrayList<CityEntity> proviceList;
    private ArrayList<City> areadList;
    @Override
    public void getCityValue(CityResult cityResult) {
        if(cityResult.isSuccess()){
            if(cityResult.getData() != null && cityResult.getData().size() > 0){
                proviceList.addAll(cityResult.getData());

            }
        }
    }
    private IOSDialogView iosDialogView;

    private void createIOS(ArrayList<String> data, String titlename) {
        if (iosDialogView == null) {
            iosDialogView = new IOSDialogView(mActivity, R.style.dialog, data, titlename,0);
            iosDialogView.setIosDialogSelectListener(iosDialogSelectListener);
            iosDialogView.setOnCancelListener(onCancelListener);
            iosDialogView.show();
        } else {
            if (!iosDialogView.isShowing()) {
                iosDialogView.show();
            }
        }
    }
    private ArrayList<String> work_type = new ArrayList<>();
    private ArrayList<String> provices = new ArrayList<>();
    private ArrayList<String> areas = new ArrayList<>();

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
    private ArrayList<String> currentList = new ArrayList<>();

    private IOSDialogView.IosDialogSelectListener iosDialogSelectListener = new IOSDialogView.IosDialogSelectListener() {
        @Override
        public void onItemClick(int position) {
            if (iosDialogView != null) {
                if (iosDialogView.isShowing()) {
                    iosDialogView.dismiss();
                    iosDialogView = null;
                }
            }
            if (mode == MODE_WORKTYPE) {
                if (searchWorkTypeselect != null) {
                    if (!TextUtils.isEmpty(currentList.get(position))) {
                        searchWorkTypeselect.setText(currentList.get(position));
                        selectWorkTypeId = workTypeList.get(position).getId();
                    }
                }
            }else if(mode == MODE_AREA){
                if (searchWorkArea != null) {
                    if (!TextUtils.isEmpty(areas.get(position))) {
                        searchWorkArea.setText(areas.get(position));
                        selectAreaId = areadList.get(position).getId();
                    }
                }
            }else if(mode == MODE_PROVICE ){
                if (searchWorkProvice != null) {
                    if (!TextUtils.isEmpty(provices.get(position))) {
                        searchWorkProvice.setText(provices.get(position));
                        selectCityId = proviceList.get(position).getId();
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

    private void initView(){
         if(titleTitle != null){
             titleTitle.setText("用工检索");
         }
        if(workTypeList == null){
            workTypeList = new ArrayList<>();
        }
        if(proviceList == null){
            proviceList = new ArrayList<>();
        }
        if(areadList == null){
            areadList = new ArrayList<>();
        }
        if(mvpPresenter != null){
            mvpPresenter.getWorkType();
        }
        if(mvpPresenter != null){
            mvpPresenter.getCityValue();
        }
    }
    //点击选择工种
    @OnClick(R.id.search_work_typeselect)
    void setWorkTypeSelect(View view){
        mode = MODE_WORKTYPE;
        currentList.clear();
        currentList.addAll(work_type);
        createIOS(currentList,"选择工种");
    }

    @Override
    public void workType(WorkTypeResult entity) {
        if(entity.isSuccess()){
            workTypeList.clear();
            work_type.clear();
            workTypeList.addAll(entity.getData());
            for(WoryTypeEntity value : workTypeList){
                work_type.add(value.getName());
            }
        }
    }

    private ArrayList<WoryTypeEntity> workTypeList;
    //点击查询操作
    @OnClick(R.id.search_work_check)
    void startCheck(View view){
        if(!hasRunnin){
            hasRunnin = true;
            getValue();
        }
    }



    private void getValue(){
        if(TextUtils.isEmpty(selectAreaId) && TextUtils.isEmpty(selectWorkTypeId)){
            new HiFoToast(mContext,"请输入查询关键字");
            hasRunnin = false;
            return;
        }
        if(mvpPresenter != null){
            mvpPresenter.getWorkByAreaIdAndrWorkType(selectAreaId,selectWorkTypeId,pageIndex,"正在查询");
        }
    }

    @OnClick(R.id.title_left)
    void backActivity(View view){
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }

}
