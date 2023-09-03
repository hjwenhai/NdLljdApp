package com.yzh.ndlljdapp.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "flowDoc")
public class FlowDoc {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private long id;

    @NonNull
    @ColumnInfo(name = "devNo")
    private String devNo;

    @NonNull
    @ColumnInfo(name = "date")
    private long date;

    @NonNull
    @ColumnInfo(name = "customer")
    private String customer;

    @NonNull
    @ColumnInfo(name = "manufactor")
    private String manufactor;

    @NonNull
    @ColumnInfo(name = "fileName")
    private String fileName;

    public  FlowDoc(){}
    /**
     * 方便直接构造对象使用
     * @param devNo
     * @param date
     * @param customer
     * @param manufactor
     * @param fileName
     */
    @Ignore
    public FlowDoc(@NonNull String devNo, long date, @NonNull String customer, @NonNull String manufactor, @NonNull String fileName) {
        this.devNo = devNo;
        this.date = date;
        this.customer = customer;
        this.manufactor = manufactor;
        this.fileName = fileName;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(@NonNull String devNo) {
        this.devNo = devNo;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @NonNull
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(@NonNull String customer) {
        this.customer = customer;
    }

    @NonNull
    public String getManufactor() {
        return manufactor;
    }

    public void setManufactor(@NonNull String manufactor) {
        this.manufactor = manufactor;
    }

    @NonNull
    public String getFileName() {
        return fileName;
    }

    public void setFileName(@NonNull String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        DateFormat format=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return "FlowDoc{" +
                "id=" + id +
                ", devNo='" + devNo + '\'' +
                ", date=" + format.format(new Date(date)) +
                ", customer='" + customer + '\'' +
                ", manufactor='" + manufactor + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
