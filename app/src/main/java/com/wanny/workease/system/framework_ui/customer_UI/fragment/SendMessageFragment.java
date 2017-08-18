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



//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.send_workinfo_activity_view, container, false);
//        return view;
//    }

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
    }
}
