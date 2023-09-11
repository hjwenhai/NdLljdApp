package com.yzh.ndlljdapp.util;


public interface WordResultCallBack {
    void onError(Exception e);
    void onSuccess(String filePath);
}
