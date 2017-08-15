package com.wanny.workease.system.framework_ui.customer_UI.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wanny.workease.system.R;
import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpFragment;
import com.wanny.workease.system.workease_business.business.send_work_mvp.SendWorkImpl;
import com.wanny.workease.system.workease_business.business.send_work_mvp.SendWorkInfoPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 文件名： UserCenterFragment
 * 功能：
 * 作者： wanny
 * 时间： 13:54 2017/6/23
 */
public class SendMessageFragment extends MvpFragment<SendWorkInfoPresenter> implements SendWorkImpl {


    @BindView(R.id.title_left)
    TextView titleLeft;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.send_work_projectname_edit)
    EditText sendWorkProjectnameEdit;
    @BindView(R.id.send_work_areaselect)
    TextView sendWorkAreaselect;
    @BindView(R.id.send_work_worktypeselect)
    TextView sendWorkWorktypeselect;
    @BindView(R.id.send_work_neednumber)
    TextView sendWorkNeednumber;
    @BindView(R.id.send_work_price_edit)
    EditText sendWorkPriceEdit;
    @BindView(R.id.send_work_detail)
    EditText sendWorkDetail;
    @BindView(R.id.send_work_location_edit)
    EditText sendWorkLocationEdit;
    @BindView(R.id.send_work_location_map)
    TextView sendWorkLocationMap;
    @BindView(R.id.send_work)
    TextView sendWork;
    @BindView(R.id.send_work_hascomplete)
    TextView sendWorkHascomplete;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.send_workinfo_activity_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void success(OrdinalResultEntity ordinalResultEntity) {

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
    protected SendWorkInfoPresenter createPresenter() {
        return new SendWorkInfoPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
