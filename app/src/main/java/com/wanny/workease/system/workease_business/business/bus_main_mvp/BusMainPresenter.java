package com.wanny.workease.system.workease_business.business.bus_main_mvp;

import com.wanny.workease.system.framework_mvpbasic.BasePresenter;

/**
 * 文件名： BusMainPresenter
 * 功能：
 * 作者： wanny
 * 时间： 20:03 2017/8/9
 */
public class BusMainPresenter extends BasePresenter<BusMainImpl> {

    public BusMainPresenter(BusMainImpl view){
        attachView(view);
    }
}
