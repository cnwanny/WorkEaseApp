package com.wanny.workease.system.framework_ui.business_UI.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.PreferenceUtil;
import com.wanny.workease.system.framework_care.ActivityStackManager;
import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpActivity;
import com.wanny.workease.system.framework_ui.customer_UI.activity.HomeManagerActivity;
import com.wanny.workease.system.framework_ui.customer_UI.activity.LoginActivity;
import com.wanny.workease.system.framework_ui.customer_UI.activity.RegisterActivity;
import com.wanny.workease.system.framework_uikite.dialog.HiFoToast;
import com.wanny.workease.system.workease_business.business.bus_login_mvp.BusLoginImpl;
import com.wanny.workease.system.workease_business.business.bus_login_mvp.BusLoginPresenter;
import com.wanny.workease.system.workease_business.customer.login_mvp.LoginPresenter;
import com.wanny.workease.system.workease_business.customer.login_mvp.LoginResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件名： BusLoginActivity
 * 功能：
 * 作者： wanny
 * 时间： 17:50 2017/8/8
 */
public class BusLoginActivity extends MvpActivity<BusLoginPresenter> implements BusLoginImpl {


    @BindView(R.id.login_app_login)
    ImageView loginAppLogin;
    @BindView(R.id.login_username)
    EditText loginUsername;
    @BindView(R.id.login_password)
    EditText loginPassword;
    @BindView(R.id.login_copyright)
    TextView loginCopyright;
    @BindView(R.id.start_login)
    TextView startLogin;
    @BindView(R.id.start_register)
    TextView startRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_view);
        ButterKnife.bind(this);
        String mobile = PreferenceUtil.getInstance(mContext).getString("busmobile", "");
        if (!TextUtils.isEmpty(mobile)) {
            Intent intent = new Intent(BusLoginActivity.this,BusHomeManagerActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.start_login)
    void setStartLogin(View view) {
        if (TextUtils.isEmpty(loginUsername.getText().toString())) {
            new HiFoToast(mContext, "请输入账号");
            return;
        }
        if (TextUtils.isEmpty(loginPassword.getText().toString())) {
            new HiFoToast(mContext, "请输入密码");
            return;
        }
        if (mvpPresenter != null) {
            mvpPresenter.login(loginUsername.getText().toString(), loginPassword.getText().toString(), "正在登录");
        }
    }


    @Override
    public void success(LoginResult loginResult) {
        if (loginResult.isSuccess()) {
            if (loginResult.getData() != null) {
                PreferenceUtil.getInstance(mContext).saveString("busmobile", loginResult.getData().getMobile());
                PreferenceUtil.getInstance(mContext).saveString("busname", loginResult.getData().getUserName());
                PreferenceUtil.getInstance(mContext).saveString("bususerId", loginResult.getData().getUserId());
                Intent intent = new Intent(BusLoginActivity.this, BusHomeManagerActivity.class);
                startActivity(intent);
                ActivityStackManager.getInstance().exitActivity(mActivity);
            }
        } else {
            if (!TextUtils.isEmpty(loginResult.getMsg())) {
                new HiFoToast(mContext, loginResult.getMsg());
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0x0001 && resultCode == 0x0003){
            if(data != null){
                if(data.hasExtra("phone")){
                    loginUsername.setText(data.getStringExtra("phone"));
                    loginUsername.setSelection(data.getStringExtra("phone").length());
                }
            }
        }
    }

    @OnClick(R.id.start_register)
    void startRegiseter(View view) {
        Intent intent = new Intent(BusLoginActivity.this, BusRegisterActivity.class);
        startActivityForResult(intent, 0x0001);
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
    protected BusLoginPresenter createPresenter() {
        return new BusLoginPresenter(this);
    }
}
