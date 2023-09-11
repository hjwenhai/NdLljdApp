package com.yzh.ndlljdapp.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;

import java.io.File;

public class WpsUtil {
    public static void openFile(Context context, File file){
        Bundle bundle = new Bundle();
        bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.NORMAL); // 正常模式

        bundle.putBoolean(WpsModel.SEND_CLOSE_BROAD, true); // 文件关闭时是否发送广播
        bundle.putBoolean(WpsModel.SEND_SAVE_BROAD, true); // 文件保存时是否发送广播
        bundle.putBoolean(WpsModel.HOMEKEY_DOWN, true); // 单机home键是否发送广播
        bundle.putBoolean(WpsModel.BACKKEY_DOWN, true); // 单机back键是否发送广播

        bundle.putString(WpsModel.THIRD_PACKAGE, WpsModel.PackageName.NORMAL); // 第三方应用的包名，用于对改应用合法性的验证
        bundle.putBoolean(WpsModel.CLEAR_FILE, false); //关闭后是否删除打开文件

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setClassName(WpsModel.PackageName.NORMAL, WpsModel.ClassName.NORMAL);
        intent.putExtras(bundle);
        uriX(context, intent, file);
    }

    public static void uriX(Context context, Intent intent, File file) {
        if (Build.VERSION.SDK_INT > 23) {//android 7.0以上时，URI不能直接暴露
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //另如果使用的androidx：androidx.core.content.FileProvider替换android.support.v4.content.FileProvider
            Uri uriForFile = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileProvider", file);

            intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
        } else {
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
//            ToastUtil.showToast(context, R.string.please_check_wps);
            e.printStackTrace();
        }
    }
/*    public static boolean openFile(Context context,String path) {

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.NORMAL);
        //打开模式
        bundle.putBoolean(WpsModel.SEND_SAVE_BROAD, true);
        //关闭时是否发送广播
        bundle.putString(WpsModel.THIRD_PACKAGE, context.getApplicationContext().getPackageName());
        //第三方应用的包名，用于对改应用合法性的验证
        bundle.putBoolean(WpsModel.CLEAR_TRACE, true);
        //清除打开记录
        //bundle.putBoolean(CLEAR_FILE, true);
        //关闭后删除打开文件
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setClassName(WpsModel.PackageName.NORMAL, WpsModel.ClassName.NORMAL);
        File file = new File(path);
        if (!file.exists()){
            return false;
        }
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        intent.putExtras(bundle);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }*/
}
