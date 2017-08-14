package com.wanny.workease.system.framework_ui.business_UI.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.AppUtils;
import com.wanny.workease.system.framework_basicutils.PreferenceUtil;
import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpFragment;
import com.wanny.workease.system.framework_ui.business_UI.activity.ModifyInfoActivity;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerInofResult;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerUserImpl;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerUserPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 文件名： BusUserCenterFragment
 * 功能：
 * 作者： wanny
 * 时间： 17:49 2017/8/8
 */
public class BusUserCenterFragment extends MvpFragment<CustomerUserPresenter> implements CustomerUserImpl {


    @BindView(R.id.tv_moblie_value)
    TextView tvMoblieValue;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_area_value)
    TextView tvAreaValue;

    @BindView(R.id.bus_usercenter_name)
    TextView busUsercenterName;


    @BindView(R.id.bus_usercenter_modifyinfo)
    TextView busUsercenterModifyinfo;

    Unbinder unbinder;


    private String mobile = "";
    private String userId = "";
    private String name = "";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.boss_frgament_main_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void success(CustomerInofResult customerInofResult) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mobile = PreferenceUtil.getInstance(mContext).getString("busmobile", "");
        userId = PreferenceUtil.getInstance(mContext).getString("bususerId", "");
        name = PreferenceUtil.getInstance(mContext).getString("busname", "");
        if(!TextUtils.isEmpty(name)){
            busUsercenterName.setText(name);
        }

        if(!TextUtils.isEmpty(mobile)){
            tvMoblieValue.setText(mobile);
        }
        if (mvpPresenter != null) {
            mvpPresenter.getUserInfo(userId, "", "正在加载");
        }
    }




    @OnClick(R.id.bus_usercenter_modifyinfo)
    void startModify(View view){
        Intent intent = new Intent(getActivity(),ModifyInfoActivity.class);
        startActivityForResult(intent,0x0001);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void fail(String errorMessage) {

    }

    @Override
    public void loadIng(String title) {

    }

    @Override
    public void hide() {

    }

    @Override
    public void modifySuccess(OrdinalResultEntity ordinalResultEntity) {

    }

    @Override
    public void workType(WorkTypeResult entity) {

    }


    //创建对象
    @Override
    protected CustomerUserPresenter createPresenter() {
        return new CustomerUserPresenter(this);
    }

    @Override
    public void getCityValue(CityResult cityResult) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
