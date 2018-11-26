package com.xygame.hobby.home.upload;

import com.xygame.hobby.http.RequestConstants;
import com.xygame.hobby.http.bean.BaseBean;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface UploadFileApi {
    String UPLOAD_FILE_URL = RequestConstants.getApiUrl() + "files/upload/v1";

    @POST
    Observable<BaseBean<UploadReq>> uploadFile(@Url String url, @Body MultipartBody body, @Query("pathType") Integer pathType, @Query("type") Integer type) ;
}
