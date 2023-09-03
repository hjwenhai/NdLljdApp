package com.yzh.ndlljdapp.entity;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CalData {
    private long id;
    private long infoId;
    private long startTime;
    private long endTime;
    private long calTime;
    private float density;
    private float temp;
    private float instantFlow;
    private float calTotalFlow;
    private float standardTotalFlow;
    private float E;

    public CalData() {
    }


    public CalData(long infoId, long startTime, long endTime, long calTime, float density, float temp, float instantFlow, float calTotalFlow, float standardTotalFlow, float e) {
        this.infoId = infoId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.calTime = calTime;
        this.density = density;
        this.temp = temp;
        this.instantFlow = instantFlow;
        this.calTotalFlow = calTotalFlow;
        this.standardTotalFlow = standardTotalFlow;
        E = e;
    }

    @Override
    public String toString() {
        DateFormat format=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return "CalData{" +
                "id=" + id +
                ", infoId=" + infoId +
                ", startTime=" + format.format(new Date(startTime)) +
                ", endTime=" + format.format(new Date(endTime)) +
                ", calTime=" + calTime +
                ", density=" + density +
                ", temp=" + temp +
                ", instantFlow=" + instantFlow +
                ", calTotalFlow=" + calTotalFlow +
                ", standardTotalFlow=" + standardTotalFlow +
                ", E=" + E +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInfoId() {
        return infoId;
    }

    public void setInfoId(long infoId) {
        this.infoId = infoId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getCalTime() {
        return calTime;
    }

    public void setCalTime(long calTime) {
        this.calTime = calTime;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getInstantFlow() {
        return instantFlow;
    }

    public void setInstantFlow(float instantFlow) {
        this.instantFlow = instantFlow;
    }

    public float getCalTotalFlow() {
        return calTotalFlow;
    }

    public void setCalTotalFlow(float calTotalFlow) {
        this.calTotalFlow = calTotalFlow;
    }

    public float getStandardTotalFlow() {
        return standardTotalFlow;
    }

    public void setStandardTotalFlow(float standardTotalFlow) {
        this.standardTotalFlow = standardTotalFlow;
    }

    public float getE() {
        return E;
    }

    public void setE(float e) {
        E = e;
    }
}

