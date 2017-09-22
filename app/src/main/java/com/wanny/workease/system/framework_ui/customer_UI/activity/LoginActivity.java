package com.wanny.workease.system.framework_ui.customer_UI.activity;

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
import com.wanny.workease.system.framework_uikite.WaitDialog;
import com.wanny.workease.system.framework_uikite.dialog.HiFoToast;
import com.wanny.workease.system.framework_uikite.dialog.IOSDialogView;
import com.wanny.workease.system.workease_business.business.bus_login_mvp.ServicePhoneResult;
import com.wanny.workease.system.workease_business.customer.login_mvp.LoginImpl;
import com.wanny.workease.system.workease_business.customer.login_mvp.LoginPresenter;
import com.wanny.workease.system.workease_business.customer.login_mvp.LoginResult;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件名： LoginActivity
 * 功能：
 * 作者： wanny
 * 时间： 16:58 2017/6/22
 */
public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginImpl {


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
        String mobile = PreferenceUtil.getInstance(mContext).getString("mobile", "");
        if (!TextUtils.isEmpty(mobile)) {
            Intent intent = new Intent(LoginActivity.this,HomeManagerActivity.class);
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
        String pushToken = PreferenceUtil.getInstance(mContext).getString("channId","");
        if (mvpPresenter != null) {
            mvpPresenter.login(loginUsername.getText().toString(), loginPassword.getText().toString(),"0",pushToken, "正在登录");
        }
    }


    @Override
    public void success(LoginResult loginResult) {
        if (loginResult.isSuccess()) {
            if (loginResult.getData() != null) {
                PreferenceUtil.getInstance(mContext).saveString("mobile", loginResult.getData().getMobile());
                PreferenceUtil.getInstance(mContext).saveString("name", loginResult.getData().getUserName());
                PreferenceUtil.getInstance(mContext).saveString("userId", loginResult.getData().getUserId());
                Intent intent = new Intent(LoginActivity.this, HomeManagerActivity.class);
                startActivity(intent);
                ActivityStackManager.getInstance().exitActivity(mActivity);
            }
        } else {
            if (!TextUtils.isEmpty(loginResult.getMsg())) {
                new HiFoToast(mContext, loginResult.getMsg());
            }
        }

    }


    @OnClick(R.id.start_register)
    void startRegiseter(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, 0x0001);
    }


    private WaitDialog waitDialog;
    private void waitDialog(String loading) {
        waitDialog = new WaitDialog(mActivity, R.style.wait_dialog, loading);
        if(!mActivity.isFinishing()){
            waitDialog.show();
        }

    }


    @Override
    public void fail(String errorMessage) {
        if (waitDialog != null) {
            if (waitDialog.isShowing()) {
                waitDialog.hide();
                waitDialog = null;
            }
        }
    }

    @Override
    public void loadIng(String title) {
        waitDialog(title);
    }

    @Override
    public void hide() {
        if (waitDialog != null) {
            if (waitDialog.isShowing()) {
                waitDialog.hide();
                waitDialog = null;
            }
        }
    }

    @Override
    public void setJpush(OrdinalResultEntity entity) {

    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
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

    private ArrayList<String> servicePhone = new ArrayList<>();
    private boolean needDialog = false;
    private void startPhone() {
        if(servicePhone != null && servicePhone.size() > 0){
            createIOS(servicePhone,"拨打客服电话");
        }else{
            needDialog = true;
            mvpPresenter.getServicePhone();
        }

    }


    @Override
    public void getServicePhone(ServicePhoneResult entity) {
        if(entity.isSuccess()){
            servicePhone.clear();
            servicePhone.addAll(entity.getData());
            if(needDialog){
                createIOS(servicePhone,"拨打客服电话");
            }
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
