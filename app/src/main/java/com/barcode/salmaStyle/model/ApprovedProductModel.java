package com.barcode.salmaStyle.model;

import com.barcode.salmaStyle.response.ProductApprovedResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApprovedProductModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<ProductApprovedResponse> productApprovedResponseList = null;

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

    public List<ProductApprovedResponse> getProductApprovedResponseList() {
        return productApprovedResponseList;
    }

    public void setProductApprovedResponseList(List<ProductApprovedResponse> productApprovedResponseList) {
        this.productApprovedResponseList = productApprovedResponseList;
    }
}
