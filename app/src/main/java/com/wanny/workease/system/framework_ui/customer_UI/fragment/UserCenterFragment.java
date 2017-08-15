package com.wanny.workease.system.framework_ui.customer_UI.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.AppUtils;
import com.wanny.workease.system.framework_basicutils.PreferenceUtil;
import com.wanny.workease.system.framework_care.ActivityStackManager;
import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpFragment;
import com.wanny.workease.system.framework_ui.customer_UI.activity.LoginActivity;
import com.wanny.workease.system.framework_ui.customer_UI.activity.ModifyUserActivity;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerInfo;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerInofResult;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerUserImpl;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerUserPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 文件名： MainSecondFragment
 * 功能：
 * 作者： wanny
 * 时间： 13:54 2017/6/23
 */
public class UserCenterFragment extends MvpFragment<CustomerUserPresenter> implements CustomerUserImpl {


    @BindView(R.id.title_left)
    TextView titleLeft;
    @BindView(R.id.title_title)
    TextView titleTitle;
    //昵称
    @BindView(R.id.user_center_name)
    TextView userCenterName;
    //电话号码
    @BindView(R.id.user_center_phone)
    TextView userCenterPhone;
    //用工状态
    @BindView(R.id.user_center_state)
    TextView userCenterState;
    //城市
    @BindView(R.id.user_center_city)
    TextView userCenterCity;
    //工种类型
    @BindView(R.id.user_center_worktype)
    TextView userCenterWorktype;
    //工作年限
    @BindView(R.id.user_center_workyear)
    TextView userCenterWorkyear;
    //技能熟练程度
    @BindView(R.id.user_center_skill)
    TextView userCenterSkill;
    //修改个人信息
    @BindView(R.id.user_info_modify)
    TextView userInfoModify;
    //退出登录
    @BindView(R.id.user_info_logout)
    TextView userInfoLogout;
    Unbinder unbinder;
    private String mobile = "";
    private String userId = "";
    private String name = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mobile = PreferenceUtil.getInstance(mContext).getString("mobile", "");
        userId = PreferenceUtil.getInstance(mContext).getString("userId", "");
        name = PreferenceUtil.getInstance(mContext).getString("name", "");
//        if (!TextUtils.isEmpty(userId)) {
//            name = mobile;
//        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cus_usercenter_fragment_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (titleLeft != null) {
            AppUtils.notShowView(titleLeft);
        }
//        if (mvpPresenter != null) {
//            mvpPresenter.getWorkType();
//        }
//        if (mvpPresenter != null) {
//            mvpPresenter.getCityValue();
//        }
        if (titleTitle != null) {
            titleTitle.setText("我的");
        }
        if (mvpPresenter != null) {
            mvpPresenter.getUserInfo(userId, "", "正在加载");
        }
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
                entity = customerInfo.getData();
                setData();
            }
        }
    }
    private CustomerInfo entity ;
    private void setData() {
        if (entity != null) {
            if (!TextUtils.isEmpty(entity.getUserName())) {
                userCenterName.setText(entity.getUserName());
            }
            if (!TextUtils.isEmpty(entity.getMobile())) {
                userCenterPhone.setText(entity.getMobile());
            }
            if (entity.getUserState() == 0) {
                userCenterState.setText("空闲");
            } else {
                userCenterState.setText("忙碌");
            }
            if (!TextUtils.isEmpty(entity.getSenior())) {
                userCenterSkill.setText(entity.getSenior());
            }
            if (!TextUtils.isEmpty(entity.getJobTypeName())) {
                userCenterWorktype.setText(entity.getJobTypeName());
            }
            if (!TextUtils.isEmpty(entity.getCityName())) {
                userCenterCity.setText(entity.getCityName());
            }
            if (!TextUtils.isEmpty(entity.getWorkyear())) {
                userCenterWorkyear.setText(entity.getWorkyear() + "年");
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

    @OnClick(R.id.user_info_modify)
    void startSave(View view) {
        Intent intent = new Intent(getActivity(), ModifyUserActivity.class);
        intent.putExtra("entity", entity);
        startActivityForResult(intent , 0x0002);
    }


    @OnClick(R.id.user_info_logout)
    void logout(View view) {
        PreferenceUtil.getInstance(mContext).saveString("mobile", "");
        PreferenceUtil.getInstance(mContext).saveString("name","");
        PreferenceUtil.getInstance(mContext).saveString("userId", "");
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }
}
