package com.wanny.workease.system.workease_business.login_mvp;


import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.BaseOperateImp;

/**
 * 文件名： LoginImpl
 * 功能：
 * 作者： wanny
 * 时间： 9:37 2017/4/6
 */
public interface LoginImpl extends BaseOperateImp<LoginResult> {

    void setJpush(OrdinalResultEntity entity);

}
