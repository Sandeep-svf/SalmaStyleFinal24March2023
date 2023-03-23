package com.barcode.salmaStyle.model;

import com.barcode.salmaStyle.response.PendingProductResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PendingProductModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<PendingProductResponse> pendingProductResponseList = null;

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

    public List<PendingProductResponse> getPendingProductResponseList() {
        return pendingProductResponseList;
    }

    public void setPendingProductResponseList(List<PendingProductResponse> pendingProductResponseList) {
        this.pendingProductResponseList = pendingProductResponseList;
    }
}
