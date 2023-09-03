package com.yzh.ndlljdapp.entity;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CalInfor {
    private long id;
    private String devNo="";  //赋值默认值，不然为null
    private long date;
    private int type;
    private float accuracy;    //精度，百分数
    private float uncertainty; //不确定度，百分数
    private String user ="";

    public CalInfor() {
    }

    public CalInfor(String devNo, long date, int type, String user) {
        this.devNo = devNo;
        this.date = date;
        this.type = type;
        this.user = user;
    }

    @Override
    public String toString() {
        DateFormat format=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return "CalInfor{" +
                "id=" + id +
                ", devNo='" + devNo + '\'' +
                ", date=" + format.format(new Date(date)) +
                ", type=" + type +
                ", accuracy=" + accuracy +
                ", uncertainty=" + uncertainty +
                ", User='" + user + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public float getUncertainty() {
        return uncertainty;
    }

    public void setUncertainty(float uncertainty) {
        this.uncertainty = uncertainty;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
