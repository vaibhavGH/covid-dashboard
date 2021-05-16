package org.example.coviddashboard.security.beans;

public class MessageResponse {
    private String message;

    public MessageResponse(){}

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
