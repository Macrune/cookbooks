package com.impal.CookBook.Payload;

public class CommentResponse {

    private UserInfoResponse author;
    private String body;

    public CommentResponse(UserInfoResponse author, String body) {
        this.author = author;
        this.body = body;
    }

    public UserInfoResponse getAuthor() {
        return author;
    }

    public void setAuthor(UserInfoResponse author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
    
}
