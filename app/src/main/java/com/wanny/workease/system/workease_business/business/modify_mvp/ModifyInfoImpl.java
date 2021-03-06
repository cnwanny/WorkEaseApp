package com.wanny.workease.system.workease_business.business.modify_mvp;

import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.BaseOperateImp;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityResult;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerInofResult;

/**
 * 文件名：com.wanny.workease.system.workease_business.business.modify_mvp
 * 功能：
 * 创建人：wanny
 * 日期：2017/8/13 上午10:39
 */

public interface ModifyInfoImpl extends BaseOperateImp<CustomerInofResult> {
    void getCityValue(CityResult cityResult);
}
