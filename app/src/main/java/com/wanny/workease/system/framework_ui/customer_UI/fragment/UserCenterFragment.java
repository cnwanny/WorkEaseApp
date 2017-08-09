package com.wanny.workease.system.framework_ui.customer_UI.fragment;

import android.os.Bundle;
import android.os.CpuUsageInfo;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.AppUtils;
import com.wanny.workease.system.framework_basicutils.PreferenceUtil;
import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpFragment;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerInfo;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerInofResult;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerUserImpl;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerUserPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 文件名： MainSecondFragment
 * 功能：
 * 作者： wanny
 * 时间： 13:54 2017/6/23
 */
public class UserCenterFragment extends MvpFragment<CustomerUserPresenter> implements CustomerUserImpl {

    //选择省份
    @BindView(R.id.register_area_provice)
    TextView registerAreaProvice;
    //选择地域
    @BindView(R.id.register_area)
    TextView registerArea;
    //类型选择
    @BindView(R.id.register_typeselect)
    TextView registerTypeselect;
    //工作时长
    @BindView(R.id.register_workertime)
    EditText registerWorkertime;
    //工作技能熟练程度选择
    @BindView(R.id.register_skilllevelselect)
    TextView registerSkilllevelselect;
    Unbinder unbinder;
    //返回操作
    @BindView(R.id.title_left)
    TextView titleLeft;
    //标题
    @BindView(R.id.title_title)
    TextView titleTitle;
    //昵称编辑
    @BindView(R.id.user_center_name_edit)
    EditText userCenterNameEdit;
    //用工状态选择
    @BindView(R.id.user_center_state_select)
    TextView userCenterStateSelect;
    //修改个人信息
    @BindView(R.id.user_info_modify)
    TextView userInfoModify;
    //电话号码
    @BindView(R.id.user_center_phone_edit)
    EditText userCenterPhoneEdit;


    private String mobile = "";
    private String userId = "";
    private String name = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mobile = PreferenceUtil.getInstance(mContext).getString("mobile", "");
        userId = PreferenceUtil.getInstance(mContext).getString("userId", "");
        name = PreferenceUtil.getInstance(mContext).getString("name", "");
        if (TextUtils.isEmpty(name)) {
            name = mobile;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.usercenter_fragment_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (titleLeft != null) {
            AppUtils.notShowView(titleLeft);
        }
        if(mvpPresenter != null){
            mvpPresenter.getWorkType();
        }
        if(mvpPresenter != null){
            mvpPresenter.getCityValue();
        }
        if (titleTitle != null) {
            titleTitle.setText("个人资料");
        }
        if (mvpPresenter != null) {
            mvpPresenter.getUserInfo(userId, "", "正在加载");
        }
        setInitView();
    }

    private void setInitView() {
        registerAreaProvice.setEnabled(false);
        registerArea.setEnabled(false);
        userCenterNameEdit.setEnabled(false);
        userCenterStateSelect.setEnabled(false);
        userCenterPhoneEdit.setEnabled(false);
        registerSkilllevelselect.setEnabled(false);
        registerWorkertime.setEnabled(false);
        registerTypeselect.setEnabled(false);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void success(CustomerInofResult customerInfo) {
        if (customerInfo.isSuccess()) {
            if (customerInfo.getData() != null) {
                setData(customerInfo.getData());
            }
        }
    }


    private void setData(CustomerInfo info) {
        if (info != null) {
            if (!TextUtils.isEmpty(info.getUserName())) {
                userCenterNameEdit.setText(info.getUserName());
            }

            if (!TextUtils.isEmpty(info.getMobile())) {
                userCenterPhoneEdit.setText(info.getMobile());
            }
            if (info.getUserState() == 0) {
                userCenterStateSelect.setText("空闲");
            }else{
                userCenterStateSelect.setText("忙碌");
            }
            if (!TextUtils.isEmpty(info.getSenior())) {
                registerSkilllevelselect.setText(info.getSenior());
            }
            if (!TextUtils.isEmpty(info.getUserName())) {
                userCenterNameEdit.setText(info.getUserName());
            }
            if (!TextUtils.isEmpty(info.getUserName())) {
                userCenterNameEdit.setText(info.getUserName());
            }
            if (!TextUtils.isEmpty(info.getUserName())) {
                userCenterNameEdit.setText(info.getUserName());
            }
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
    public void modifySuccess(OrdinalResultEntity ordinalResultEntity) {

    }

    @Override
    protected CustomerUserPresenter createPresenter() {
        return new CustomerUserPresenter(this);
    }

    @Override
    public void workType(WorkTypeResult entity) {

    }

    @Override
    public void getCityValue(CityResult cityResult) {

    }
}
