package com.example.a6001cem_artapp.adapters_and_models;

public class commentModel {

    private String commentID, commentTimeStamp, commentText, userId, userName;

    public commentModel(){

    }

    public commentModel(String commentID, String commentTimeStamp, String commentText, String userId, String userName) {
        this.commentID = commentID;
        this.commentTimeStamp = commentTimeStamp;
        this.commentText = commentText;
        this.userId = userId;
        this.userName = userName;
    }

    public String getCommentID() {
        return commentID;
    }

    public String getCommentTimeStamp() {
        return commentTimeStamp;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
