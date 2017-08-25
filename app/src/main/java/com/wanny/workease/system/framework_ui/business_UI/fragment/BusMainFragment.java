package com.wanny.workease.system.framework_ui.business_UI.fragment;

import android.os.Bundle;
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
import com.wanny.workease.system.framework_uikite.dialog.HiFoToast;
import com.wanny.workease.system.framework_uikite.recycler.ListViewItemDecotion;
import com.wanny.workease.system.workease_business.business.bus_main_mvp.BusMainAdapter;
import com.wanny.workease.system.workease_business.business.bus_main_mvp.BusMainImpl;
import com.wanny.workease.system.workease_business.business.bus_main_mvp.BusMainPresenter;
import com.wanny.workease.system.workease_business.business.bus_main_mvp.WordPeopleResult;
import com.wanny.workease.system.workease_business.business.bus_main_mvp.WorkPeopleEntity;
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




    private BusMainAdapter.WorkListListener workListListener = new BusMainAdapter.WorkListListener() {
        @Override
        public void click(int position) {

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
