package com.yzh.ndlljdapp.ui.controller_new;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yzh.ndlljdapp.config.Constants;
import com.yzh.ndlljdapp.util.OkHttpResultCallback;
import com.yzh.ndlljdapp.util.OkHttpUtil;

import java.io.UnsupportedEncodingException;

import okhttp3.Call;

public class ControllerNewViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ControllerNewViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void getNewestFlowData(){
        String url  = Constants.BASE_URL + "v1/calinfo/querry?newest=true";
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mText.setValue("服务器没有响应！");
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s = new String(bytes,"UTF-8");
                    mText.setValue(s);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}