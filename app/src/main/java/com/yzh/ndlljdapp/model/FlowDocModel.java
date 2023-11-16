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
import com.yzh.ndlljdapp.entity.CalInfor;
import com.yzh.ndlljdapp.entity.FlowDoc;
import com.yzh.ndlljdapp.recyleviewitem.CalInforItem;

import java.util.ArrayList;
import java.util.List;


public class FlowDocModel {
    private FlowDataAppDataBase dataBase;
    private FlowDocDao flowDocDao;

    public FlowDocModel(Context context) {
        this.dataBase=FlowDataAppDataBase.getINSTANCE(context);
        this.flowDocDao=dataBase.getFlowDocDao();
    }
    public void getFlowDocList(@Nullable String devNo, @Nullable Long date, Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FlowDoc> flowDocList = flowDocDao.getFlowDocList(devNo, date);
                Message message = handler.obtainMessage();
                message.what = Constants.QUERY_FLOW_DOC;
                message.obj = flowDocList;
                handler.sendMessage(message);
            }
        }).start();
    }

    //本地搜索
    public void getFlowDocList(@Nullable String devNo, @Nullable Long startDate, @Nullable Long endDate,@Nullable String customer,
                               @Nullable String manufactor, Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FlowDoc> flowDocList = flowDocDao.getFlowDocList(devNo, startDate, endDate, customer, manufactor);
                Message message = handler.obtainMessage();
                message.what = Constants.QUERY_FLOW_DOC;
                message.obj = flowDocList;
                handler.sendMessage(message);
            }
        }).start();
    }

    public void getFlowDocList(List<CalInforItem> calInforItems, Handler handler ){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FlowDoc> flowDocList=new ArrayList<FlowDoc>();
                for (CalInforItem calInforItem : calInforItems) {
                    final CalInfor calInfor = calInforItem.getCalInfor();
                    //根据devNo,date取第一个值（满足此条件只能有一个值）
                    final List<FlowDoc> docList = flowDocDao.getFlowDocList(calInfor.getDevNo(), calInfor.getDate());
                    FlowDoc flowDoc=null;
                    if(docList!=null && docList.size()>0)
                        flowDoc=docList.get(0);
                    flowDocList.add(flowDoc);
                }
                Message message = handler.obtainMessage();
                message.what = Constants.QUERY_SEARCH_FLOW_DOCS;
                message.obj = flowDocList;
                handler.sendMessage(message);
            }
        }).start();
    }

//    public void getFlowDocList(String devNo,Handler handler){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<FlowDoc> flowDocList = flowDocDao.getFlowDocList(devNo);
//                Message message = handler.obtainMessage();
//                message.what = Constants.QUERY_FLOW_DOC;
//                message.obj = flowDocList;
//                handler.sendMessage(message);
//            }
//        }).start();
//    }
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
    public void deleteFlowDoc(FlowDoc flowDoc, Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int num=flowDocDao.deleteFlowDoc(flowDoc);
                Message message = handler.obtainMessage();
                message.what = Constants.Delet_FLOW_DOC;
                message.obj = num;
                handler.sendMessage(message);
            }
        }).start();
    }
}
