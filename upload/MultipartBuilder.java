package com.xygame.hobby.home.upload;

import com.xygame.hobby.http.bean.BaseBean;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class MultipartBuilder {
    /**
     * 单文件上传构造.
     *
     * @param file 文件
     * @param requestBody 请求体
     * @return MultipartBody
     */
    public static MultipartBody fileToMultipartBody(File file, RequestBody requestBody) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("pathType", pathType);
////        jsonObject.addProperty("fileSha", Utils.getFileSha1(file));
//        jsonObject.addProperty("type", type);

        builder.addFormDataPart("file", file.getName(), requestBody);
//        builder.addFormDataPart("params", jsonObject.toString());
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

    /**
     * 多文件上传构造.
     *
     * @param files 文件列表
     * @param fileUploadObserver 文件上传回调
     * @return MultipartBody
     */
    public static MultipartBody filesToMultipartBody(List<File> files,
                                                     FileUploadObserver<BaseBean<UploadReq>> fileUploadObserver) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
//        JsonArray jsonArray = new JsonArray();

//        Gson gson = new Gson();
        for (File file : files) {
            UploadFileRequestBody uploadFileRequestBody =
                    new UploadFileRequestBody(file, fileUploadObserver);
//            JsonObject jsonObject = new JsonObject();
//
//            jsonObject.addProperty("fileName", file.getName());
////            jsonObject.addProperty("fileSha", Utils.getFileSha1(file));
//            jsonObject.addProperty("appId", "test0002");

//            jsonArray.add(jsonObject);
//            MMLog.e(jsonObject.toString());
            builder.addFormDataPart("file", file.getName(), uploadFileRequestBody);
        }

//        builder.addFormDataPart("params", gson.toJson(jsonArray));
//
//        MMLog.e(gson.toJson(jsonArray));
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }
}
