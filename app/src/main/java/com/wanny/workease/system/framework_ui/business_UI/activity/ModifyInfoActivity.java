package com.wanny.workease.system.framework_ui.business_UI.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.PreferenceUtil;
import com.wanny.workease.system.framework_care.ActivityStackManager;
import com.wanny.workease.system.framework_mvpbasic.MvpActivity;
import com.wanny.workease.system.framework_uikite.dialog.HiFoToast;
import com.wanny.workease.system.framework_uikite.dialog.IOSDialogView;
import com.wanny.workease.system.workease_business.business.modify_mvp.ModifyInfoImpl;
import com.wanny.workease.system.workease_business.business.modify_mvp.ModifyInfoPresenter;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityEntity;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityResult;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerInofResult;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.modify_user_areaselect)
    TextView modifyUserAreaselect;
    @BindView(R.id.modify_user_password)
    TextView modifyUserPassword;
    //修改保存
    @BindView(R.id.modify_user_save)
    TextView modifyUserSave;

    private String userId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boss_modify_userinfo);
        ButterKnife.bind(this);
        userId = PreferenceUtil.getInstance(mContext).getString("bususerId", "");
        if (mvpPresenter != null) {
            mvpPresenter.getCityValue();
        }
        init();
    }

    private ArrayList<CityEntity> proviceList;

    private void init() {
        if (proviceList == null) {
            proviceList = new ArrayList<>();
        }
        if (titleTitle != null) {
            titleTitle.setText("修改个人信息");
        }
    }

    @Override
    public void success(CustomerInofResult customerInofResult) {
        if (customerInofResult.isSuccess()) {
            ActivityStackManager.getInstance().exitActivity(mActivity);
        } else {
            if (!TextUtils.isEmpty(customerInofResult.getMsg())) {
                new HiFoToast(mContext, customerInofResult.getMsg());
            } else {
                new HiFoToast(mContext, "个人信息修改失败");
            }
        }

    }


    @OnClick(R.id.modify_user_areaselect)
    void startSelectProvice(View view) {
        provices.clear();
        for (CityEntity entity : proviceList) {
            provices.add(entity.getName());
        }
        createIOS(provices, "选择省/市");
    }

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


    @OnClick(R.id.modify_user_password)
    void modifyPsd(View view) {
        Intent intent = new Intent(ModifyInfoActivity.this , ModifyPsdActivity.class);
        intent.putExtra("mode", ModifyPsdActivity.MODE_BUSINESS);
        startActivity(intent);
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }


    @OnClick(R.id.title_left)
    void backActivity(View view) {
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }

    private String selectCityId = "";
    private IOSDialogView iosDialogView;
    private IOSDialogView.IosDialogSelectListener iosDialogSelectListener = new IOSDialogView.IosDialogSelectListener() {
        @Override
        public void onItemClick(int position) {
            if (iosDialogView != null) {
                if (iosDialogView.isShowing()) {
                    iosDialogView.dismiss();
                    iosDialogView = null;
                }
            }
            if (modifyUserAreaselect != null) {
                if (!TextUtils.isEmpty(provices.get(position))) {
                    modifyUserAreaselect.setText(provices.get(position));
                    selectCityId = proviceList.get(position).getId();
                }
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

    //启动修改密码界面
    @OnClick(R.id.modify_user_password)
    void startModifyPsw(View view) {
        Intent intent = new Intent();

    }

    @OnClick(R.id.modify_user_save)
    void save(View view) {
        if (TextUtils.isEmpty(registerPhone.getText().toString())) {
            new HiFoToast(mContext, "请输入联系电话");
            return;
        }
        if (TextUtils.isEmpty(registerUsername.getText().toString())) {
            new HiFoToast(mContext, "请输入姓名");
            return;
        }

        if (TextUtils.isEmpty(selectCityId)) {
            new HiFoToast(mContext, "请选择城市");
            return;
        }
        if (mvpPresenter != null) {
            mvpPresenter.modifyUserInfo(userId, registerUsername.getText().toString(), registerPhone.getText().toString(), "", selectCityId, "", "", "", "正在保存");
        }
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

    private ArrayList<String> provices = new ArrayList<>();

    @Override
    public void getCityValue(CityResult cityResult) {
        if (cityResult.isSuccess()) {
            if (cityResult.getData() != null && cityResult.getData().size() > 0) {
                proviceList.addAll(cityResult.getData());
            }
        }
    }
}
