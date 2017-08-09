package com.wanny.workease.system.workease_business.customer.user_mvp;

import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.BaseOperateImp;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;

/**
 * 文件名： CustomerUserImpl
 * 功能：
 * 作者： wanny
 * 时间： 15:10 2017/8/3
 */
public interface CustomerUserImpl extends BaseOperateImp<CustomerInofResult>{


    void modifySuccess(OrdinalResultEntity ordinalResultEntity);
    void workType(WorkTypeResult entity);
    void getCityValue(CityResult cityResult);
}

