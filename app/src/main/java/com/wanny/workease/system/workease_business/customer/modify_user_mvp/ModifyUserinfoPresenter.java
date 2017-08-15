package com.wanny.workease.system.workease_business.customer.modify_user_mvp;

import android.text.TextUtils;

import com.wanny.workease.system.framework_care.OrdinalResultEntity;
import com.wanny.workease.system.framework_mvpbasic.BasePresenter;
import com.wanny.workease.system.framework_net.rxjava.ApiCallback;
import com.wanny.workease.system.framework_net.rxjava.SubscribCallBack;
import com.wanny.workease.system.workease_business.customer.register_mvp.CityResult;
import com.wanny.workease.system.workease_business.customer.register_mvp.WorkTypeResult;
import com.wanny.workease.system.workease_business.customer.user_mvp.CustomerInofResult;

/**
 * 文件名： ModifyUserinfoPresenter
 * 功能：
 * 作者： wanny
 * 时间： 11:04 2017/8/15
 */
public class ModifyUserinfoPresenter extends BasePresenter<ModifyUserImpl> {

    public ModifyUserinfoPresenter(ModifyUserImpl view){
        attachView(view);
    }
   //userId=40626768-ff90-4f38-aa47-fe560e9067f6&mobile=13220286010&type=1&userName=tony&areaId=110000&jobTypeId=2321&senior=精通&workyear=12
    //修改个人信息
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
