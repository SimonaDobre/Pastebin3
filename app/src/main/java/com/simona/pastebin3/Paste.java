package com.simona.pastebin3;

public class Paste {

    private String postID;
    private String postTitle;
    private String postContent;
    private String postDate;
    private String postExpirationDate;
    private int numberOfViews;
    private String userName;
    private String userID;

    public Paste() {
    }

    public Paste(String postID, String postTitle, String postContent,
                 String postDate, String postExpirationDate,
                 int numberOfViews, String userName, String userID) {
        this.postID = postID;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postDate = postDate;
        this.postExpirationDate = postExpirationDate;
        this.numberOfViews = numberOfViews;
        this.userName = userName;
        this.userID = userID;
    }

    public String getPostID() {
        return postID;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public String getPostDate() {
        return postDate;
    }

    public String getPostExpirationDate() {
        return postExpirationDate;
    }

    public int getNumberOfViews() {
        return numberOfViews;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserID() {
        return userID;
    }
}
