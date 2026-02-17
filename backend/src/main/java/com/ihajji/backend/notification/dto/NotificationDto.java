package com.ihajji.backend.notification.dto;



public class NotificationDto {
    private Long id;
    private String message;
    private String nature;
    private String senderUsername;
    private String receiverUsername;

    public NotificationDto(Long id, String message, String nature, String senderUsername, String receiverUsername) {
        this.id = id;
        this.message = message;
        this.nature = nature;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
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

   public String getReceiverUsername() {
       return receiverUsername;
   }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
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
   
}
