package com.ihajji.backend.notification.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;


public class NotificationErrDto {
    private int code;
    private String message;
    private List<NotificationDto> body;

    public NotificationErrDto() {
        this.code = HttpStatus.SC_OK;
        this.message = "this app went good";
        this.body = new ArrayList<NotificationDto>();
    }

    public NotificationErrDto(int code, String message) {
        this.code = code;
        this.message = message;
        this.body = new ArrayList<NotificationDto>();

    }
      public NotificationErrDto(List<NotificationDto> body ) {
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
    public List<NotificationDto> getBody() {
        return body;
    }
    public void setBody(List<NotificationDto> body) {
        this.body = body;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
