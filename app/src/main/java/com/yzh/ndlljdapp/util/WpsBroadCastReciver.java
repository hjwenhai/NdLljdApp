package com.yzh.ndlljdapp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
//此类暂时未用.
public class WpsBroadCastReciver extends BroadcastReceiver {

    private final String TAG = "WpsBroadCastReciver-->";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e(TAG, "onReceive : wps广播  --> " + action);
        if (action == null) {
            return;
        }
        switch (action) {
            case WpsModel.Reciver.ACTION_BACK://返回键
                Log.e(TAG, "onReceive :  返回键 --> ");
                break;
            case WpsModel.Reciver.ACTION_CLOSE://关闭文件时的广播
                String closeFile = intent.getStringExtra(WpsModel.ReciverExtra.CLOSEFILE);
                String thirdPackage1 = intent.getStringExtra(WpsModel.ReciverExtra.THIRDPACKAGE);
                Log.e(TAG, "onReceive :  关闭文件收到广播 --> closeFile：" + closeFile + ", \n thirdPackage：" + thirdPackage1);
                break;
            case WpsModel.Reciver.ACTION_HOME://home键广播
                Log.e(TAG, "onReceive :  home键广播 --> ");
                break;
            case WpsModel.Reciver.ACTION_SAVE://保存文件时的广播
                String openFile = intent.getStringExtra(WpsModel.ReciverExtra.OPENFILE);//文件最初的路径
                String thirdPackage = intent.getStringExtra(WpsModel.ReciverExtra.THIRDPACKAGE);//传入的第三方的包名。
                String savePath = intent.getStringExtra(WpsModel.ReciverExtra.SAVEPATH);//文件这次保存的路径
                Log.e(TAG, "onReceive :  保存键广播 --> \n文件最初的路径： "
                        + openFile + "\n传入的第三方的包名：" + thirdPackage + "\n文件这次保存的路径：" + savePath);
                break;
        }
    }
}