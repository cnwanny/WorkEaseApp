package com.wanny.workease.system.framework_ui.business_UI.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.NewPremissionUtils;
import com.wanny.workease.system.framework_care.ActivityStackManager;
import com.wanny.workease.system.framework_care.AppContent;
import com.wanny.workease.system.framework_mvpbasic.MvpActivity;
import com.wanny.workease.system.framework_uikite.dialog.HiFoToast;
import com.wanny.workease.system.framework_uikite.dialog.IOSDialogView;
import com.wanny.workease.system.framework_uikite.dialog.MyDialog;
import com.wanny.workease.system.framework_uikite.recycler.ListViewItemDecotion;
import com.wanny.workease.system.workease_business.business.bus_main_mvp.BusMainAdapter;
import com.wanny.workease.system.workease_business.business.bus_main_mvp.BusMainImpl;
import com.wanny.workease.system.workease_business.business.bus_main_mvp.BusMainPresenter;
import com.wanny.workease.system.workease_business.business.bus_main_mvp.WordPeopleResult;
import com.wanny.workease.system.workease_business.business.bus_main_mvp.WorkPeopleEntity;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WoryTypeEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件名： SearchWorkActivity
 * 功能：
 * 作者： wanny
 * 时间： 16:06 2017/7/21
 */
public class SearchWokerActivity extends MvpActivity<BusMainPresenter> implements BusMainImpl, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.title_left)
    TextView titleLeft;
    @BindView(R.id.title_title)
    TextView titleTitle;

    @BindView(R.id.search_worker_typeselect)
    TextView searchWorkerTypeselect;

    @BindView(R.id.search_worker_stateeselect)
    TextView searchWorkerStateeselect;

    @BindView(R.id.search_worker_check)
    TextView searchWorkerCheck;

    @BindView(R.id.ordinal_recycler)
    RecyclerView ordinalRecycler;

    @BindView(R.id.ordinal_refresh)
    SwipeRefreshLayout ordinalRefresh;
    //
    private String selectWorkTypeId = "";
    //
    private String state = "";

    private ArrayList<WorkPeopleEntity> dataList;

    private static final int MODE_STATE = 0x0002;
    private static final int MODE_WORKTYPE = 0x0001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_worker_activity_view);
        ButterKnife.bind(this);
        initView();
        initViewRecyclerView();
    }

    private LinearLayoutManager layoutManager;
    private int pageSize = 10;
    private ArrayList<String> workerStateArray;
    private BusMainAdapter adapter;

    private void initViewRecyclerView() {
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


    private String currentPhone = "";
    private BusMainAdapter.WorkListListener workListListener = new BusMainAdapter.WorkListListener() {
        @Override
        public void click(int position) {
            //拨打电话
            currentPhone = dataList.get(position).getMobile();
            requsetPhone();
        }
    };


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
            if (totalItemCount >= pageSize) {
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
    public void success(WordPeopleResult wordPeopleResult) {
        hasRunnin = false;
        if (ordinalRefresh != null) {
            if (ordinalRefresh.isRefreshing()) {
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


    @Override
    public void fail(String errorMessage) {
        hasRunnin = false;
        if (ordinalRefresh != null) {
            if (ordinalRefresh.isRefreshing()) {
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
    protected BusMainPresenter createPresenter() {
        return new BusMainPresenter(this);
    }

    private int mode = MODE_STATE;

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

    private ArrayList<String> work_type = new ArrayList<>();

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
                if (searchWorkerTypeselect != null) {
                    if (!TextUtils.isEmpty(currentList.get(position))) {
                        searchWorkerTypeselect.setText(currentList.get(position));
                        selectWorkTypeId = workTypeList.get(position).getId();
                    }
                }
            } else if (mode == MODE_STATE) {
                if (searchWorkerStateeselect != null) {
                    if (!TextUtils.isEmpty(workerStateArray.get(position))) {
                        searchWorkerStateeselect.setText(workerStateArray.get(position));
                        if (workerStateArray.get(position).equals("忙碌")) {
                            state = "1";
                        } else {
                            state = "0";
                        }
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

    private void initView() {
        if (titleTitle != null) {
            titleTitle.setText("用工检索");
        }
        if (workTypeList == null) {
            workTypeList = new ArrayList<>();
        }
        if (workerStateArray == null) {
            workerStateArray = new ArrayList<>();
        }
        workerStateArray.add("忙碌");
        workerStateArray.add("空闲");
        if (mvpPresenter != null) {
            mvpPresenter.getWorkType();
        }

    }

    //点击选择工种
    @OnClick(R.id.search_worker_typeselect)
    void setWorkTypeSelect(View view) {
        mode = MODE_WORKTYPE;
        currentList.clear();
        currentList.addAll(work_type);
        createIOS(currentList, "选择工种");
    }


    @OnClick(R.id.search_worker_stateeselect)
    void setStateSelect(View view) {
        mode = MODE_STATE;
        currentList.clear();
        currentList.addAll(workerStateArray);
        createIOS(currentList, "选择用工状态");
    }

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

    private ArrayList<WoryTypeEntity> workTypeList;

    //点击查询操作
    @OnClick(R.id.search_worker_check)
    void startCheck(View view) {
        if (!hasRunnin) {
            hasRunnin = true;
            getValue();
        }
    }


    private void getValue() {
        if (TextUtils.isEmpty(state) && TextUtils.isEmpty(selectWorkTypeId)) {
            new HiFoToast(mContext, "请输入查询关键字");
            hasRunnin = false;
            return;
        }
        if (mvpPresenter != null) {
            mvpPresenter.getWorkList(pageIndex, selectWorkTypeId, state, "正在查询");
        }
    }

    @OnClick(R.id.title_left)
    void backActivity(View view) {
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }

}
