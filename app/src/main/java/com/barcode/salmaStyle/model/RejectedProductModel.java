package com.barcode.salmaStyle.model;

import com.barcode.salmaStyle.response.ProductRejectedResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RejectedProductModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<ProductRejectedResponse> productRejectedResponseList = null;

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

    public List<ProductRejectedResponse> getProductRejectedResponseList() {
        return productRejectedResponseList;
    }

    public void setProductRejectedResponseList(List<ProductRejectedResponse> productRejectedResponseList) {
        this.productRejectedResponseList = productRejectedResponseList;
    }
}
