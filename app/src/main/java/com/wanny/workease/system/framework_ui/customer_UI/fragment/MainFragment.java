package com.wanny.workease.system.framework_ui.customer_UI.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_care.AppContent;
import com.wanny.workease.system.framework_mvpbasic.MvpFragment;
import com.wanny.workease.system.framework_ui.customer_UI.activity.SearchWorkActivity;
import com.wanny.workease.system.framework_ui.customer_UI.activity.WorkInfoDetailActivity;
import com.wanny.workease.system.framework_uikite.dialog.HiFoToast;
import com.wanny.workease.system.framework_uikite.recycler.ListViewItemDecotion;
import com.wanny.workease.system.workease_business.customer.main_mvp.MainImpl;
import com.wanny.workease.system.workease_business.customer.main_mvp.MainPresenter;
import com.wanny.workease.system.workease_business.customer.main_mvp.WordListAdapter;
import com.wanny.workease.system.workease_business.customer.main_mvp.WorkInfoEntity;
import com.wanny.workease.system.workease_business.customer.main_mvp.WorkResult;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 文件名： MainFragment
 * 功能：
 * 作者： wanny
 * 时间： 13:53 2017/6/23
 */
public class MainFragment extends MvpFragment<MainPresenter> implements MainImpl ,SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.city_name)
    TextView cityName;

    @BindView(R.id.main_search)
    TextView mainSearch;

    @BindView(R.id.ordinal_recycler)
    RecyclerView ordinalRecycler;
    @BindView(R.id.ordinal_refresh)
    SwipeRefreshLayout ordinalRefresh;
    Unbinder unbinder;

    private WordListAdapter adapter;
    private ArrayList<WorkInfoEntity> dataList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.main_search)
    void startSearch(View view){
        Intent intent = new Intent(mActivity, SearchWorkActivity.class);
        startActivity(intent);
    }

    private LinearLayoutManager layoutManager;
    private int pageSize = 10;
    private void initView() {
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
        adapter.setWorkClickListener(workClickListener);
        ordinalRecycler.addOnScrollListener(onScrollListener);
        ordinalRecycler.addItemDecoration(new ListViewItemDecotion(mContext, ListViewItemDecotion.ORIVATION_VERCAL, R.drawable.listview_itemdec_drawable));
    }


    private WordListAdapter.WorkClickListener workClickListener = new WordListAdapter.WorkClickListener() {
        @Override
        public void click(int position) {
            Intent intent = new Intent(getActivity(), WorkInfoDetailActivity.class);
            intent.putExtra("entity",dataList.get(position));
            startActivity(intent);
        }
    };

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
            mvpPresenter.getWorkData(pageIndex, "正在加载");
            hasRunnin = true;
        }
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
            mvpPresenter.getWorkData(pageIndex, "正在加载");
            hasRunnin = true;
        }
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        if(mvpPresenter != null){
            mvpPresenter.getWorkData(1,"正在加载");
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

    @Override
    public void fail(String errorMessage) {
        hasRunnin = false;
        if(ordinalRefresh != null){
            if(ordinalRefresh.isRefreshing()){
                ordinalRefresh.setRefreshing(false);
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
    public void loadIng(String title) {

    }

    @Override
    public void hide() {

    }


    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }
}
