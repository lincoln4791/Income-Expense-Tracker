package com.example.dailyexpensemanager.model;

public class MC_Posts {
   public String postDescription;
   public String postCategory;
   public String postType;
   public String postAmount;
   public String postYear;
   public String postMonth;
   public String postDay;
   public String postTime;
   public String timeStamp;
   public String postDateTime;

    public MC_Posts(String postDescription, String postCategory, String postType, String postAmount, String postYear,
                    String postMonth, String postDay, String postTime, String timeStamp, String postDateTime) {
        this.postDescription = postDescription;
        this.postCategory = postCategory;
        this.postType = postType;
        this.postAmount = postAmount;
        this.postYear = postYear;
        this.postMonth = postMonth;
        this.postDay = postDay;
        this.postTime = postTime;
        this.timeStamp = timeStamp;
        this.postDateTime = postDateTime;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostAmount() {
        return postAmount;
    }

    public void setPostAmount(String postAmount) {
        this.postAmount = postAmount;
    }

    public String getPostYear() {
        return postYear;
    }

    public void setPostYear(String postYear) {
        this.postYear = postYear;
    }

    public String getPostMonth() {
        return postMonth;
    }

    public void setPostMonth(String postMonth) {
        this.postMonth = postMonth;
    }

    public String getPostDay() {
        return postDay;
    }

    public void setPostDay(String postDay) {
        this.postDay = postDay;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPostDateTime() {
        return postDateTime;
    }

    public void setPostDateTime(String postDateTime) {
        this.postDateTime = postDateTime;
    }
}
