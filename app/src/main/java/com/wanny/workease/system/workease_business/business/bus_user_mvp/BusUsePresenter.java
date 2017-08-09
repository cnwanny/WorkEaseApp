package com.wanny.workease.system.workease_business.business.bus_user_mvp;

import com.wanny.workease.system.framework_mvpbasic.BasePresenter;

/**
 * 文件名： BusUsePresenter
 * 功能：
 * 作者： wanny
 * 时间： 20:05 2017/8/9
 */
public class BusUsePresenter extends BasePresenter<BusUserImpl> {

    public BusUsePresenter(BusUserImpl view){
        attachView(view);
    }
}
