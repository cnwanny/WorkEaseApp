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
import com.wanny.workease.system.framework_basicutils.LogUtil;
import com.wanny.workease.system.framework_basicutils.PreferenceUtil;
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
import com.wanny.workease.system.workease_business.customer.register_mvp.WoryTypeEntity;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件名： RegisterActivity
 * 功能：
 * 作者： wanny
 * 时间： 9:29 2017/4/6
 */
public class RegisterActivity extends MvpActivity<RegisterPresenter> implements RegisterImpl {

    @BindView(R.id.title_left)
    TextView titleLeft;

    @BindView(R.id.title_title)
    TextView titleTitle;

    @BindView(R.id.register_phone)
    EditText registerPhone;

    @BindView(R.id.register_password)
    EditText registerPassword;

    @BindView(R.id.register_sure_password)
    EditText registerSurePassword;

    //省选择
    @BindView(R.id.register_area_provice)
    TextView registerProvice;
    //市选择
    @BindView(R.id.register_area)
    TextView registerArea;

    @BindView(R.id.register_typeselect)
    TextView registerTypeselect;

    @BindView(R.id.register_workertime)
    EditText registerWorkertime;

    @BindView(R.id.register_skilllevelselect)
    TextView registerSkilllevelselect;

    @BindView(R.id.start_register)
    TextView startRegister;
   //注册
    @BindView(R.id.register_username)
    TextView registerUsername;
    private String code = "";

//    @Override
//    public void success(OrdinalResultEntity s) {
//        if(s.isState()){
//            if(!TextUtils.isEmpty(s.getData())){
//                code = s.getData();
//                new HiFoToast(mContext,"验证码发送成功,请注意查收");
//            }
//        }else{
//            if(!TextUtils.isEmpty(s.getMsg())){
//                new HiFoToast(mContext,s.getMsg());
//            }
//            if(!TextUtils.isEmpty(s.getData().toString())){
//                PreferenceUtil.getInstance(mContext).saveBoolean("isLogin",false);
//            }
//        }
//    }

    @Override
    public void fail(String errorMessage) {
        LogUtil.log("log==", errorMessage);
        if (!TextUtils.isEmpty(errorMessage) && errorMessage.equals("103")) {
            new HiFoToast(mContext, "认证失败,重新登录");
            Intent intent = new Intent(mActivity, LoginActivity.class);
            PreferenceUtil.getInstance(mContext).saveBoolean("isLogin", false);
            intent.putExtra("type", "token");
            startActivity(intent);
        }
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }

    private ArrayList<String> level = new ArrayList<>();
    private ArrayList<String> work_type = new ArrayList<>();
    private ArrayList<String> provices = new ArrayList<>();
    private ArrayList<String> areas = new ArrayList<>();

    private ArrayList<WoryTypeEntity> workTypeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_view);
        ButterKnife.bind(this);
        if (titleTitle != null) {
            titleTitle.setText("注册");
        }
        initView();
//        String[] wrokType  = getResources().getStringArray(R.array.work_type);
//        work_type.addAll(Arrays.asList(wrokType));


    }

    private void initView(){
        if(workTypeList == null){
            workTypeList = new ArrayList<>();
        }
        if(proviceList == null){
            proviceList = new ArrayList<>();
        }
        if(areadList == null){
            areadList = new ArrayList<>();
        }
        String[] levelRank = getResources().getStringArray(R.array.leaveRank);
        level.addAll(Arrays.asList(levelRank));
        if(mvpPresenter != null){
            mvpPresenter.getWorkType();
        }
        if(mvpPresenter != null){
            mvpPresenter.getCityValue();
        }
    }


    @Override
    public void workType(WorkTypeResult entity) {
        if(entity.isSuccess()){
            workTypeList.clear();
            work_type.clear();
            workTypeList.addAll(entity.getData());
            for(WoryTypeEntity value : workTypeList){
               work_type.add(value.getName());
            }
        }
    }




    @OnClick(R.id.register_area_provice)
    void startSelectProvice(View view){
        mode = MODE_PROVICE;
        provices.clear();
        for(CityEntity entity : proviceList){
            provices.add(entity.getName());
        }
        createIOS(provices,"选择省/市");
    }

    private String selectCityId = "";

    @OnClick(R.id.register_area)
    void startSelectArea(View view){
        mode = MODE_AREA;
        areas.clear();
        areadList.clear();
        for(CityEntity entity : proviceList){
            if(entity.getId().equals(selectCityId)){
                  areadList.addAll(entity.getSubCitys());
                break;
            }
        }
        for(City  entity : areadList){
            areas.add(entity.getName());
        }
        createIOS(areas,"选择市/区");
    }




    @OnClick(R.id.title_left)
    void backActivity(View view) {
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }

//    private boolean runningThree = false;
//
//    private CountDownTimer downTimer = new CountDownTimer(60 * 1000, 1000) {
//        @Override
//        public void onTick(long l) {
//            runningThree = true;
//            registerCheckcode.setText((l / 1000) + "秒");
//        }
//
//        @Override
//        public void onFinish() {
//            runningThree = false;
//            registerCheckcode.setText("重新发送");
//        }
//    };


    @OnClick(R.id.start_register)
    void startRegister(View view) {
        if (!hasRunning) {
            hasRunning = true;
            startRegister();
        }
    }


//    @OnClick(R.id.register_checkcode)
//    void startSendCode(View view){
//        startSendCode();
//    }
//


//    private void startSendCode(){
//        if(!TextUtils.isEmpty(registerPhone.getText().toString()) && AppUtils.isPhone(registerPhone.getText().toString())){
//
//        }else{
//            new HiFoToast(mContext,"请输入电话号码");
//            return ;
//        }
//        if (runningThree) {
//            return;
//        } else {
//            if(mvpPresenter != null){
//                String phone = registerPhone.getText().toString();
//                Map<String, String> map = new HashMap<>();
//                map.put("PhoneNumber", phone);
//                map.put("CodeType", "1");
//                map.put("key", "aFkeieD65dDtgUYod4dDeo98dde" + "#" + "dSDAS");
//                String nosign = MD5Utils.formatUrlMap(map, false, false);
//                String sign = MD5Utils.MD5Encode(nosign, "utf-8", false);
//                mvpPresenter.getCode(phone, 1, sign,"正在注册");
//            }
//            downTimer.start();
//        }
//
//    }

    private boolean hasRunning = false;
    private void startRegister() {
        if (TextUtils.isEmpty(registerPhone.getText().toString())) {
            new HiFoToast(mContext, "请输入电话号码");
            hasRunning = false;
            return;
        }
        if (TextUtils.isEmpty(registerPassword.getText().toString())) {
            new HiFoToast(mContext, "请输入密码");
            hasRunning = false;
            return;

        }
        if (mvpPresenter != null) {
            mvpPresenter.register(registerPhone.getText().toString(),registerPassword.getText().toString(),"0",registerUsername.getText().toString() ,selectAreaId,selectWorkTypeId,registerSkilllevelselect.getText().toString());
        }
    }

    @Override
    public void registerSuccess(RegisterResult entity) {
        hasRunning = false;
        if (entity.isSuccess()) {
            if (!TextUtils.isEmpty(entity.getMsg())) {
                new HiFoToast(mContext, entity.getMsg() + ",请登录");
                Intent intent = new Intent();
                intent.putExtra("phone", registerPhone.getText().toString());
                setResult(0x0003, intent);
                ActivityStackManager.getInstance().exitActivity(mActivity);
            }
        } else {
            if (!TextUtils.isEmpty(entity.getMsg())) {
                new HiFoToast(mContext, entity.getMsg());
            }
        }
    }

    //点击选择工种
    @OnClick(R.id.register_typeselect)
     void setWorkTypeSelect(View view){
        mode = MODE_WORKTYPE;
        currentList.clear();
        currentList.addAll(work_type);
        createIOS(currentList,"选择工种");
    }



    //既能熟练程度选择
    @OnClick(R.id.register_skilllevelselect)
    void selectLevel(View view){
        mode = MODE_LEVEL;
        currentList.clear();
        currentList.addAll(level);
        createIOS(currentList,"选择技能熟练程度");
    }


    private ArrayList<CityEntity> proviceList;
    private ArrayList<City> areadList;
    @Override
    public void getCityValue(CityResult cityResult) {
        if(cityResult.isSuccess()){
             if(cityResult.getData() != null && cityResult.getData().size() > 0){
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
        if(!mActivity.isFinishing()){
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
    public static final int MODE_WORKTYPE= 0x0001;
    //等级
    public static final int MODE_LEVEL = 0x0002;
    //选择省
    public static final int MODE_PROVICE = 0x0003;
    //选择市
    public static final int MODE_AREA = 0x0004;


   private String selectAreaId = "";
    private int mode = MODE_WORKTYPE;
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
                if (registerSkilllevelselect != null) {
                    if (!TextUtils.isEmpty(currentList.get(position))) {
                        registerSkilllevelselect.setText(currentList.get(position));
                    }
                }
            } else if (mode == MODE_WORKTYPE) {
                if (registerTypeselect != null) {
                    if (!TextUtils.isEmpty(currentList.get(position))) {
                        registerTypeselect.setText(currentList.get(position));
                        selectWorkTypeId = workTypeList.get(position).getId();
                    }
                }
            }else if(mode == MODE_AREA){
                if (registerArea != null) {
                    if (!TextUtils.isEmpty(areas.get(position))) {
                        registerArea.setText(areas.get(position));
                        selectAreaId = areadList.get(position).getId();
                    }
                }
            }else if(mode == MODE_PROVICE ){
                if (registerProvice != null) {
                    if (!TextUtils.isEmpty(provices.get(position))) {
                        registerProvice.setText(provices.get(position));
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
}
