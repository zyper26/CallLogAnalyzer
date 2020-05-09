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
    int day;
    int hour;
    double lat;
    double lon;
    int callType;
    String name;
    String phone;
    int duration;
    String packagename;

    public Features(){

    }

    public Features(int day, int hour,double lat, double lon, int callType, String name, String phone, int duration, String packagename){
        this.day = day;
        this.hour = hour;
        this.lat = lat;
        this.lon = lon;
        this.callType = callType;
        this.name = name;
        this.phone = phone;
        this.duration = duration;
        this.packagename = packagename;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public int getCallType() {
        return callType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public String getName() {
        return name;
    }

    public String getPackagename() {
        return packagename;
    }

    public String getPhone() {
        return phone;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
