package com.cango.palmcartreasure.model;

import com.cango.palmcartreasure.util.CommUtil;

import java.util.ArrayList;

/**
 * Created by dell on 2018/3/15.
 */

public class TackPictureInfo {
    /**
     * {
     "Success": true,
     "Msg": "操作成功",
     "Code": 0,
     "Data": {
     "FileList": [
     {
     "FileName": "kfcs.png",
     "FileUrl": "http://192.168.121.7:8060/erp-image/IF/IDownload/2szsxIQr5VM%3D_d954f6c2e91b86ca526b047d4867cb36d70c1456.do"
     }
     ]
     }
     }
     */

    private boolean Success;
    private String Msg;
    private int Code;
    private PictureInfo Data;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        if (CommUtil.checkIsNull(msg))
            msg="";
        Msg = msg;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public PictureInfo getData() {
        return Data;
    }

    public void setData(PictureInfo data) {
        Data = data;
    }

    public class PictureInfo{
        private ArrayList<FileItemInfo> FileList;

        public ArrayList<FileItemInfo> getFileList() {
            return FileList;
        }

        public void setFileList(ArrayList<FileItemInfo> fileList) {
            FileList = fileList;
        }
    }

    public class FileItemInfo{
        private String FileName;
        private String FileUrl;
        private byte[] bytes;
        private boolean isLoader;

        public byte[] getBytes() {
            return bytes;
        }

        public void setBytes(byte[] bytes) {
            this.bytes = bytes;
        }


        public String getFileName() {
            return FileName;
        }

        public void setFileName(String fileName) {
            if (CommUtil.checkIsNull(fileName))
                fileName="";
            FileName = fileName;
        }

        public String getFileUrl() {
            return FileUrl;
        }

        public void setFileUrl(String fileUrl) {
            if (CommUtil.checkIsNull(fileUrl))
                fileUrl="";
            FileUrl = fileUrl;
        }

        public boolean isLoader() {
            return isLoader;
        }

        public void setLoader(boolean loader) {
            isLoader = loader;
        }
    }
}
