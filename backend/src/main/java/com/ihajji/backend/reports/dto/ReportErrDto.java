package com.ihajji.backend.reports.dto;

import org.apache.hc.core5.http.HttpStatus;

public class ReportErrDto {
    private int code;
    private  String message ;
    public ReportErrDto(){
        this.code= HttpStatus.SC_OK;
        this.message= "this ops went smouth" ;
    }
     public ReportErrDto(int code, String message  ){
        this.code= code;
        this.message= message ;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
}
