package com.example.socialization.DataCollection;

public class DataInterface {

    private long date = 0;
    private int callType = 0;
    private long duration = 0;
    private String name = null;
    private String number = null;
    private double lat = 0.0;
    private double lon = 0.0;
    private int socialStatus = 0;
//    private List<String> packageName= null;

    public DataInterface(){
    }


    public void setName(String contactName) {
        this.name = contactName;
    }

    public String getName() {
        return name;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setCallType(int setCallType) {
        this.callType = setCallType;
    }

    public int getGetCallType() {
        return callType;
    }


    public void setNumber(String phoneNumber) {
        this.number = phoneNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getSocialStatus() {
        return socialStatus;
    }

    public void setSocialStatus(int socialStatus) {
        this.socialStatus = socialStatus;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
//    public void setPackage(List<String> packages){
//     this.packageName = packages;
//    }
//
//    public List<String> getPackageName() {
//        return packageName;
//    }



}
