package com.wanny.workease.system.workease_business.customer.user_mvp;

import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.BaseOperateImp;

/**
 * 文件名： CustomerUserImpl
 * 功能：
 * 作者： wanny
 * 时间： 15:10 2017/8/3
 */
public interface CustomerUserImpl extends BaseOperateImp<CustomerInfo>{


    void modifySuccess(OrdinalResultEntity ordinalResultEntity);
}

