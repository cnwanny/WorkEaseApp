package com.wanny.workease.system.workease_business.customer.register_mvp;

import com.wanny.workease.system.framework_mvpbasic.BasePresenter;
import com.wanny.workease.system.framework_net.rxjava.ApiCallback;
import com.wanny.workease.system.framework_net.rxjava.SubscribCallBack;
import com.wanny.workease.system.workease_business.customer.login_mvp.LoginResult;


/**
 * 文件名： RegisterPresenter
 * 功能：
 * 作者： wanny
 * 时间： 14:31 2017/4/6
 */
public class RegisterPresenter extends BasePresenter<RegisterImpl> {

    public RegisterPresenter(RegisterImpl view ){
        attachView(view);
    }



//    public void getCode(String phone, int codeType, String sign, final String loading) {
//        //执行网络请求的回调
//        if(!TextUtils.isEmpty(loading)){
//            mvpView.loadIng(loading);
//        }
//        addSubscription(apiStores.getCode(phone,codeType,sign), new SubscribCallBack<>(new ApiCallback<OrdinalResultEntity>() {
//            @Override
//            public void onSuccess(OrdinalResultEntity model) {
//                if(!TextUtils.isEmpty(loading)){
//                    mvpView.hide();
//                }
//                mvpView.success(model);
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//                if(!TextUtils.isEmpty(loading)){
//                    mvpView.hide();
//                }
//                if (code == 103) {
//                    mvpView.fail("103");
//                }
//            }
//
//            @Override
//            public void onCompleted() {
//
//            }
//        }));
//    }


    public void register(String mobile,String password,String type,String userName,String areaId,String jobTypeId,String workyear , String senior) {
        //执行网络请求的回调
        addSubscription(apiStores.register(mobile,password,type,userName,areaId,jobTypeId,workyear,senior), new SubscribCallBack<>(new ApiCallback<RegisterResult>() {
            @Override
            public void onSuccess(RegisterResult model) {
                mvpView.registerSuccess(model);
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
