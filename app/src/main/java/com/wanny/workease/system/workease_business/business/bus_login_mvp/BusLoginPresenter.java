package com.wanny.workease.system.workease_business.business.bus_login_mvp;

import android.text.TextUtils;

import com.wanny.workease.system.framework_mvpbasic.BasePresenter;
import com.wanny.workease.system.framework_net.rxjava.ApiCallback;
import com.wanny.workease.system.framework_net.rxjava.SubscribCallBack;
import com.wanny.workease.system.workease_business.customer.login_mvp.LoginResult;

/**
 * 文件名： BusLoginPresenter
 * 功能：
 * 作者： wanny
 * 时间： 20:02 2017/8/9
 */
public class BusLoginPresenter extends BasePresenter<BusLoginImpl> {



    public BusLoginPresenter(BusLoginImpl veiw){
        attachView(veiw);
    }
    public void login(String phone , String password ,String type, final String loading) {
        //执行网络请求的回调
        if(!TextUtils.isEmpty(loading)){
            mvpView.loadIng(loading);
        }
        addSubscription(apiStores.login(phone,password,type),new SubscribCallBack<>(new ApiCallback<LoginResult>() {
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


    public void getServicePhone() {
        //执行网络请求的回调

        addSubscription(apiStores.getServicePhone(),new SubscribCallBack<>(new ApiCallback<ServicePhoneResult>() {
            @Override
            public void onSuccess(ServicePhoneResult model) {
                mvpView.getServicePhone(model);
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
