package com.barcode.salmaStyle.model;

import com.barcode.salmaStyle.response.CountryCodeResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryCodeMOdel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<CountryCodeResponse> countryCodeResponseList = null;

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

    public List<CountryCodeResponse> getCountryCodeResponseList() {
        return countryCodeResponseList;
    }

    public void setCountryCodeResponseList(List<CountryCodeResponse> countryCodeResponseList) {
        this.countryCodeResponseList = countryCodeResponseList;
    }
}
