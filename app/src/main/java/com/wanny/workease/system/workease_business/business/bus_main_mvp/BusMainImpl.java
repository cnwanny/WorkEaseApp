package com.wanny.workease.system.workease_business.business.bus_main_mvp;

import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.BaseOperateImp;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;

/**
 * 文件名： BusMainImpl
 * 功能：
 * 作者： wanny
 * 时间： 20:11 2017/8/9
 */
public interface BusMainImpl extends BaseOperateImp<WordPeopleResult> {

   void workType(WorkTypeResult entity);
}
