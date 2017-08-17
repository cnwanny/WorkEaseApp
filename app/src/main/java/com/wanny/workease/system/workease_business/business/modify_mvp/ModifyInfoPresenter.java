package com.wanny.workease.system.workease_business.business.modify_mvp;

import android.text.TextUtils;

import com.wanny.workease.system.framework_mvpbasic.BasePresenter;
import com.wanny.workease.system.framework_net.rxjava.ApiCallback;
import com.wanny.workease.system.framework_net.rxjava.SubscribCallBack;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerInofResult;

/**
 * 文件名：com.wanny.workease.system.workease_business.business.modify_mvp
 * 功能：
 * 创建人：wanny
 * 日期：2017/8/13 上午10:39
 */

public class ModifyInfoPresenter extends BasePresenter<ModifyInfoImpl> {

    public ModifyInfoPresenter(ModifyInfoImpl view){
        attachView(view);
    }
    public void modifyUserInfo(String userId, String userName, String mobile, String userState, String areaId, String jobTypeId, String senor,String workyear , final String loading) {
        //执行网络请求的回调
        if (!TextUtils.isEmpty(loading)) {
            mvpView.loadIng(loading);
        }
        addSubscription(apiStores.modifyUserInfo(userId, userName, mobile, userState, areaId, jobTypeId, senor,workyear), new SubscribCallBack<>(new ApiCallback<CustomerInofResult>() {
            @Override
            public void onSuccess(CustomerInofResult model) {
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
                mvpView.hide();
            }
        }));
    }


}
