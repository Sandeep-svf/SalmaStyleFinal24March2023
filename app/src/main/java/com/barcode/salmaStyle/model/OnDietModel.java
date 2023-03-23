package com.barcode.salmaStyle.model;

import com.barcode.salmaStyle.response.DietResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OnDietModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DietResponse> getDietResponseList() {
        return dietResponseList;
    }

    public void setDietResponseList(List<DietResponse> dietResponseList) {
        this.dietResponseList = dietResponseList;
    }

    @SerializedName("data")
    @Expose
    private List<DietResponse> dietResponseList = null;

}
