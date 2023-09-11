package com.yzh.ndlljdapp.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.yzh.ndlljdapp.config.Constants;
import com.yzh.ndlljdapp.dao.FlowDataAppDataBase;
import com.yzh.ndlljdapp.dao.FlowDocDao;
import com.yzh.ndlljdapp.dao.UserDao;
import com.yzh.ndlljdapp.entity.FlowDoc;

import java.util.List;


public class FlowDocModel {
    private FlowDataAppDataBase dataBase;
    private FlowDocDao flowDocDao;

    public FlowDocModel(Context context) {
        this.dataBase=FlowDataAppDataBase.getINSTANCE(context);
        this.flowDocDao=dataBase.getFlowDocDao();
    }
    public void getFlowDocList(@Nullable String devNo, @Nullable Long date, @Nullable String customer,
                               @Nullable String manufactor, @Nullable String fileName, Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FlowDoc> flowDocList = flowDocDao.getFlowDocList(devNo, date, customer, manufactor, fileName);
                Message message = handler.obtainMessage();
                message.what = Constants.QUERY_FLOW_DOC;
                message.obj = flowDocList;
                handler.sendMessage(message);
            }
        }).start();
    }
    public void getFlowDocList(String devNo,Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FlowDoc> flowDocList = flowDocDao.getFlowDocList(devNo);
                Message message = handler.obtainMessage();
                message.what = Constants.QUERY_FLOW_DOC;
                message.obj = flowDocList;
                handler.sendMessage(message);
            }
        }).start();
    }
    public void insertFlowDoc(FlowDoc flowDoc, Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long id=flowDocDao.insertFlowDoc(flowDoc);
                Message message = handler.obtainMessage();
                message.what = Constants.INSERT_FLOW_DOC;
                message.obj = id;
                handler.sendMessage(message);
            }
        }).start();
    }
}
