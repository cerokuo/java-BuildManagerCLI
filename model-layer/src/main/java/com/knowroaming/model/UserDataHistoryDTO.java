package com.knowroaming.model;

/**
 * Created by cerokuo on 03/04/2017.
 */
public class UserDataHistoryDTO {

    private String userID;
    private String dataTypeID;
    private String starDate;
    private String endDate;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDataTypeID() {
        return dataTypeID;
    }

    public void setDataTypeID(String dataTypeID) {
        this.dataTypeID = dataTypeID;
    }

    public String getStarDate() {
        return starDate;
    }

    public void setStarDate(String starDate) {
        this.starDate = starDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getUserID());
        stringBuilder.append(" - ");
        stringBuilder.append(this.getDataTypeID());
        stringBuilder.append(" - ");
        stringBuilder.append(this.getStarDate());
        return stringBuilder.toString();
    }
}
