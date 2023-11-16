package com.yzh.ndlljdapp.entity;

import java.util.List;

public class ResponseCalInforList {
    private String error_string;
    private int error_code;
    private List<CalInfor> data;

    public ResponseCalInforList(String error_string, int error_code, List<CalInfor> data) {
        this.error_string = error_string;
        this.error_code = error_code;
        this.data = data;
    }

    public String getError_string() {
        return error_string;
    }

    public void setError_string(String error_string) {
        this.error_string = error_string;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<CalInfor> getData() {
        return data;
    }

    public void setData(List<CalInfor> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseCalData{" +
                "error_string='" + error_string + '\'' +
                ", error_code=" + error_code +
                ", data=" + data +
                '}';
    }
}
