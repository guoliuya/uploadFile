package com.xygame.hobby.home.upload;

import android.text.TextUtils;

import com.panic.base.BasePreferences;
import com.panic.base.account.AccountManager;
import com.panic.base.config.GlobalConfig;
import com.panic.base.utils.DeviceUtil;
import com.xygame.hobby.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.panic.http.http.ServiceFactory.KEY_APP_VERSION;
import static com.panic.http.http.ServiceFactory.KEY_AUTHORIZATION;
import static com.panic.http.http.ServiceFactory.KEY_DEVICE_ID;
import static com.panic.http.http.ServiceFactory.KEY_OS;


public class OkHttpManager {
    private static OkHttpClient okHttpClient;

    /**
     * 获取OkHttp单例，线程安全.
     *
     * @return 返回OkHttpClient单例
     */
    public static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            synchronized (OkHttpManager.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();

                    builder.addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            //请求头,具体参数与后台商议
                            Request newReq =
                                    request.newBuilder()
                                            .addHeader("content-type", "application/json;charset:utf-8")
                                            .addHeader(KEY_AUTHORIZATION, TextUtils.isEmpty(AccountManager.getInstance().getToken())?
                                                    BasePreferences.getKeyGuesssToken():AccountManager.getInstance().getToken())
                                            .addHeader(KEY_OS, "1")
                                            .addHeader(KEY_DEVICE_ID, DeviceUtil.getDeviceId(GlobalConfig.getAppContext()))
                                            .addHeader(KEY_APP_VERSION, GlobalConfig.getVersionName())
                                            .build();
                            return chain.proceed(newReq);
                        }
                    });

                    // 超时时间
                    builder.connectTimeout(15, TimeUnit.SECONDS);// 15S连接超时
                    builder.readTimeout(100, TimeUnit.SECONDS);// 100s读取超时
                    builder.writeTimeout(100, TimeUnit.SECONDS);// 100s写入超时
                    // 错误重连
                    builder.retryOnConnectionFailure(true);
                    okHttpClient = builder.build();
                }
            }
        }
        return okHttpClient;
    }
}
