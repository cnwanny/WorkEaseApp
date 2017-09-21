package com.wanny.workease.system.framework_ui.business_UI.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.AppUtils;
import com.wanny.workease.system.framework_basicutils.NewPremissionUtils;
import com.wanny.workease.system.framework_basicutils.PreferenceUtil;
import com.wanny.workease.system.framework_care.ActivityStackManager;
import com.wanny.workease.system.framework_care.AppContent;
import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpActivity;
import com.wanny.workease.system.framework_ui.customer_UI.activity.HomeManagerActivity;
import com.wanny.workease.system.framework_ui.customer_UI.activity.LoginActivity;
import com.wanny.workease.system.framework_ui.customer_UI.activity.RegisterActivity;
import com.wanny.workease.system.framework_uikite.dialog.HiFoToast;
import com.wanny.workease.system.framework_uikite.dialog.IOSDialogView;
import com.wanny.workease.system.workease_business.business.bus_login_mvp.BusLoginImpl;
import com.wanny.workease.system.workease_business.business.bus_login_mvp.BusLoginPresenter;
import com.wanny.workease.system.workease_business.business.bus_login_mvp.ServicePhoneResult;
import com.wanny.workease.system.workease_business.customer.login_mvp.LoginPresenter;
import com.wanny.workease.system.workease_business.customer.login_mvp.LoginResult;

import java.util.ArrayList;

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
        if(mvpPresenter != null){
            mvpPresenter.getServicePhone();
        }
    }

    @OnClick(R.id.start_login)
    void setStartLogin(View view) {
        if (TextUtils.isEmpty(loginUsername.getText().toString())) {
            new HiFoToast(mContext, "请输入账号");
            return;
        }

        if (!AppUtils.isMobile(loginUsername.getText().toString())) {
            new HiFoToast(mContext, "请输入正确的电话号码");
            return;
        }

        if (TextUtils.isEmpty(loginPassword.getText().toString())) {
            new HiFoToast(mContext, "请输入密码");
            return;
        }
        if (mvpPresenter != null) {
            mvpPresenter.login(loginUsername.getText().toString(), loginPassword.getText().toString(),"1", "正在登录");
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

    private ArrayList<String > servicePhone = new ArrayList<>();

    @Override
    public void getServicePhone(ServicePhoneResult entity) {
        if(entity.isSuccess()){
            servicePhone.clear();
            servicePhone.addAll(entity.getData());
            if(dialogMode){
                createIOS(servicePhone,"拨打客服电话");
            }
        }
    }

    @OnClick(R.id.login_forget_password)
    void forgetPsd(View view){
        if(hasPhone){
            startPhone();
        }else{
            requsetPhone();
        }
    }

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

    private IOSDialogView iosDialogView;
    //创建对话框
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

    private IOSDialogView.IosDialogSelectListener iosDialogSelectListener = new IOSDialogView.IosDialogSelectListener() {
        @Override
        public void onItemClick(int position) {
            if (iosDialogView != null) {
                if (iosDialogView.isShowing()) {
                    iosDialogView.dismiss();
                    iosDialogView = null;
                }
            }
            try{
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + servicePhone.get(position)));
                startActivity(intent);
            }catch (SecurityException e){
                e.printStackTrace();
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

    boolean dialogMode  = false;
    private void startPhone() {
        if(servicePhone != null && servicePhone.size() > 0){
            createIOS(servicePhone,"拨打客服电话");
        }else{
            mvpPresenter.getServicePhone();
            dialogMode = true;
        }
    }


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


}
