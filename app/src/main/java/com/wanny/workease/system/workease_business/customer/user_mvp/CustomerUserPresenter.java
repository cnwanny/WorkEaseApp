package com.wanny.workease.system.workease_business.customer.user_mvp;

import android.text.TextUtils;

import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.BasePresenter;
import com.wanny.workease.system.framework_net.rxjava.ApiCallback;
import com.wanny.workease.system.framework_net.rxjava.SubscribCallBack;
import com.wanny.workease.system.workease_business.login_mvp.LoginResult;

/**
 * 文件名： CustomerUserPresenter
 * 功能：
 * 作者： wanny
 * 时间： 15:10 2017/8/3
 */
public class CustomerUserPresenter extends BasePresenter<CustomerUserImpl> {

    public CustomerUserPresenter(CustomerUserImpl view) {
        attachView(view);
    }

    public void getUserInfo(String userId, String username, final String loading) {
        //执行网络请求的回调
        if (!TextUtils.isEmpty(loading)) {
            mvpView.loadIng(loading);
        }
        addSubscription(apiStores.getUserInfo(userId, username), new SubscribCallBack<>(new ApiCallback<CustomerInfo>() {
            @Override
            public void onSuccess(CustomerInfo model) {
                if (!TextUtils.isEmpty(loading)) {
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

    //修改个人信息
    public void modifyUserInfo(String userId, String name, String mobile, String userState, String areaId, String jobTypeId, String senor, final String loading) {
        //执行网络请求的回调
        if (!TextUtils.isEmpty(loading)) {
            mvpView.loadIng(loading);
        }
        addSubscription(apiStores.modifyUserInfo(userId, name, mobile, userState, areaId, jobTypeId, senor), new SubscribCallBack<>(new ApiCallback<OrdinalResultEntity>() {
            @Override
            public void onSuccess(OrdinalResultEntity model) {
                if (!TextUtils.isEmpty(loading)) {
                    mvpView.hide();
                }
                mvpView.modifySuccess(model);
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.hide();
            }

            @Override
            public void onCompleted() {
                mvpView.hide();
            }
        }));
    }


}
