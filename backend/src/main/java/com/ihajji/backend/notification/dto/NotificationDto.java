package com.ihajji.backend.notification.dto;




public class NotificationDto {
    private Long id;
    private String message;
    private String nature;
    private String senderUsername;
    private String receiverUsername;
    private Boolean read;
    private String senderprofile;

    public NotificationDto(Long id, String message, String nature, String senderUsername, String receiverUsername, Boolean read ,String senderprofile) {
        this.id = id;
        this.message = message;
        this.nature = nature;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.read = read;
        this.senderprofile=senderprofile;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getNature() {
        return nature;
    }

    public String getSenderprofile() {
        return senderprofile;
    }
    

   public String getReceiverUsername() {
       return receiverUsername;
   }

    public String getSenderUsername() {
        return senderUsername;
    }

    public Boolean getRead() {
        return read;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderprofile(String senderprofile) {
        this.senderprofile = senderprofile;
    }
    

    public void setNature(String nature) {
        this.nature = nature;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }
   
}
