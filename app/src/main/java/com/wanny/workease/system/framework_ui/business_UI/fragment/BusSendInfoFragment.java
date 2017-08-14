package com.wanny.workease.system.framework_ui.business_UI.fragment;

import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.MvpFragment;
import com.wanny.workease.system.workease_business.business.bus_sendinfo_mvp.BusSendinfoImpl;
import com.wanny.workease.system.workease_business.business.bus_sendinfo_mvp.BusSendinfoPresenter;

/**
 * 文件名： BusSendInfoFragment
 * 功能：
 * 作者： wanny
 * 时间： 17:49 2017/8/8
 */
public class BusSendInfoFragment extends MvpFragment<BusSendinfoPresenter> implements BusSendinfoImpl {

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
    protected BusSendinfoPresenter createPresenter() {
        return null;
    }
}
