package com.ihajji.backend.notification.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;

import com.ihajji.backend.notification.entity.NotificationEntity;

public class NotificationErrDto {
    private int code;
    private String message;
    private List<NotificationEntity> body;

    public NotificationErrDto() {
        this.code = HttpStatus.SC_OK;
        this.message = "this app went good";
        this.body = new ArrayList<NotificationEntity>();
    }

    public NotificationErrDto(int code, String message) {
        this.code = code;
        this.message = message;
        this.body = new ArrayList<NotificationEntity>();

    }
      public NotificationErrDto(List<NotificationEntity> body ) {
        this.code = HttpStatus.SC_OK;
        this.message = "this app went good";
        this.body = body;

    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    public List<NotificationEntity> getBody() {
        return body;
    }
    public void setBody(List<NotificationEntity> body) {
        this.body = body;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
