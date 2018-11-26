package com.xygame.hobby.home.upload;

import com.xygame.hobby.http.bean.BaseBean;

import java.io.File;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class RetrofitClient {
    private static RetrofitClient mInstance;
    private static Retrofit retrofit;

    private RetrofitClient() {
        retrofit = RetrofitBuilder.buildRetrofit();
    }

    /**
     * 获取RetrofitClient实例.
     *
     * @return 返回RetrofitClient单例
     */
    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    private <T> T create(Class<T> clz) {
        return retrofit.create(clz);
    }

    /**
     * 单上传文件的封装.
     *
     * @param url 完整的接口地址
     * @param file 需要上传的文件
     * @param fileUploadObserver 上传回调
     * @param pathType  文件存储路径 1:头像目录,2:活动图片目录,3:动态目录,4:相册目录,5:用户验证资料目录
     * @param type 类型，1上传图片，2上传视频
     */
    public void upLoadFile(String url, File file,
                           FileUploadObserver<BaseBean<UploadReq>> fileUploadObserver, Integer pathType, Integer type) {

        UploadFileRequestBody uploadFileRequestBody =
                new UploadFileRequestBody(file, fileUploadObserver);

        create(UploadFileApi.class)
                .uploadFile(url, MultipartBuilder.fileToMultipartBody(file,
                        uploadFileRequestBody),pathType,type)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileUploadObserver);

    }

    /**
     * 多文件上传.
     *
     * @param url 上传接口地址
     * @param files 文件列表
     * @param fileUploadObserver 文件上传回调
     * @param pathType  文件存储路径 1:头像目录,2:活动图片目录,3:动态目录,4:相册目录,5:用户验证资料目录
     * @param type 类型，1上传图片，2上传视频
     */
    public void upLoadFiles(String url, List<File> files,
                            FileUploadObserver<BaseBean<UploadReq>> fileUploadObserver,Integer pathType,Integer type) {

        create(UploadFileApi.class)
                .uploadFile(url, MultipartBuilder.filesToMultipartBody(files,
                        fileUploadObserver),pathType,type)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileUploadObserver);

    }

}
