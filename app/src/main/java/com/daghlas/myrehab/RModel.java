package com.daghlas.myrehab;

/**
 * DEVELOPER: daghlaskaire58@gmail.com
 * For KELVIN WASONGA
 * JULY 2023
 */

public class RModel {

    String rehabName, rehabDirector, rehabPhone, dateAdded, rehabLocation, rehabEmail;

    public RModel(String rehabName, String rehabDirector, String rehabPhone, String dateAdded, String rehabLocation, String rehabEmail) {
        this.rehabName = rehabName;
        this.rehabDirector = rehabDirector;
        this.rehabPhone = rehabPhone;
        this.dateAdded = dateAdded;
        this.rehabLocation = rehabLocation;
        this.rehabEmail = rehabEmail;
    }

    public RModel(){

    }

    public String getRehabName() {
        return rehabName;
    }

    public String getRehabDirector() {
        return rehabDirector;
    }

    public String getRehabPhone() {
        return rehabPhone;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getRehabLocation() {
        return rehabLocation;
    }

    public String getRehabEmail() {
        return rehabEmail;
    }

}
