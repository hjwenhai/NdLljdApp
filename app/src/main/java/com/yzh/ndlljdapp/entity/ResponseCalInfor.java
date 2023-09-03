package com.yzh.ndlljdapp.entity;


public class ResponseCalInfor {
    private String error_string;
    private int error_code;
    private CalInfor data;

    public ResponseCalInfor(String error_string, int error_code, CalInfor data) {
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

    public CalInfor getData() {
        return data;
    }

    public void setData(CalInfor data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseCalInfor{" +
                "error_string='" + error_string + '\'' +
                ", error_code=" + error_code +
                ", data=" + data +
                '}';
    }
}
