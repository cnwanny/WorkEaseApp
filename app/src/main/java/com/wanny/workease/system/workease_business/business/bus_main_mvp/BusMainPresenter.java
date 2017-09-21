package com.wanny.workease.system.workease_business.business.bus_main_mvp;

import android.text.TextUtils;

import com.wanny.workease.system.framework_mvpbasic.BasePresenter;
import com.wanny.workease.system.framework_net.rxjava.ApiCallback;
import com.wanny.workease.system.framework_net.rxjava.SubscribCallBack;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;

/**
 * 文件名： BusMainPresenter
 * 功能：
 * 作者： wanny
 * 时间： 20:03 2017/8/9
 */
public class BusMainPresenter extends BasePresenter<BusMainImpl> {

    public BusMainPresenter(BusMainImpl view){
        attachView(view);
    }


    public void getWorkList(int  number ,String workTypeId,String userState, final String loading) {
        //执行网络请求的回调
        if(!TextUtils.isEmpty(loading)){
            mvpView.loadIng(loading);
        }
        addSubscription(apiStores.getWorkInfo(number,workTypeId,userState),new SubscribCallBack<>(new ApiCallback<WordPeopleResult>() {
            @Override
            public void onSuccess(WordPeopleResult model) {
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

    public void getWorkType() {
        //执行网络请求的回调
        addSubscription(apiStores.getWorkType(), new SubscribCallBack<>(new ApiCallback<WorkTypeResult>() {
            @Override
            public void onSuccess(WorkTypeResult model) {
                mvpView.workType(model);
            }

            @Override
            public void onFailure(int code, String msg) {
                if (code == 103) {
                    mvpView.fail("103");
                }
            }

            @Override
            public void onCompleted() {

            }
        }));
    }





}
