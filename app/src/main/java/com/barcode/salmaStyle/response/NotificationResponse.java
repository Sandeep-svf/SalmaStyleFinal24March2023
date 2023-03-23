package com.barcode.salmaStyle.response;

import java.util.ArrayList;

public class NotificationResponse {

    public int id;
    public String created_at;
    public String updated_at;
    public String title;
    public String عنوان;
    public String message;
    public String پیام;
    public String barcode;
    public String recieved_date;
    public String product_image;

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getعنوان() {
        return عنوان;
    }

    public void setعنوان(String عنوان) {
        this.عنوان = عنوان;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getپیام() {
        return پیام;
    }

    public void setپیام(String پیام) {
        this.پیام = پیام;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getRecieved_date() {
        return recieved_date;
    }

    public void setRecieved_date(String recieved_date) {
        this.recieved_date = recieved_date;
    }

    public Object getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Object created_by) {
        this.created_by = created_by;
    }

    public Object getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(Object updated_by) {
        this.updated_by = updated_by;
    }

    public Object getSender() {
        return sender;
    }

    public void setSender(Object sender) {
        this.sender = sender;
    }

    public ArrayList<Integer> getRecipient() {
        return recipient;
    }

    public void setRecipient(ArrayList<Integer> recipient) {
        this.recipient = recipient;
    }

    public Object created_by;
    public Object updated_by;
    public Object sender;
    public ArrayList<Integer> recipient;
}
