package com.yzh.ndlljdapp.entity;
//检定表辅助信息类
public class ValidationTableAuxMessage {

    private String customer;
    private String manufactor;

    public ValidationTableAuxMessage(String customer, String manufactor) {
        this.customer = customer;
        this.manufactor = manufactor;
    }
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getManufactor() {
        return manufactor;
    }

    public void setManufactor(String manufactor) {
        this.manufactor = manufactor;
    }

    @Override
    public String toString() {
        return "ValidationTableAuxMessage{" +
                "customer='" + customer + '\'' +
                ", manufactor='" + manufactor + '\'' +
                '}';
    }
}
