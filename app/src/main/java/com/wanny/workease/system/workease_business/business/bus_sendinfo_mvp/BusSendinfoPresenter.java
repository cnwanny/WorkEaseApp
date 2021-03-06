package com.wanny.workease.system.workease_business.business.bus_sendinfo_mvp;

import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.BasePresenter;
import com.wanny.workease.system.framework_net.rxjava.ApiCallback;
import com.wanny.workease.system.framework_net.rxjava.SubscribCallBack;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;

/**
 * 文件名： BusSendinfoPresenter
 * 功能：
 * 作者： wanny
 * 时间： 20:04 2017/8/9
 */
public class BusSendinfoPresenter extends BasePresenter<BusSendinfoImpl> {

    public BusSendinfoPresenter(BusSendinfoImpl view){
        attachView(view);
    }

    public void relaseWork(String userId,String areaId , String jobTypeId , int returmeNmber ,String price,String name , String desc ,String detaillAddress ,String imgs , double lat , double lon,String laoding ) {
        //执行网络请求的回调
        addSubscription(apiStores.releaseTask(userId,areaId,jobTypeId,returmeNmber,price,name,desc,detaillAddress,imgs,lat,lon), new SubscribCallBack<>(new ApiCallback<OrdinalResultEntity>() {
            @Override
            public void onSuccess(OrdinalResultEntity model) {
                mvpView.success(model);
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.fail(msg);
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
               mvpView.fail(msg);
            }

            @Override
            public void onCompleted() {

            }
        }));
    }




    public void getCityValue() {
        //执行网络请求的回调
        addSubscription(apiStores.getCity(), new SubscribCallBack<>(new ApiCallback<CityResult>() {
            @Override
            public void onSuccess(CityResult model) {
                mvpView.getCityValue(model);
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
