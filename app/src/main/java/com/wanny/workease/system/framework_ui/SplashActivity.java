package com.wanny.workease.system.framework_ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.thinkcool.circletextimageview.CircleTextImageView;
import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.PreferenceUtil;
import com.wanny.workease.system.framework_care.ActivityStackManager;
import com.wanny.workease.system.framework_mvpbasic.BaseOperateImp;
import com.wanny.workease.system.framework_mvpbasic.BasePresenter;
import com.wanny.workease.system.framework_mvpbasic.MvpActivity;
import com.wanny.workease.system.framework_ui.business_UI.activity.BusHomeManagerActivity;
import com.wanny.workease.system.framework_ui.business_UI.activity.BusLoginActivity;
import com.wanny.workease.system.framework_ui.customer_UI.activity.HomeManagerActivity;
import com.wanny.workease.system.framework_ui.customer_UI.activity.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件名： SplashActivity
 * 功能：
 * 作者： wanny
 * 时间： 18:09 2017/8/9
 */
public class SplashActivity extends MvpActivity<BasePresenter> implements BaseOperateImp {


    @BindView(R.id.customer)
    CircleTextImageView customer;
    @BindView(R.id.business)
    CircleTextImageView business;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity_view);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.business)
    void startBusiness(View view) {
        String  busUser = PreferenceUtil.getInstance(mContext).getString("bususernmae","");
        if(!TextUtils.isEmpty(busUser)) {
            Intent intent = new Intent(SplashActivity.this , BusHomeManagerActivity.class);
            startActivity(intent);
            ActivityStackManager.getInstance().exitActivity(mActivity);
        }else{
            Intent intent = new Intent(SplashActivity.this , BusLoginActivity.class);
            startActivity(intent);
            ActivityStackManager.getInstance().exitActivity(mActivity);
        }
    }


    @OnClick(R.id.customer)
    void startCustomer(View view) {
        String customer = PreferenceUtil.getInstance(mContext).getString("mobile","");
        if (!TextUtils.isEmpty(customer)) {
            Intent intent = new Intent(SplashActivity.this , HomeManagerActivity.class);
            startActivity(intent);
            ActivityStackManager.getInstance().exitActivity(mActivity);
        }else{
            Intent intent = new Intent(SplashActivity.this , LoginActivity.class);
            startActivity(intent);
            ActivityStackManager.getInstance().exitActivity(mActivity);
        }
    }

    @Override
    public void success(Object o) {

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
    protected BasePresenter createPresenter() {
        return new BasePresenter();
    }
}
