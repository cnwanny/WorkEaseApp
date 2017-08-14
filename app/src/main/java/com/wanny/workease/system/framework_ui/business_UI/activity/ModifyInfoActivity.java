package com.wanny.workease.system.framework_ui.business_UI.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpActivity;
import com.wanny.workease.system.workease_business.business.modify_mvp.ModifyInfoImpl;
import com.wanny.workease.system.workease_business.business.modify_mvp.ModifyInfoPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件名：com.wanny.workease.system.framework_ui.business_UI.activity
 * 功能：
 * 创建人：wanny
 * 日期：2017/8/13 上午10:37
 */

public class ModifyInfoActivity extends MvpActivity<ModifyInfoPresenter> implements ModifyInfoImpl {


    @BindView(R.id.title_left)
    TextView titleLeft;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.register_phone)
    EditText registerPhone;
    @BindView(R.id.register_username)
    EditText registerUsername;
    @BindView(R.id.register_area_provice)
    TextView registerAreaProvice;
    @BindView(R.id.register_area)
    TextView registerArea;
    @BindView(R.id.boss_register_username)
    TextView bossRegisterUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boss_modify_userinfo);
        ButterKnife.bind(this);
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
    protected ModifyInfoPresenter createPresenter() {
        return new ModifyInfoPresenter(this);
    }
}
