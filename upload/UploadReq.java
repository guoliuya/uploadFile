package com.xygame.hobby.home.upload;

import java.io.Serializable;


public class UploadReq implements Serializable {

    private FileResponse image;
    private FileResponse video;
    private Integer type;

    public FileResponse getImage() {
        return image;
    }

    public void setImage(FileResponse image) {
        this.image = image;
    }

    public FileResponse getVideo() {
        return video;
    }

    public void setVideo(FileResponse video) {
        this.video = video;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public class FileResponse{
        public String key;
        public String url;
    }
}
