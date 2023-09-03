package com.yzh.ndlljdapp.ui.controller_new;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yzh.ndlljdapp.config.Constants;
import com.yzh.ndlljdapp.dao.FlowDataAppDataBase;
import com.yzh.ndlljdapp.entity.CalData;
import com.yzh.ndlljdapp.entity.CalInfor;
import com.yzh.ndlljdapp.entity.ResponseCalData;
import com.yzh.ndlljdapp.entity.ResponseCalInfor;
import com.yzh.ndlljdapp.util.GsonUtil;
import com.yzh.ndlljdapp.util.OkHttpResultCallback;
import com.yzh.ndlljdapp.util.OkHttpUtil;

import java.io.UnsupportedEncodingException;
import java.util.List;

import okhttp3.Call;

public class ControllerNewViewModel extends ViewModel {

    private final MutableLiveData<String> mText;    //提示信息
    private final MutableLiveData<CalInfor> mCalInfor; //检定信息
    private final MutableLiveData<List<CalData>> mCalDatas; //检定数据
    private final MutableLiveData<Boolean> mIsInLocalDataBase; //在本地数据库中是否有该记录

    public ControllerNewViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        mCalInfor=new MutableLiveData<>();
        mCalInfor.setValue(null);
        mCalDatas=new MutableLiveData<>();
        mCalDatas.setValue(null);
        mIsInLocalDataBase=new MutableLiveData<>();
        mIsInLocalDataBase.setValue(false);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<CalInfor> getCalInfor(){
        return mCalInfor;
    }
    public LiveData<List<CalData>> getCalDatas(){return mCalDatas;}
    public LiveData<Boolean> getIsInLocalDataBase(){return mIsInLocalDataBase;}
    /**
     * 从控制器获取最新的检定信息
     */
    public void getNewestFlowDataFromController(){
        String url  = Constants.BASE_URL + "v1/calinfo/querry?newest=true";
        OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {
            {
                mText.setValue("正在从控制器获取最新的检定信息...");
            }
            @Override
            public void onError(Call call, Exception e) {
                mText.setValue("控制器没有响应,请检查是否连接到控制器,控制器的IP是否设置正确！");
                mCalInfor.setValue(null);
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    mText.setValue("获取最新的检定信息成功！");
                    String s = new String(bytes,"UTF-8");
                    ResponseCalInfor responseCalInfor = GsonUtil.gsonToBean(s, ResponseCalInfor.class);
                    switch (responseCalInfor.getError_code()){
                        case 0:
                            Log.d("return CalInfor:",responseCalInfor.getData().toString());
                            mCalInfor.setValue(responseCalInfor.getData());
                            break;
                        case 400:
                            mText.setValue("请求检定信息的参数有误");
                            mCalInfor.setValue(null);
                            break;
                        case 500:
                            mText.setValue("控制器数据库中没有任何检定信息记录！");
                            mCalInfor.setValue(null);
                            break;
                            case 501:
                            mText.setValue("控制器数据库中没有符合查询条件的检定信息记录！");
                            mCalInfor.setValue(null);
                            break;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getCalDatasFromController(){
        CalInfor calInfor=mCalInfor.getValue();
        if(calInfor!=null){
            String url  = Constants.BASE_URL + "v1/caldata/querry?infoid="+ calInfor.getId();
            OkHttpUtil.getInstance().getAsync(url, new OkHttpResultCallback() {
                {
                    mText.setValue("正在从控制器获取最新的检定数据...");
                }
                @Override
                public void onError(Call call, Exception e) {
                    mText.setValue("控制器没有响应,请检查是否连接到控制器,控制器的IP是否设置正确！");
                    mCalInfor.setValue(null);
                }

                @Override
                public void onResponse(byte[] bytes) {
                    try {
                        mText.setValue("获取最新的检定信息成功！");
                        String s = new String(bytes,"UTF-8");
                        ResponseCalData responseCalData = GsonUtil.gsonToBean(s, ResponseCalData.class);
                        switch (responseCalData.getError_code()){
                            case 0:
                                Log.d("return CalData:",responseCalData.toString());
                                mCalDatas.setValue(responseCalData.getData());
                                break;
                            case 400:
                                mText.setValue("请求检定数据的参数有误");
                                mCalDatas.setValue(null);
                                break;
                            case 500:
                                mText.setValue("控制器数据库中没有任何检定数据记录！");
                                mCalDatas.setValue(null);
                                break;
                            case 501:
                                mText.setValue("控制器数据库中没有符合查询条件的检定数据记录！");
                                mCalDatas.setValue(null);
                                break;
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}