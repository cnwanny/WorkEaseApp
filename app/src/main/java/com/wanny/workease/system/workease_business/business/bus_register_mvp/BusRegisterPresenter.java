package com.wanny.workease.system.workease_business.business.bus_register_mvp;

import com.wanny.workease.system.framework_mvpbasic.BasePresenter;

/**
 * 文件名： BusRegisterPresenter
 * 功能：
 * 作者： wanny
 * 时间： 20:04 2017/8/9
 */
public class BusRegisterPresenter extends BasePresenter<BusRegisterImpl> {

    public BusRegisterPresenter(BusRegisterImpl view){
        attachView(view);
    }
}
