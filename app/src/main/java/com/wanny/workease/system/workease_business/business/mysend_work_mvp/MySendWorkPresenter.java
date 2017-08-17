package com.wanny.workease.system.workease_business.business.mysend_work_mvp;

import android.text.TextUtils;

import com.wanny.workease.system.framework_mvpbasic.BasePresenter;
import com.wanny.workease.system.framework_net.rxjava.ApiCallback;
import com.wanny.workease.system.framework_net.rxjava.SubscribCallBack;
import com.wanny.workease.system.workease_business.customer.main_mvp.WorkResult;

/**
 * 文件名： MySendWorkPresenter
 * 功能：
 * 作者： wanny
 * 时间： 14:46 2017/8/17
 */
public class MySendWorkPresenter extends BasePresenter<MySendWorkImpl> {

    public MySendWorkPresenter(MySendWorkImpl view){
        attachView(view);
    }

    public void getMyWorkData(String userId,int pageNumber , final String loading) {
        //执行网络请求的回调
        if(!TextUtils.isEmpty(loading)){
            mvpView.loadIng(loading);
        }
        addSubscription(apiStores.getMyWorkResult(userId,pageNumber),new SubscribCallBack<>(new ApiCallback<WorkResult>() {
            @Override
            public void onSuccess(WorkResult model) {
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
