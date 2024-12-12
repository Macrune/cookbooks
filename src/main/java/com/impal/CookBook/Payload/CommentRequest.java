package com.impal.CookBook.Payload;

import jakarta.validation.constraints.NotBlank;

public class CommentRequest {

    @NotBlank
    private String authorId;

    private String commentBody;

    public CommentRequest() {
    }

    public CommentRequest(String authorId, String commentBody) {
        this.authorId = authorId;
        this.commentBody = commentBody;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }
    
    
}
