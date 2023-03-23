package com.barcode.salmaStyle.model;

import com.barcode.salmaStyle.response.OccassionResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OccassionModel {

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

    public List<OccassionResponse> getOccassionResponseList() {
        return occassionResponseList;
    }

    public void setOccassionResponseList(List<OccassionResponse> occassionResponseList) {
        this.occassionResponseList = occassionResponseList;
    }

    @SerializedName("data")
    @Expose
    private List<OccassionResponse> occassionResponseList = null;

}
