package com.yzh.ndlljdapp.entity;

import java.util.List;

public class ResponseCalData {
    private String error_string;
    private int error_code;
    private List<CalData> data;

    public ResponseCalData(String error_string, int error_code, List<CalData> data) {
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

    public List<CalData> getData() {
        return data;
    }

    public void setData(List<CalData> data) {
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
