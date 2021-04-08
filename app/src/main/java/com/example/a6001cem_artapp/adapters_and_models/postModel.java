package com.example.a6001cem_artapp.adapters_and_models;

public class postModel {

    private String postCommentsNum, postID, postTitle, postDescription, postImage, postTimestamp, userID, userPfp, userName, postLikes, reportsNum, is_a_challenge;

    public  postModel(){

    }

    public postModel(String postCommentsNum, String postID, String postTitle, String postDescription, String postImage, String postTimestamp, String userID, String userPfp, String userName, String postLikes, String reportsNum, String is_a_challenge) {
        this.postCommentsNum = postCommentsNum;
        this.postID = postID;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.postImage = postImage;
        this.postTimestamp = postTimestamp;
        this.userID = userID;
        this.userPfp = userPfp;
        this.userName = userName;
        this.postLikes = postLikes;
        this.reportsNum = reportsNum;
        this.is_a_challenge = is_a_challenge;
    }

    public String getPostCommentsNum() {
        return postCommentsNum;
    }

    public String getPostID() {
        return postID;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public String getPostImage() {
        return postImage;
    }

    public String getPostTimestamp() {
        return postTimestamp;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserPfp() {
        return userPfp;
    }

    public String getUserName() {
        return userName;
    }

    public String getPostLikes() {
        return postLikes;
    }

    public String getReportsNum() {
        return reportsNum;
    }
}
