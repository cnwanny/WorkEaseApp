package com.wanny.workease.system.framework_ui.business_UI.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.AppUtils;
import com.wanny.workease.system.framework_basicutils.PreferenceUtil;
import com.wanny.workease.system.framework_care.ActivityStackManager;
import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpFragment;
import com.wanny.workease.system.framework_ui.business_UI.activity.BusLoginActivity;
import com.wanny.workease.system.framework_ui.business_UI.activity.ModifyInfoActivity;
import com.wanny.workease.system.framework_ui.business_UI.activity.MySendWorkListActivity;
import com.wanny.workease.system.framework_uikite.WaitDialog;
import com.wanny.workease.system.framework_uikite.dialog.MyDialog;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerInofResult;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerUserImpl;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerUserPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 文件名： BusUserCenterFragment
 * 功能：
 * 作者： wanny
 * 时间： 17:49 2017/8/8
 */
public class BusUserCenterFragment extends MvpFragment<CustomerUserPresenter> implements CustomerUserImpl {


    @BindView(R.id.tv_moblie_value)
    TextView tvMoblieValue;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.center_mysendwork_rel)
    RelativeLayout center_mysendwork_rel;
    @BindView(R.id.tv_area_value)
    TextView tvAreaValue;
    @BindView(R.id.bus_usercenter_name)
    TextView busUsercenterName;
    @BindView(R.id.bus_usercenter_modifyinfo)
    TextView busUsercenterModifyinfo;

    @BindView(R.id.bus_usercenter_logout)
    TextView busUsercenterLogout;


    Unbinder unbinder;
    private String mobile = "";
    private String userId = "";
    private String name = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.boss_frgament_main_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void success(CustomerInofResult customerInofResult) {
        if (customerInofResult.isSuccess()) {
            PreferenceUtil.getInstance(mContext).saveString("busmobile", customerInofResult.getData().getMobile());
            PreferenceUtil.getInstance(mContext).saveString("busname", customerInofResult.getData().getUserName());
            mobile = PreferenceUtil.getInstance(mContext).getString("busmobile", "");
            userId = PreferenceUtil.getInstance(mContext).getString("bususerId", "");
            name = PreferenceUtil.getInstance(mContext).getString("busname", "");
            if (!TextUtils.isEmpty(name)) {
                busUsercenterName.setText(name);
            }
            if (!TextUtils.isEmpty(mobile)) {
                tvMoblieValue.setText(mobile);
            }
            if (!TextUtils.isEmpty(customerInofResult.getData().getCityName())) {
                tvAreaValue.setText(customerInofResult.getData().getCityName());
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mobile = PreferenceUtil.getInstance(mContext).getString("busmobile", "");
        userId = PreferenceUtil.getInstance(mContext).getString("bususerId", "");
        name = PreferenceUtil.getInstance(mContext).getString("busname", "");
        if (!TextUtils.isEmpty(name)) {
            busUsercenterName.setText(name);
        }
        if (!TextUtils.isEmpty(mobile)) {
            tvMoblieValue.setText(mobile);
        }
        if (mvpPresenter != null) {
            mvpPresenter.getUserInfo(userId, "", "正在加载");
        }
    }


    @OnClick(R.id.bus_usercenter_modifyinfo)
    void startModify(View view) {
        Intent intent = new Intent(getActivity(), ModifyInfoActivity.class);
        startActivityForResult(intent, 0x0001);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void fail(String errorMessage) {
        if(waitDialog != null){
            if(waitDialog.isShowing()){
                waitDialog.dismiss();
                waitDialog = null;
            }
        }
    }

    @Override
    public void loadIng(String title) {
        createWait(title);
    }

    @Override
    public void hide() {
        if(waitDialog != null){
                if(waitDialog.isShowing()){
                    waitDialog.dismiss();
                    waitDialog = null;
                }
        }
    }


    @OnClick(R.id.center_mysendwork_rel)
    void startMySend(View view) {
        Intent intent = new Intent(getActivity(), MySendWorkListActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.bus_usercenter_logout)
    void startLogout(View view) {
        createMyDialog();
    }


    private MyDialog myDialog;


    private void createMyDialog() {
        if (myDialog == null) {
            myDialog = new MyDialog(mActivity, R.style.dialog, "确定退出该账号吗？", "", mActivity);
            myDialog.setClickListener(clickListenerInterface);
            myDialog.show();
        } else {
            if (!myDialog.isShowing()) {
                myDialog.show();
            }
        }

    }


    @Override
    public void logout(OrdinalResultEntity entity) {
        if(waitDialog != null){
            if(waitDialog.isShowing()){
                waitDialog.dismiss();
                waitDialog = null;
            }
        }
        if(entity.isSuccess()){
            PreferenceUtil.getInstance(mContext).saveString("busmobile", "");
            PreferenceUtil.getInstance(mContext).saveString("busname", "");
            PreferenceUtil.getInstance(mContext).saveString("bususerId", "");
            Intent intent = new Intent(getActivity(), BusLoginActivity.class);
            startActivity(intent);
            ActivityStackManager.getInstance().exitActivity(mActivity);
        }
    }

    private MyDialog.ClickListenerInterface clickListenerInterface = new MyDialog.ClickListenerInterface() {
        @Override
        public void cancel() {
            if (myDialog != null) {
                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                    myDialog = null;
                }
            }
        }

        @Override
        public void sure(String editdata, String pricecallback) {
            if (myDialog != null) {
                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                    myDialog = null;
                }
            }
            String pushToken = PreferenceUtil.getInstance(mContext).getString("channId","");
            if (!TextUtils.isEmpty(pushToken)) {
                mvpPresenter.logout(pushToken,"请稍等");
            }else{
                PreferenceUtil.getInstance(mContext).saveString("busmobile", "");
                PreferenceUtil.getInstance(mContext).saveString("busname", "");
                PreferenceUtil.getInstance(mContext).saveString("bususerId", "");
                Intent intent = new Intent(getActivity(), BusLoginActivity.class);
                startActivity(intent);
                ActivityStackManager.getInstance().exitActivity(mActivity);
            }
        }
    };

    private WaitDialog waitDialog;
    private void createWait(String  titlename){
        if(waitDialog == null){
            waitDialog = new WaitDialog(mActivity,R.style.wait_dialog,titlename);
            waitDialog.show();
        }
        if(waitDialog != null){
            waitDialog.show();
        }
    }


    @Override
    public void modifySuccess(OrdinalResultEntity ordinalResultEntity) {

    }

    @Override
    public void workType(WorkTypeResult entity) {

    }


    //创建对象
    @Override
    protected CustomerUserPresenter createPresenter() {
        return new CustomerUserPresenter(this);
    }

    @Override
    public void getCityValue(CityResult cityResult) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
