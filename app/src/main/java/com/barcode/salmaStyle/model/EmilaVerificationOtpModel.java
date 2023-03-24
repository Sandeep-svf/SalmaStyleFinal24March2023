
package com.barcode.salmaStyle.model;


import com.google.gson.annotations.SerializedName;


public class EmilaVerificationOtpModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("otp")
    private Long mOtp;
    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Long getOtp() {
        return mOtp;
    }

    public void setOtp(Long otp) {
        mOtp = otp;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
