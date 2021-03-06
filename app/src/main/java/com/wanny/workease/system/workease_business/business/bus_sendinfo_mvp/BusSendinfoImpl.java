package com.wanny.workease.system.workease_business.business.bus_sendinfo_mvp;

import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.BaseOperateImp;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;

/**
 * 文件名： BusSendinfoImpl
 * 功能：
 * 作者： wanny
 * 时间： 20:05 2017/8/9
 */
public interface BusSendinfoImpl extends BaseOperateImp<OrdinalResultEntity>{
    //获取工作类型
    void workType(WorkTypeResult entity);
    //获取城市数据
    void getCityValue(CityResult cityResult);
}


