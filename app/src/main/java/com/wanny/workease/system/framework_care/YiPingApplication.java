package com.wanny.workease.system.framework_care;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

/**
 * 文件名： YiPingApplication
 * 功能：
 * 作者： wanny
 * 时间： 16:16 2017/6/22
 */
public class YiPingApplication extends Application {




    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        SDKInitializer.initialize(getApplicationContext());
    }


    public static Context getContext(){
        return context;
    }
}
