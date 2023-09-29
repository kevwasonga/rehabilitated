package com.daghlas.myrehab.Model;

/**
 * DEVELOPER: daghlaskaire58@gmail.com
 * For KELVIN WASONGA
 * JULY 2023
 */

public class KModel {

    String fullName, rehab,dateEnrolled, residence, guardian, gender, health, fingerprint, faceID;

    public KModel(String fullName, String rehab, String dateEnrolled, String residence, String guardian, String gender,
                  String health, String fingerprint, String faceID) {
        this.fullName = fullName;
        this.rehab = rehab;
        this.dateEnrolled = dateEnrolled;
        this.residence = residence;
        this.guardian = guardian;
        this.gender = gender;
        this.health = health;
        this.fingerprint = fingerprint;
        this.faceID = faceID;
    }

    public KModel(){

    }

    public String getFullName() {
        return fullName;
    }

    public String getRehab() {
        return rehab;
    }

    public void setRehab(String rehab) {
        this.rehab = rehab;
    }

    public String getDateEnrolled() {
        return dateEnrolled;
    }

    public String getResidence() {
        return residence;
    }

    public String getGuardian() {
        return guardian;
    }

    public String getGender() {
        return gender;
    }

    public String getHealth() {
        return health;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public String getFaceID() {
        return faceID;
    }

}
