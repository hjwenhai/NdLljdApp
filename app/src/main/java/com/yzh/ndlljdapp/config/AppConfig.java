package com.yzh.ndlljdapp.config;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.yzh.ndlljdapp.entity.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppConfig extends Application {
    private static AppConfig appConfig;   //唯一实例

    private  SharedPreferences preferences;

    private String baseUrl; //控制器的IP与端口
    private String ip; //控制器的ip
    private User loginUser; //登录用户

    public static AppConfig getInstance(){
        return appConfig;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // 在打开应用时对静态的应用实例赋值
        preferences = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
        baseUrl=preferences.getString("baseUrl",Constants.BASE_URL);
        String[] strs=baseUrl.split("http://");
        if(strs.length>0){
            String str1=strs[1];
            strs=str1.split(":");
            ip=strs.length>0 ? strs[0] : "";
        }
        appConfig = this;
        loginUser=null;
    }
    public String getBaseUrl() {
        return baseUrl;
    }


    public String getIp(){return ip;}

    public void setIp(String ip){
        //使用正则表达式过滤
        String re = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(ip);
        if(matcher.matches()){
            this.ip=ip;
            String url="http://"+ip+":65000/";
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("baseUrl",url);
            editor.commit();
            this.baseUrl = url;
        }
    }

    public User getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }

}
