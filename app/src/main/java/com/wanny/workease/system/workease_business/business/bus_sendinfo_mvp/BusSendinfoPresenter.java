package com.wanny.workease.system.workease_business.business.bus_sendinfo_mvp;

import com.wanny.workease.system.framework_mvpbasic.BasePresenter;

/**
 * 文件名： BusSendinfoPresenter
 * 功能：
 * 作者： wanny
 * 时间： 20:04 2017/8/9
 */
public class BusSendinfoPresenter extends BasePresenter<BusSendinfoImpl> {

    public BusSendinfoPresenter(BusSendinfoImpl view){
        attachView(view);
    }
}
