package com.wanny.workease.system.workease_business.business.bus_login_mvp;

import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.BaseOperateImp;
import com.wanny.workease.system.workease_business.customer.login_mvp.LoginResult;

/**
 * 文件名： BusLoginImpl
 * 功能：
 * 作者： wanny
 * 时间： 20:02 2017/8/9
 */
public interface BusLoginImpl extends BaseOperateImp<LoginResult> {
    void getServicePhone(ServicePhoneResult entity);
}
