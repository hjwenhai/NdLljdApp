package com.yzh.ndlljdapp.model;



import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.yzh.ndlljdapp.config.Constants;
import com.yzh.ndlljdapp.dao.FlowDataAppDataBase;
import com.yzh.ndlljdapp.dao.UserDao;
import com.yzh.ndlljdapp.entity.User;

public class UserModel {
    private FlowDataAppDataBase dataBase;
    private UserDao userDao;

    public UserModel(Context context) {
        this.dataBase=FlowDataAppDataBase.getINSTANCE(context);
        this.userDao=dataBase.getUserDao();
    }

    /**
     * 检查登录
     * @param name
     * @param password
     * @param handler
     */
    public void checkUser(String name,String password, Handler handler){
        new Thread(new Runnable() {
            public void run() {
                // 执行耗时操作，查询用户
                // 查询结果，发送消息通知主线程
                User user = userDao.getUserByName(name);
                if(user!=null && !user.getPassword().equals(password))
                    user=null;
                Message message = handler.obtainMessage();
                message.what = Constants.LOGIN_CHECK_USER;
                message.obj = user;
                handler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 增加新用户
     * @param user
     * @param handler
     */
    public void insertNewUser(User user,Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long id=userDao.insertUser(user);
                user.setId(id);
                Message message = handler.obtainMessage();
                message.what = Constants.LOGIN_REGISTER_USER;
                message.obj = user;
                handler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 更新用户
     * @param user
     * @param handler
     */
    public void updateUser(User user,Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Integer num=userDao.updateUser(user);
                Message message = handler.obtainMessage();
                message.what = Constants.LOGIN_UPDATE_USER;
                message.obj = num;
                handler.sendMessage(message);
            }
        }).start();
    }

    public void deleteUser(User user,Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Integer num=userDao.deleteUser(user);
                Message message = handler.obtainMessage();
                message.what = Constants.LOGIN_DELETE_USER;
                message.obj = num;
                handler.sendMessage(message);
            }
        }).start();
    }

}
