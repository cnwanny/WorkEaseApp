package com.wanny.workease.system.workease_business.customer.modify_user_mvp;

import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.BaseOperateImp;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerInofResult;

/**
 * 文件名： ModifyUserImpl
 * 功能：
 * 作者： wanny
 * 时间： 11:05 2017/8/15
 */
public interface ModifyUserImpl extends BaseOperateImp<OrdinalResultEntity> {

    void modifySuccess(CustomerInofResult entity);

    void workType(WorkTypeResult entity);

    void getCityValue(CityResult entity);


}
