package com.wanny.workease.system.workease_business.business.modify_psd_mvp;

import android.text.TextUtils;

import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.BasePresenter;
import com.wanny.workease.system.framework_net.rxjava.ApiCallback;
import com.wanny.workease.system.framework_net.rxjava.SubscribCallBack;
import com.wanny.workease.system.workease_business.customer.login_mvp.LoginResult;

/**
 * 文件名： ModifyPsdPresenter
 * 功能： 修改密码
 * 作者： wanny
 * 时间： 10:37 2017/8/23
 */
public class ModifyPsdPresenter extends BasePresenter<ModifyPsdImpl> {
    public ModifyPsdPresenter(ModifyPsdImpl view){
        attachView(view);
    }







     //修改密码
    public void modifypsd(String userId , String oldpsd ,String newpsd, final String loading) {
        //执行网络请求的回调
        if(!TextUtils.isEmpty(loading)){
            mvpView.loadIng(loading);
        }
        addSubscription(apiStores.modifyPsd(userId,oldpsd,newpsd),new SubscribCallBack<>(new ApiCallback<OrdinalResultEntity>() {
            @Override
            public void onSuccess(OrdinalResultEntity model) {
                if(!TextUtils.isEmpty(loading)){
                    mvpView.hide();
                }
                mvpView.success(model);
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.hide();
            }
            @Override
            public void onCompleted() {

            }
        }));
    }


}

