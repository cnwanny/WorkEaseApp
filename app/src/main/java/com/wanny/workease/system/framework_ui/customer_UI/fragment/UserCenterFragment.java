package com.wanny.workease.system.framework_ui.customer_UI.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_basicutils.PreferenceUtil;
import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpFragment;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerInfo;
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


    @BindView(R.id.register_area_provice)
    TextView registerAreaProvice;
    @BindView(R.id.register_area)
    TextView registerArea;
    @BindView(R.id.register_typeselect)
    TextView registerTypeselect;
    @BindView(R.id.register_workertime_index)
    TextView registerWorkertimeIndex;
    @BindView(R.id.register_workertime)
    EditText registerWorkertime;
    @BindView(R.id.register_skilllevelselect)
    TextView registerSkilllevelselect;
    Unbinder unbinder;
    @BindView(R.id.register_type)
    TextView registerType;
    @BindView(R.id.register_skilllevel)
    TextView registerSkilllevel;
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
    //
    @BindView(R.id.register_workertime_lab)
    TextView registerWorkertimeLab;


    //修改个人信息
    @BindView(R.id.user_info_modify)
    TextView userInfoModify;



    private String mobile = "";
    private String userId = "";
    private String name = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mobile = PreferenceUtil.getInstance(mContext).getString("mobile","");
        userId = PreferenceUtil.getInstance(mContext).getString("userId","");
        name = PreferenceUtil.getInstance(mContext).getString("name","");
        if(TextUtils.isEmpty(name)){
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
        if(mvpPresenter != null){
            mvpPresenter.getUserInfo(userId, name,"正在加载");
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void success(CustomerInfo customerInfo) {

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
}
