package com.wanny.workease.system.framework_ui.customer_UI.activity;

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
import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpActivity;
import com.wanny.workease.system.framework_ui.business_UI.activity.ModifyInfoActivity;
import com.wanny.workease.system.framework_ui.business_UI.activity.ModifyPsdActivity;
import com.wanny.workease.system.framework_ui.business_UI.activity.ModifyWorkActivity;
import com.wanny.workease.system.framework_uikite.WaitDialog;
import com.wanny.workease.system.framework_uikite.dialog.HiFoToast;
import com.wanny.workease.system.framework_uikite.dialog.IOSDialogView;
import com.wanny.workease.system.workease_business.customer.modify_user_mvp.ModifyUserImpl;
import com.wanny.workease.system.workease_business.customer.modify_user_mvp.ModifyUserinfoPresenter;
import com.wanny.workease.system.workease_business.customer.register_mvp.City;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityEntity;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WoryTypeEntity;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerInfo;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerInofResult;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件名：ModifyUserActivity
 * 功能： 工人端修改个人用户信息
 * 作者： wanny
 * 时间： 11:02 2017/8/15
 */
public class ModifyUserActivity extends MvpActivity<ModifyUserinfoPresenter> implements ModifyUserImpl {


    @BindView(R.id.title_left)
    TextView titleLeft;
    @BindView(R.id.title_title)
    TextView titleTitle;

    @BindView(R.id.cus_modify_name_edit)
    EditText cusModifyNameEdit;
    @BindView(R.id.cus_modify_phone_edit)
    EditText cusModifyPhoneEdit;
//    @BindView(R.id.cus_modify_state_select)
//    TextView cusModifyStateSelect;
    @BindView(R.id.cus_modify_provice)
    TextView cusModifyProvice;
//    @BindView(R.id.cus_modify_area)
//    TextView cusModifyArea;
    @BindView(R.id.cus_modify_typeselect)
    TextView cusModifyTypeselect;
    @BindView(R.id.register_workertime)
    EditText registerWorkertime;
    @BindView(R.id.cus_modify_skilllevelselect)
    TextView cusModifySkilllevelselect;



    @BindView(R.id.cus_modify_user_password)
    TextView cusModifyUserPassword;

    @BindView(R.id.cus_modify_save)
    TextView cusModifySave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_user_activity_view);
        ButterKnife.bind(this);
        initView();
    }

    private CustomerInfo entity;

    private void initView() {
        if (getIntent() != null) {
            if (getIntent().hasExtra("entity")) {
                entity = getIntent().getParcelableExtra("entity");
            }
        }
        if(titleTitle != null){
            titleTitle.setText("修改个人资料");
        }
        if (entity != null) {
            if (!TextUtils.isEmpty(entity.getWorkyear())) {
                registerWorkertime.setText(entity.getWorkyear());
                registerWorkertime.setSelection(entity.getWorkyear().length());

            }
            if (!TextUtils.isEmpty(entity.getCityName())) {

            }
            if (!TextUtils.isEmpty(entity.getJobTypeName())) {
                cusModifyTypeselect.setText(entity.getJobTypeName());
            }
//            if (entity.getUserState() == 1) {
//                cusModifyStateSelect.setText("忙碌");
//            } else if (entity.getUserState() == 0) {
//                cusModifyStateSelect.setText("空闲");
//            }
            if (!TextUtils.isEmpty(entity.getSenior())) {
                cusModifySkilllevelselect.setText(entity.getSenior());
            }
            if (!TextUtils.isEmpty(entity.getUserName())) {
                cusModifyNameEdit.setText(entity.getUserName());
                cusModifyNameEdit.setSelection(entity.getUserName().length());
            }
            if (!TextUtils.isEmpty(entity.getMobile())) {
                cusModifyPhoneEdit.setText(entity.getMobile());
            }
            if (!TextUtils.isEmpty(entity.getCityName())) {
                cusModifyProvice.setText(entity.getCityName());
            }
        }
        if (workTypeList == null) {
            workTypeList = new ArrayList<>();
        }
        if (proviceList == null) {
            proviceList = new ArrayList<>();
        }
        if (areadList == null) {
            areadList = new ArrayList<>();
        }

        //技能等级
        String[] levelRank = getResources().getStringArray(R.array.leaveRank);
        level.addAll(Arrays.asList(levelRank));
        //用工状态
        String[] states = getResources().getStringArray(R.array.stateList);
        statList.addAll(Arrays.asList(states));

        if (mvpPresenter != null) {
            mvpPresenter.getWorkType();
        }
        if (mvpPresenter != null) {
            mvpPresenter.getCityValue();
        }
    }


    private ArrayList<String> level = new ArrayList<>();
    private ArrayList<String> work_type = new ArrayList<>();
    private ArrayList<String> provices = new ArrayList<>();
    private ArrayList<String> areas = new ArrayList<>();
    private ArrayList<String> statList = new ArrayList<>();

    private ArrayList<WoryTypeEntity> workTypeList;


    @Override
    protected ModifyUserinfoPresenter createPresenter() {
        return new ModifyUserinfoPresenter(this);
    }

    @Override
    public void modifySuccess(CustomerInofResult entity) {
        if (entity.isSuccess()) {
            setResult(0x0002);
            ActivityStackManager.getInstance().exitActivity(mActivity);
        } else {
            if(!TextUtils.isEmpty(entity.getMsg())){
                new HiFoToast(mContext,entity.getMsg());
            }else{
                new HiFoToast(mContext,"修改失败");
            }
        }
    }


    @OnClick(R.id.title_left)
    void backActivity(View view){
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }

    @Override
    public void workType(WorkTypeResult entity) {
        if (entity.isSuccess()) {
            workTypeList.clear();
            work_type.clear();
            workTypeList.addAll(entity.getData());
            for (WoryTypeEntity value : workTypeList) {
                work_type.add(value.getName());
            }
        }
    }


    //选择省份
    @OnClick(R.id.cus_modify_provice)
    void startSelectProvice(View view) {
        mode = MODE_PROVICE;
        provices.clear();
        for (CityEntity entity : proviceList) {
            provices.add(entity.getName());
        }
        createIOS(provices, "选择省/市");
    }


    @OnClick(R.id.cus_modify_user_password)
    void modifyPsd(View view) {
        Intent intent = new Intent(ModifyUserActivity.this , ModifyPsdActivity.class);
        intent.putExtra("mode", ModifyPsdActivity.MODE_CUSTOMRE);
        startActivity(intent);
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }


    private String selectCityId = "";

//    @OnClick(R.id.cus_modify_area)
//    void startSelectArea(View view) {
//        mode = MODE_AREA;
//        areas.clear();
//        areadList.clear();
//        for (CityEntity entity : proviceList) {
//            if (entity.getId().equals(selectCityId)) {
//                areadList.addAll(entity.getSubCitys());
//                break;
//            }
//        }
//        for (City entity : areadList) {
//            areas.add(entity.getName());
//        }
//        createIOS(areas, "选择市/区");
//    }


    //点击选择工种
    @OnClick(R.id.cus_modify_typeselect)
    void setWorkTypeSelect(View view) {
        mode = MODE_WORKTYPE;
        currentList.clear();
        currentList.addAll(work_type);
        createIOS(currentList, "选择工种");
    }


    //既能熟练程度选择
    @OnClick(R.id.cus_modify_skilllevelselect)
    void selectLevel(View view) {
        mode = MODE_LEVEL;
        currentList.clear();
        currentList.addAll(level);
        createIOS(currentList, "选择技能熟练程度");
    }


//    //既能熟练程度选择
//    @OnClick(R.id.cus_modify_state_select)
//    void selectStatus(View view) {
//        mode = MODE_STATE;
//        currentList.clear();
//        currentList.addAll(statList);
//        createIOS(currentList, "选择当前的用工状态");
//    }


    private ArrayList<CityEntity> proviceList;
    private ArrayList<City> areadList;

    @Override
    public void getCityValue(CityResult cityResult) {
        if (cityResult.isSuccess()) {
            if (cityResult.getData() != null && cityResult.getData().size() > 0) {
                proviceList.addAll(cityResult.getData());
            }
        }
    }

    @Override
    public void success(OrdinalResultEntity ordinalResultEntity) {

    }


    private ArrayList<String> currentList = new ArrayList<>();
    private WaitDialog waitDialog;

    private void waitDialog(String loading) {
        waitDialog = new WaitDialog(mActivity, R.style.wait_dialog, loading);
        if (!mActivity.isFinishing()) {
            waitDialog.show();
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


    private IOSDialogView iosDialogView;
    //工种
    public static final int MODE_WORKTYPE = 0x0001;
    //等级
    public static final int MODE_LEVEL = 0x0002;
    //选择省
    public static final int MODE_PROVICE = 0x0003;
    //选择市
    public static final int MODE_AREA = 0x0004;
    //用工状态
    public static final int MODE_STATE = 0x0005;

//    private String selectAreaId = "";
    private int mode = MODE_WORKTYPE;

    private void createIOS(ArrayList<String> data, String titlename) {
        if (iosDialogView == null) {
            iosDialogView = new IOSDialogView(mActivity, R.style.dialog, data, titlename,0);
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

    private String selectWorkTypeId = "";

    private IOSDialogView.IosDialogSelectListener iosDialogSelectListener = new IOSDialogView.IosDialogSelectListener() {
        @Override
        public void onItemClick(int position) {
            if (iosDialogView != null) {
                if (iosDialogView.isShowing()) {
                    iosDialogView.dismiss();
                    iosDialogView = null;
                }
            }
            if (mode == MODE_LEVEL) {
                if (cusModifySkilllevelselect != null) {
                    if (!TextUtils.isEmpty(currentList.get(position))) {
                        cusModifySkilllevelselect.setText(currentList.get(position));
                    }
                }
            } else if (mode == MODE_WORKTYPE) {
                if (cusModifyTypeselect != null) {
                    if (!TextUtils.isEmpty(currentList.get(position))) {
                        cusModifyTypeselect.setText(currentList.get(position));
                        selectWorkTypeId = workTypeList.get(position).getId();
                    }
                }
            }
//            else if (mode == MODE_AREA) {
//                if (cusModifyArea != null) {
//                    if (!TextUtils.isEmpty(areas.get(position))) {
//                        cusModifyArea.setText(areas.get(position));
//                        selectAreaId = areadList.get(position).getId();
//                    }
//                }
//            }
            else if (mode == MODE_PROVICE) {
                if (cusModifyProvice != null) {
                    if (!TextUtils.isEmpty(provices.get(position))) {
                        cusModifyProvice.setText(provices.get(position));
                        selectCityId = proviceList.get(position).getId();
                    }
                }
            } else if (mode == MODE_STATE) {
//                if (cusModifyStateSelect != null) {
//                    if (!TextUtils.isEmpty(statList.get(position))) {
//                        cusModifyStateSelect.setText(statList.get(position));
//                    }
//                }
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

    @Override
    public void fail(String errorMessage) {
        if (!TextUtils.isEmpty(errorMessage) && errorMessage.equals("103")) {
            new HiFoToast(mContext, "认证失败,重新登录");
            Intent intent = new Intent(mActivity, LoginActivity.class);
            PreferenceUtil.getInstance(mContext).saveBoolean("isLogin", false);
            intent.putExtra("type", "token");
            startActivity(intent);
        }
    }


    @OnClick(R.id.cus_modify_save)
    void save(View view) {
        if (mvpPresenter != null) {
            String state = "0";
//            if (!TextUtils.isEmpty(cusModifyStateSelect.getText().toString())) {
//                if (cusModifyStateSelect.getText().toString().equals("空闲")) {
//                    state = "0";
//                } else {
//                    state = "1";
//                }
//            }
            mvpPresenter.modifyUserInfo(entity.getUserId(), cusModifyNameEdit.getText().toString(), cusModifyPhoneEdit.getText().toString(), state, selectCityId, selectWorkTypeId, cusModifySkilllevelselect.getText().toString(), registerWorkertime.getText().toString(), "正在保存");
        }
    }
}
