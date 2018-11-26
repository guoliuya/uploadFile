package com.xygame.hobby.discovery.presenter;

import android.content.Context;

import com.panic.base.rx.RxUtils;
import com.panic.base.utils.CollectionUtils;
import com.panic.base.utils.GsonUtils;
import com.panic.base.utils.MainThreadPostUtils;
import com.xygame.hobby.db.DaoUtils;
import com.xygame.hobby.db.entity.Hobby;
import com.xygame.hobby.discovery.bean.RequestPublishTrendBean;
import com.xygame.hobby.discovery.bean.TrendHobbiesBean;
import com.xygame.hobby.discovery.bean.TrendsPhotoBean;
import com.xygame.hobby.discovery.contract.PublicTrendsContract;
import com.xygame.hobby.discovery.request.FindBuilder;
import com.xygame.hobby.home.upload.FileUploadObserver;
import com.xygame.hobby.home.upload.RetrofitClient;
import com.xygame.hobby.home.upload.UploadFileApi;
import com.xygame.hobby.home.upload.UploadReq;
import com.xygame.hobby.http.DefaultRetrofitObserver;
import com.xygame.hobby.http.bean.BaseBean;
import com.xygame.hobby.thirdManage.intermessage.HttpCallBack;
import com.xygame.hobby.thirdManage.utils.AliySSOHepler;
import com.xygame.hobby.thirdManage.utils.Constants;
import com.xygame.hobby.thirdManage.utils.ProgressDialogUtil;
import com.xygame.hobby.utils.MMLog;
import com.xygame.hobby.utils.ValidatesUtil;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PublicTrendsPresenter implements PublicTrendsContract.Prestenter {

    private Context mContext;
    private PublicTrendsContract.View mView;


    public PublicTrendsPresenter(Context mContext, PublicTrendsContract.View mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void getHobbiesFromDB() {
        List<Hobby> hobbyList = DaoUtils.getParentHoobyById();
        ArrayList<TrendHobbiesBean> hobbiesBeans = new ArrayList<>();

        if (!CollectionUtils.isEmpty(hobbiesBeans)) {
            hobbiesBeans.clear();
        }

        if (!CollectionUtils.isEmpty(hobbyList)) {
            for (Hobby hobby : hobbyList) {
                TrendHobbiesBean bean = new TrendHobbiesBean();
                bean.isSelect = 0;
                bean.setIcon(hobby.getIcon());
                bean.setId(hobby.getId());
                bean.setName(hobby.getName());
                bean.setParentId(hobby.getParentId());
                bean.setLeaf(hobby.getLeaf());
                hobbiesBeans.add(bean);
            }
        }
        if (mView != null) mView.setHobbies(hobbiesBeans);
    }

    @Override
    public void upImages(final List<String> mList) {

        if (!CollectionUtils.isEmpty(mList)) {
            for (int i = 0; i < mList.size(); i++) {

                RetrofitClient.getInstance().upLoadFile(UploadFileApi.UPLOAD_FILE_URL, new File(mList.get(i)),
                        new FileUploadObserver<BaseBean<UploadReq>>() {
                            @Override
                            public void onUploadSuccess(BaseBean<UploadReq> responseBody) {
                                if (responseBody == null) {
                                    MMLog.e("responseBody null");
                                    return;
                                }
                                MMLog.e(GsonUtils.getGson().toJson(responseBody));
                                if (responseBody.status == 0) {
                                    if (!ValidatesUtil.isEmpty(responseBody.body.getImage().key)) {
                                        if (mView != null)
                                            mView.successUpImage(responseBody.body.getImage().key);
                                    }
                                }else {
                                    ProgressDialogUtil.dismissProgressDialog();
                                    MainThreadPostUtils.toast("图片上传失败!");
                                }
                            }

                            @Override
                            public void onUploadFail(Throwable e) {
                                ProgressDialogUtil.dismissProgressDialog();
                                MainThreadPostUtils.toast("图片上传失败!");
                            }

                            @Override
                            public void onProgress(int progress) {
                                MMLog.i(progress + "");
                            }


                        }, 3, 1);
            }
        }
    }

    @Override public void upImageAndVideo(List<TrendsPhotoBean> trendsPhotoBeans) {
        if (!CollectionUtils.isEmpty(trendsPhotoBeans)) {
            for (int i = 0; i < trendsPhotoBeans.size(); i++) {

                RetrofitClient.getInstance().upLoadFile(UploadFileApi.UPLOAD_FILE_URL, new File(trendsPhotoBeans.get(i).glidePath),
                        new FileUploadObserver<BaseBean<UploadReq>>() {
                            @Override
                            public void onUploadSuccess(BaseBean<UploadReq> responseBody) {
                                if (responseBody == null) {
                                    MMLog.e("responseBody null");
                                    return;
                                }
                                MMLog.e(GsonUtils.getGson().toJson(responseBody));
                                if (responseBody.status == 0) {
                                        if (!ValidatesUtil.isNull(responseBody.body.getVideo())) {
                                            if (mView != null)
                                                mView.successUpImage(responseBody.body.getVideo().key,responseBody.body.getImage().key);
                                        }else{
                                            if (!ValidatesUtil.isNull(responseBody.body.getImage())) {
                                                if (mView != null)
                                                    mView.successUpImage(responseBody.body.getImage().key,"");
                                            }
                                        }
                                }else {
                                    ProgressDialogUtil.dismissProgressDialog();
                                    MainThreadPostUtils.toast("图片上传失败!");
                                }
                            }

                            @Override
                            public void onUploadFail(Throwable e) {
                                ProgressDialogUtil.dismissProgressDialog();
                                MainThreadPostUtils.toast("图片上传失败!");
                            }

                            @Override
                            public void onProgress(int progress) {
                                MMLog.i(progress + "");
                            }


                        }, 3, trendsPhotoBeans.get(i).type);
            }
        }
    }

    /**
     * 上传视频
     *
     * @param path
     */
    @Override
    public void upVideo(String path) {
        RetrofitClient.getInstance().upLoadFile(UploadFileApi.UPLOAD_FILE_URL, new File(path),
                new FileUploadObserver<BaseBean<UploadReq>>() {
                    @Override
                    public void onUploadSuccess(BaseBean<UploadReq> responseBody) {
                        if (responseBody == null) {
                            MMLog.e("responseBody null");
                            return;
                        }
                        MMLog.e(GsonUtils.getGson().toJson(responseBody));
                        if (responseBody.status == 0) {
                            if (!ValidatesUtil.isEmpty(responseBody.body.getVideo().key)) {
                                if (mView != null)
                                    mView.successUpVideo(responseBody.body.getVideo().key,responseBody.body.getImage().key);
                            }
                        }else {
                            ProgressDialogUtil.dismissProgressDialog();
                            MainThreadPostUtils.toast("视频上传失败!");
                        }
                    }

                    @Override
                    public void onUploadFail(Throwable e) {
                        ProgressDialogUtil.dismissProgressDialog();
                        MainThreadPostUtils.toast("视频上传失败!");
                    }

                    @Override
                    public void onProgress(int progress) {
                        MMLog.i(progress + "");
                    }


                }, 3, 2);

    }

    /**
     * 上传gif
     *
     * @param path
     */

    @Override
    public void upgif(String path) {
        AliySSOHepler.getInstance().uploadRecord(mContext, Constants.AVATAR_PATH, new File(path), new HttpCallBack() {
            @Override
            public void onSuccess(String msg, int requestCode) {
                if (mView != null) mView.successUpGif(msg);
            }

            @Override
            public void onFailure(int errorCode, String msg, int requestCode) {
                ProgressDialogUtil.dismissProgressDialog();
            }

            @Override
            public void onProgress(String objectKey, int byteCount, int totalSize) {

            }
        }, "image/*", "gif");
    }

    /**
     * 发布动态
     *
     * @param bean
     */
    @Override
    public void publishTrends(RequestPublishTrendBean bean) {
        new FindBuilder().publishTrend(bean)
                .compose(mView.<BaseBean<Object>>bindToLife())
                .compose(RxUtils.<BaseBean<Object>>defaultSchedulers_single())
                .subscribe(new DefaultRetrofitObserver<Object>() {
                    @Override
                    public void onDataCallback(Object responseObj) {
                        if (mView != null) mView.successPublishTrend();

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ProgressDialogUtil.dismissProgressDialog();
                    }

                });
    }

      /**
     * 成功上传图片
     *
     * @param successPath
     */
    @Override
    public synchronized void successUpImage(String successPath) {
        synchronized (SystemClock.class) {
            mSuccessImage.add(successPath);
            imageCount++;

            if (mPhotoesList.size() == imageCount) {//图片上传完成
                for (int i = 0; i < mSuccessImage.size(); i++) {
                    //视频或者图片bean
                    RessBean ressBean = new RessBean();
                    ressBean.resType = type;
                    ressBean.resUrl = mSuccessImage.get(i);
                    requestBean.cover = mSuccessImage.get(0);
                    requestBean.ress.add(ressBean);

                }
                requestBean.type = type;
                mPresenter.publishTrends(requestBean);
            }else {
                ProgressDialogUtil.dismissProgressDialog();
            }
        }
    }

    @Override
    public synchronized void successUpImage(String videoKey, String gifKey) {
        synchronized (SystemClock.class) {
            mSuccessImage.add(videoKey);
            imageCount++;
            MMLog.e("videoKey",videoKey);

            if (mPhotoesList.size() == imageCount) {//图片上传完成
                for (int i = 0; i < mSuccessImage.size(); i++) {
                    //视频或者图片bean
                    RessBean ressBean = new RessBean();
                    if(mSuccessImage.get(i).contains("mp4")){
                        ressBean.resType = 2;
                    }else{
                        ressBean.resType = 1;
                    }
                    ressBean.resUrl = mSuccessImage.get(i);
                    requestBean.cover = mSuccessImage.get(0).replace("mp4","jpg");
                    ressBean.width = width;
                    ressBean.high = high;
                    requestBean.ress.add(ressBean);

                }
                //        mPresenter.upgif(gifKey);
                requestBean.moodType = TextUtils.isEmpty(mType)?1:Integer.parseInt(mType);
                requestBean.categoryId = topicCategoryId;
                mPresenter.publishTrends(requestBean);
            }
//          else {
//              ProgressDialogUtil.dismissProgressDialog();
//          }
        }
    }

}
