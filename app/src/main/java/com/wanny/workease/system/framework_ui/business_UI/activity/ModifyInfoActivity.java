package com.wanny.workease.system.framework_ui.business_UI.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpActivity;
import com.wanny.workease.system.workease_business.business.modify_mvp.ModifyInfoImpl;
import com.wanny.workease.system.workease_business.business.modify_mvp.ModifyInfoPresenter;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerInofResult;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件名：com.wanny.workease.system.framework_ui.business_UI.activity
 * 功能：
 * 创建人：wanny
 * 日期：2017/8/13 上午10:37
 */

public class ModifyInfoActivity extends MvpActivity<ModifyInfoPresenter> implements ModifyInfoImpl {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boss_modify_userinfo);
        ButterKnife.bind(this);
    }

    @Override
    public void success(CustomerInofResult customerInofResult){


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
    protected ModifyInfoPresenter createPresenter() {
        return new ModifyInfoPresenter(this);
    }
}
