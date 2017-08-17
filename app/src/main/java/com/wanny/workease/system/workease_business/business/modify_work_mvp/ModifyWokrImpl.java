package com.wanny.workease.system.workease_business.business.modify_work_mvp;

import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.BaseOperateImp;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;

/**
 * 文件名： ModifyWokrImpl
 * 功能：
 * 作者： wanny
 * 时间： 16:00 2017/8/17
 */
public interface ModifyWokrImpl extends BaseOperateImp<OrdinalResultEntity> {



    void workType(WorkTypeResult entity);


    void getCityValue(CityResult entity);

    void deleteById(OrdinalResultEntity entity);
}
