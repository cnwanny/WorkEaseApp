
package com.wanny.workease.system.framework_net.rxjava;

import com.wanny.workease.system.framework_basicutils.PreferenceUtil;
import com.wanny.workease.system.framework_care.ContentData;
import com.wanny.workease.system.framework_care.YiPingApplication;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 文件名： AddCookieInternal
 * 功能：
 * 作者： wanny
 * 时间： 10:54 2017/5/5
 */

public class AddCookieInternal implements Interceptor {
    public AddCookieInternal() {
        super();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request.Builder builder = chain.request().newBuilder();
        String userId = PreferenceUtil.getInstance(YiPingApplication.getContext()).getString("userId","");
        builder.addHeader("userId",userId);
        return chain.proceed(builder.build());
    }
}

