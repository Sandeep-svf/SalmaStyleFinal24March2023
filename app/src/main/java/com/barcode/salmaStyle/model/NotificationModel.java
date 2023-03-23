package com.barcode.salmaStyle.model;

import com.barcode.salmaStyle.response.NotificationCount;
import com.barcode.salmaStyle.response.NotificationResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationModel {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("data")
        @Expose
        private List<NotificationResponse> notificationResponseList = null;

        public List<NotificationResponse> getNotificationResponseList() {
                return notificationResponseList;
        }

        public void setNotificationResponseList(List<NotificationResponse> notificationResponseList) {
                this.notificationResponseList = notificationResponseList;
        }

        @SerializedName("count")
        @Expose
        private NotificationCount notificationCount;

        public NotificationCount getNotificationCount() {
                return notificationCount;
        }

        public void setNotificationCount(NotificationCount notificationCount) {
                this.notificationCount = notificationCount;
        }

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


}
