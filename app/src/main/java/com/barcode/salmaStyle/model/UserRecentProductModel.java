package com.barcode.salmaStyle.model;

import com.barcode.salmaStyle.response.UserRecentProductResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserRecentProductModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<UserRecentProductResponse> userRecentProductResponseList = null;

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

    public List<UserRecentProductResponse> getUserRecentProductResponseList() {
        return userRecentProductResponseList;
    }

    public void setUserRecentProductResponseList(List<UserRecentProductResponse> userRecentProductResponseList) {
        this.userRecentProductResponseList = userRecentProductResponseList;
    }
}
