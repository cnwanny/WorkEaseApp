package com.wanny.workease.system.framework_ui.customer_UI.activity;

import android.os.Bundle;

import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpActivity;
import com.wanny.workease.system.workease_business.customer.work_detail.WorkDetailImpl;
import com.wanny.workease.system.workease_business.customer.work_detail.WorkDetailPresenter;

/**
 * 文件名： WorkInfoDetailActivity
 * 功能：
 * 作者： wanny
 * 时间： 15:28 2017/8/3
 */
public class WorkInfoDetailActivity extends MvpActivity<WorkDetailPresenter> implements WorkDetailImpl {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void success(OrdinalResultEntity ordinalResultEntity) {

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
    protected WorkDetailPresenter createPresenter() {
        return new WorkDetailPresenter(this);
    }
}
