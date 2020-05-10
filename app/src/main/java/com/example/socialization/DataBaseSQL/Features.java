package com.example.socialization.DataBaseSQL;


/**
 *   ID INTEGER PRIMARY KEY AUTOINCREMENT,
 *   DAY INTEGER,
 *   HOUR INTEGER,
 *   LATITUDE REAL,
 *   LONGITUDE REAL,
 *   CALL_TYPE INTEGER,
 *   CONTACT_NAME TEXT,
 *   CONTACT_PHONE TEXT,
 *   DURATION INTEGER,
 *   APP_PACKAGE TEXT;
 */

public class Features {
    private long date;
    private int callType;
    private String name;
    private String phone;
    private int duration;
    private double lat;
    private double lon;
    private int socialStatus;
    //    String packagename;

    public Features(long date, int callType, String name, String phone, int duration, double lat, double lon, int socialStatus) {
        this.date = date;
        this.callType = callType;
        this.name = name;
        this.phone = phone;
        this.duration = duration;
        this.lat = lat;
        this.lon = lon;
        this.socialStatus = socialStatus;
    }
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getSocialStatus() {
        return socialStatus;
    }

    public void setSocialStatus(int socialStatus) {
        this.socialStatus = socialStatus;
    }
}
