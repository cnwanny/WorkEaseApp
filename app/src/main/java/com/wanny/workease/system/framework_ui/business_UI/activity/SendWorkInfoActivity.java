package com.wanny.workease.system.framework_ui.business_UI.activity;

import android.os.Bundle;
import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpActivity;
import com.wanny.workease.system.workease_business.business.send_work_mvp.SendWorkImpl;
import com.wanny.workease.system.workease_business.business.send_work_mvp.SendWorkInfoPresenter;

/**
 * 文件名： SendWorkInfoActivity
 * 功能： 发布消息
 * 作者： wanny
 * 时间： 17:20 2017/8/14
 */
public class SendWorkInfoActivity extends MvpActivity<SendWorkInfoPresenter> implements SendWorkImpl {


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
    protected SendWorkInfoPresenter createPresenter() {
        return new SendWorkInfoPresenter(this);
    }
}
