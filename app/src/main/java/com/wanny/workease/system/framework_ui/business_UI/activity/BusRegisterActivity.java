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
import com.wanny.workease.system.framework_basicutils.AppUtils;
import com.wanny.workease.system.framework_care.ActivityStackManager;
import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpActivity;
import com.wanny.workease.system.framework_uikite.WaitDialog;
import com.wanny.workease.system.framework_uikite.dialog.HiFoToast;
import com.wanny.workease.system.framework_uikite.dialog.IOSDialogView;
import com.wanny.workease.system.workease_business.customer.register_mvp.City;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityEntity;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.RegisterImpl;
import com.wanny.workease.system.workease_business.customer.register_mvp.RegisterPresenter;
import com.wanny.workease.system.workease_business.customer.register_mvp.RegisterResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件名： BusRegisterActivity
 * 功能：
 * 作者： wanny
 * 时间： 17:50 2017/8/8
 */
public class BusRegisterActivity extends MvpActivity<RegisterPresenter> implements RegisterImpl {

    //返回
    @BindView(R.id.title_left)
    TextView titleLeft;
    //标题
    @BindView(R.id.title_title)
    TextView titleTitle;
    //名字
    @BindView(R.id.bus_register_username)
    EditText busRegisterUsername;
    //电话号码
    @BindView(R.id.bus_register_phone)
    EditText busRegisterPhone;
    //输入密码
    @BindView(R.id.bus_register_password)
    EditText busRegisterPassword;
    //再次输入密码
    @BindView(R.id.bus_register_againpass)
    EditText busRegisterAgainpass;
    //省份
    @BindView(R.id.bus_register_area_provice)
    TextView busRegisterAreaProvice;
    //区域
    @BindView(R.id.bus_register_area)
    TextView busRegisterArea;
    //注册
    @BindView(R.id.bus_start_register)
    TextView busStartRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_register_activity_view);
        ButterKnife.bind(this);
        initView();
        if (mvpPresenter != null) {
            mvpPresenter.getCityValue();
        }
    }

    private ArrayList<String> provices = new ArrayList<>();
    private ArrayList<String> areas = new ArrayList<>();

    @Override
    public void success(OrdinalResultEntity ordinalResultEntity) {

    }

    private ArrayList<CityEntity> proviceList;
    private ArrayList<City> areadList;

    private void initView() {
        if(titleTitle != null){
            titleTitle.setText("注册");
        }
        if (proviceList == null) {
            proviceList = new ArrayList<>();
        }
        if (areadList == null) {
            areadList = new ArrayList<>();
        }
    }

    @Override
    public void fail(String errorMessage) {

    }


    @OnClick(R.id.bus_register_area_provice)
    void startSelectProvice(View view) {
        mode = MODE_PROVICE;
        provices.clear();
        for (CityEntity entity : proviceList) {
            provices.add(entity.getName());
        }
        createIOS(provices, "选择省/市");
    }

    @OnClick(R.id.bus_register_area)
    void startSelectArea(View view) {
        mode = MODE_AREA;
        areas.clear();
        areadList.clear();
        for (CityEntity entity : proviceList) {
            if (entity.getId().equals(selectCityId)) {
                areadList.addAll(entity.getSubCitys());
                break;
            }
        }
        for (City entity : areadList) {
            areas.add(entity.getName());
        }
        createIOS(areas, "选择市/区");
    }


    @Override
    public void registerSuccess(RegisterResult entity) {
        hasRunning = false;
        if (entity.isSuccess()) {
            if (!TextUtils.isEmpty(entity.getMsg())) {
                new HiFoToast(mContext, entity.getMsg() + ",请登录");
                Intent intent = new Intent();
                intent.putExtra("phone", busRegisterPhone.getText().toString());
                setResult(0x0003, intent);
                ActivityStackManager.getInstance().exitActivity(mActivity);
            }
        } else {
            if (!TextUtils.isEmpty(entity.getMsg())) {
                new HiFoToast(mContext, entity.getMsg());
            }
        }
    }

    @Override
    public void workType(WorkTypeResult entity) {

    }

    @Override
    public void getCityValue(CityResult cityResult) {
        if (cityResult.isSuccess()) {
            if (cityResult.getData() != null && cityResult.getData().size() > 0) {
                proviceList.addAll(cityResult.getData());
            }
        }
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }

    private ArrayList<String> currentList = new ArrayList<>();

    private WaitDialog waitDialog;

    private void waitDialog(String loading) {
        waitDialog = new WaitDialog(mActivity, R.style.wait_dialog, loading);
        waitDialog.show();
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


    private IOSDialogView iosDialogView;
    //选择省
    public static final int MODE_PROVICE = 0x0003;
    //选择市
    public static final int MODE_AREA = 0x0004;


    private boolean hasRunning = false;

    @OnClick(R.id.bus_start_register)
    void startRegister(View view) {
        if (!hasRunning) {
            hasRunning = true;
            startRegister();
        }
    }

    private void startRegister() {
        if (TextUtils.isEmpty(busRegisterPhone.getText().toString())) {
            new HiFoToast(mContext, "请输入电话号码");
            hasRunning = false;
            return;
        }
        if (!AppUtils.isMobile(busRegisterPhone.getText().toString())) {
            new HiFoToast(mContext, "请输入正确的电话号码");
            hasRunning = false;
            return;
        }
        if (TextUtils.isEmpty(busRegisterPassword.getText().toString())) {
            new HiFoToast(mContext, "请输入密码");
            hasRunning = false;
            return;
        }
        if (TextUtils.isEmpty(busRegisterUsername.getText().toString())) {
            new HiFoToast(mContext, "请输入姓名");
            hasRunning = false;
            return;
        }
        if (mvpPresenter != null) {
            mvpPresenter.register(busRegisterPhone.getText().toString(), busRegisterPassword.getText().toString(), "1", busRegisterUsername.getText().toString(), selectAreaId, "" , "", "");
        }
    }

    private String selectAreaId = "";
    private int mode = MODE_PROVICE;

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

    private String selectCityId = "";
    private IOSDialogView.IosDialogSelectListener iosDialogSelectListener = new IOSDialogView.IosDialogSelectListener() {
        @Override
        public void onItemClick(int position) {
            if (iosDialogView != null) {
                if (iosDialogView.isShowing()) {
                    iosDialogView.dismiss();
                    iosDialogView = null;
                }
            }
            if (mode == MODE_AREA) {
                if (busRegisterArea != null) {
                    if (!TextUtils.isEmpty(areas.get(position))) {
                        busRegisterArea.setText(areas.get(position));
                        selectAreaId = areadList.get(position).getId();
                    }
                }
            } else if (mode == MODE_PROVICE) {
                if (busRegisterAreaProvice != null) {
                    if (!TextUtils.isEmpty(provices.get(position))) {
                        busRegisterAreaProvice.setText(provices.get(position));
                        selectCityId = proviceList.get(position).getId();
                    }
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

    @OnClick(R.id.title_left)
    void backActivity(View view){
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }
}
