package com.wanny.workease.system.workease_business.customer.login_mvp;

import android.text.TextUtils;

import com.wanny.workease.system.framework_mvpbasic.BasePresenter;
import com.wanny.workease.system.framework_net.rxjava.ApiCallback;
import com.wanny.workease.system.framework_net.rxjava.SubscribCallBack;


/**
 * 文件名： LoginPresenter
 * 功能：
 * 作者： wanny
 * 时间： 9:36 2017/4/6
 */
public class LoginPresenter extends BasePresenter<LoginImpl> {

    public LoginPresenter(LoginImpl view){
        attachView(view);
    }

    public void login(String phone , String password , final String loading) {
        //执行网络请求的回调
        if(!TextUtils.isEmpty(loading)){
            mvpView.loadIng(loading);
        }
        addSubscription(apiStores.login(phone,password),new SubscribCallBack<>(new ApiCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult model) {
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

    //
//    public void setJpush(String parentId , String registId , String sign) {
//        //执行网络请求的回调
//        addSubscription(apiStores.setJpush(parentId,registId,sign), new SubscribCallBack<>(new ApiCallback<OrdinalResultEntity>() {
//            @Override
//            public void onSuccess(OrdinalResultEntity model) {
//                mvpView.setJpush(model);
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//            }
//
//            @Override
//            public void onCompleted() {
//
//            }
//        }));
//    }

}
