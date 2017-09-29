package com.wanny.workease.system.framework_ui.customer_UI.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.NewPremissionUtils;
import com.wanny.workease.system.framework_care.ActivityStackManager;
import com.wanny.workease.system.framework_care.AppContent;
import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpActivity;
import com.wanny.workease.system.framework_ui.business_UI.activity.LocationActivity;
import com.wanny.workease.system.framework_uikite.dialog.HiFoToast;
import com.wanny.workease.system.framework_uikite.dialog.MyDialog;
import com.wanny.workease.system.workease_business.customer.main_mvp.WorkInfoEntity;
import com.wanny.workease.system.workease_business.customer.work_detail.WorkDetailImpl;
import com.wanny.workease.system.workease_business.customer.work_detail.WorkDetailPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件名： WorkInfoDetailActivity
 * 功能：
 * 作者： wanny
 * 时间： 15:28 2017/8/3
 */
public class WorkInfoDetailActivity extends MvpActivity<WorkDetailPresenter> implements WorkDetailImpl {

    @BindView(R.id.title_left)
    TextView titleLeft;
    //
    @BindView(R.id.title_title)
    TextView titleTitle;
    //
    @BindView(R.id.work_detail_banner)
    ConvenientBanner workDetailBanner;
    //
    @BindView(R.id.work_detail_name)
    TextView workDetailName;
    //
    @BindView(R.id.work_detail_area)
    TextView workDetailArea;
    //
    @BindView(R.id.work_detail_type)
    TextView workDetailType;
    //
    @BindView(R.id.work_detail_number)
    TextView workDetailNumber;
    //
    @BindView(R.id.work_detail_price)
    TextView workDetailPrice;
    //
    @BindView(R.id.work_project_detail)
    TextView workProjectDetail;
    //
    @BindView(R.id.work_detail_location)
    TextView workDetailLocation;
    //
    @BindView(R.id.workd_contact_name)
    TextView workdContactName;
    //
    @BindView(R.id.work_contact_phone)
    TextView workContactPhone;
    //
    @BindView(R.id.work_detail_callwork)
    LinearLayout workDetailCallwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_detail_activity_view);
        ButterKnife.bind(this);
        initView();
    }

    private WorkInfoEntity entity;

    private void initView() {
        if (titleTitle != null) {
            titleTitle.setText("用工详情");
        }
        if (getIntent() != null) {
            if (getIntent().hasExtra("entity")) {
                entity = getIntent().getParcelableExtra("entity");
            }
        }
        if (entity != null) {
            if (!TextUtils.isEmpty(entity.getName())) {
                workDetailName.setText("楼盘名称：" + entity.getName());
            }
            if (!TextUtils.isEmpty(entity.getRecruitNum())) {
                workDetailNumber.setText(entity.getRecruitNum());
            }
            if (!TextUtils.isEmpty(entity.getPrice())) {
                workDetailPrice.setText(entity.getPrice());
            }
            if (entity.getCity() != null) {
                if (!TextUtils.isEmpty(entity.getCity().getName())) {
                    workDetailArea.setText(entity.getCity().getName());
                }
            }
            if (!TextUtils.isEmpty(entity.getDesc())) {
                workProjectDetail.setText("项目描述：" + entity.getDesc());
            }

            if (!TextUtils.isEmpty(entity.getDetailAddress())) {
                workDetailLocation.setText("项目位置：" + entity.getDetailAddress());
            }

            if (entity.getJobType() != null) {
                if (!TextUtils.isEmpty(entity.getJobType().getName())) {
                    workDetailType.setText(entity.getJobType().getName());
                }
            }
            if (entity.getUser() != null) {
                if (!TextUtils.isEmpty(entity.getUser().getUserName())) {
                    workdContactName.setText(entity.getUser().getUserName().substring(0,1) + "先生");
                }
                if (!TextUtils.isEmpty(entity.getUser().getMobile())) {
                    workContactPhone.setText(entity.getUser().getMobile());
                }
            }
        }
    }

    @Override
    public void success(OrdinalResultEntity ordinalResultEntity) {

    }


    @OnClick(R.id.title_left)
    void startBack(View view){
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }


    @OnClick(R.id.work_detail_callwork)
    void startCall(View view){
        if(entity != null && entity.getUser() != null){
            if(!TextUtils.isEmpty(entity.getUser().getMobile())){
                createCallphoneDialog("确定要给" + entity.getUser().getMobile() + "打电话吗");
            }else{
                new HiFoToast(mContext,"电话号码不能为空");
            }
        }else{
            new HiFoToast(mContext,"电话号码不能为空");
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


    private void startPhone() {
        if (hasPhone) {
            try {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + entity.getUser().getMobile()));
                startActivity(intent);
            } catch (SecurityException e) {
                e.printStackTrace();
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

    private MyDialog dialog;

    private void createCallphoneDialog(String content) {
        if (dialog == null) {
            dialog = new MyDialog(mActivity, R.style.dialog, content, "", mActivity);
            dialog.setClickListener(CallCustomerDialogListener);
            dialog.show();
        } else {
            if (dialog != null) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        }
    }

    //拨打电话。
    private MyDialog.ClickListenerInterface CallCustomerDialogListener = new MyDialog.ClickListenerInterface() {
        @Override
        public void cancel() {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        }

        @Override
        public void sure(String editdata, String pricecallback) {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
            requsetPhone();
        }
    };



    @Override
    public void fail(String errorMessage) {

    }

    @Override
    public void loadIng(String title) {

    }

    @Override
    public void hide() {

    }


    @OnClick(R.id.work_detail_location)
    void startLocation(View view){
        Intent intent = new Intent(WorkInfoDetailActivity.this, LocationActivity.class);
        intent.putExtra("isScan",true);
        LatLng latLng = new LatLng(entity.getPointLat(),entity.getPointLon());
        intent.putExtra("location",latLng);
        intent.putExtra("address",entity.getDetailAddress());
        startActivity(intent);
    }

    @Override
    protected WorkDetailPresenter createPresenter() {
        return new WorkDetailPresenter(this);
    }
}
