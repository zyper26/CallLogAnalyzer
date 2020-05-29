package com.example.socialization.CallFeatures;

public class CallLogInfo {
    private String name;
    private String number;
    private String callType;
    private long date;
    private long duration;
    private Boolean socialStatus = Boolean.FALSE;
    private long callIncomingOutgoingCount = 0, callIncomingOutgoingDuration = 0;
    private long callIncomingCount = 0, callIncomingDuration = 0;
    private long callOutgoingCount = 0, callOutgoingDuration = 0;
    private long totalIncomingOutgoingCount = 0, totalIncomingOutgoingDuration = 0;
    private long totalIncomingCount = 0, totalIncomingDuration = 0;
    private long totalOutgoingCount = 0, totalOutgoingDuration = 0;
    private float individualScore = 0, globalScore = 0;
    private float weekDayBias = 0, weekEndBias = 0, weekDayDuration = 0, weekEndDuration = 0;
    private float knownBias = 0, unknownBias = 0;
    private float pastSocializingContactBias = 0;
    private float finalIndividualScore = 0, finalGlobalScore = 0;
    private long totalDistinctContacts = 0;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSocialStatus() {
        return socialStatus;
    }

    public void setSocialStatus(Boolean socialStatus) {
        this.socialStatus = socialStatus;
    }

    public float getFinalGlobalScore() {
        return finalGlobalScore;
    }

    public void setFinalGlobalScore(float finalGlobalScore) {
        this.finalGlobalScore = finalGlobalScore;
    }

    public long getCallIncomingOutgoingCount() {
        return callIncomingOutgoingCount;
    }

    public void setCallIncomingOutgoingCount(long callIncomingOutgoingCount) {
        this.callIncomingOutgoingCount = callIncomingOutgoingCount;
    }

    public long getCallIncomingOutgoingDuration() {
        return callIncomingOutgoingDuration;
    }

    public void setCallIncomingOutgoingDuration(long callIncomingOutgoingDuration) {
        this.callIncomingOutgoingDuration = callIncomingOutgoingDuration;
    }

    public long getCallIncomingDuration() {
        return callIncomingDuration;
    }

    public void setCallIncomingDuration(long callIncomingDuration) {
        this.callIncomingDuration = callIncomingDuration;
    }

    public long getCallIncomingCount() {
        return callIncomingCount;
    }

    public void setCallIncomingCount(long callIncomingCount) {
        this.callIncomingCount = callIncomingCount;
    }

    public long getCallOutgoingCount() {
        return callOutgoingCount;
    }

    public void setCallOutgoingCount(long callOutgoingCount) {
        this.callOutgoingCount = callOutgoingCount;
    }

    public long getTotalIncomingOutgoingDuration() {
        return totalIncomingOutgoingDuration;
    }

    public void setTotalIncomingOutgoingDuration(long totalIncomingOutgoingDuration) {
        this.totalIncomingOutgoingDuration = totalIncomingOutgoingDuration;
    }

    public long getCallOutgoingDuration() {
        return callOutgoingDuration;
    }

    public void setCallOutgoingDuration(long callOutgoingDuration) {
        this.callOutgoingDuration = callOutgoingDuration;
    }

    public long getTotalIncomingOutgoingCount() {
        return totalIncomingOutgoingCount;
    }

    public void setTotalIncomingOutgoingCount(long totalIncomingOutgoingCount) {
        this.totalIncomingOutgoingCount = totalIncomingOutgoingCount;
    }

    public long getTotalIncomingCount() {
        return totalIncomingCount;
    }

    public void setTotalIncomingCount(long totalIncomingCount) {
        this.totalIncomingCount = totalIncomingCount;
    }

    public long getTotalIncomingDuration() {
        return totalIncomingDuration;
    }

    public void setTotalIncomingDuration(long totalIncomingDuration) {
        this.totalIncomingDuration = totalIncomingDuration;
    }

    public long getTotalOutgoingDuration() {
        return totalOutgoingDuration;
    }

    public void setTotalOutgoingDuration(long totalOutgoingDuration) {
        this.totalOutgoingDuration = totalOutgoingDuration;
    }

    public long getTotalOutgoingCount() {
        return totalOutgoingCount;
    }

    public void setTotalOutgoingCount(long totalOutgoingCount) {
        this.totalOutgoingCount = totalOutgoingCount;
    }

    public float getIndividualScore() {
        return individualScore;
    }

    public void setIndividualScore(float individualScore) {
        this.individualScore = individualScore;
    }

    public float getGlobalScore() {
        return globalScore;
    }

    public void setGlobalScore(float globalScore) {
        this.globalScore = globalScore;
    }

    public float getWeekEndBias() {
        return weekEndBias;
    }

    public void setWeekEndBias(float weekEndBias) {
        this.weekEndBias = weekEndBias;
    }

    public float getWeekDayBias() {
        return weekDayBias;
    }

    public void setWeekDayBias(float weekDayBias) {
        this.weekDayBias = weekDayBias;
    }

    public float getKnownBias() {
        return knownBias;
    }

    public void setKnownBias(float knownBias) {
        this.knownBias = knownBias;
    }

    public float getUnknownBias() {
        return unknownBias;
    }

    public void setUnknownBias(float unknownBias) {
        this.unknownBias = unknownBias;
    }

    public float getPastSocializingContactBias() {
        return pastSocializingContactBias;
    }

    public void setPastSocializingContactBias(float pastSocializingContactBias) {
        this.pastSocializingContactBias = pastSocializingContactBias;
    }

    public float getFinalIndividualScore() {
        return finalIndividualScore;
    }

    public void setFinalIndividualScore(float finalIndividualScore) {
        this.finalIndividualScore = finalIndividualScore;
    }

    public long getTotalDistinctContacts() {
        return totalDistinctContacts;
    }

    public void setTotalDistinctContacts(long totalDistinctContacts) {
        this.totalDistinctContacts = totalDistinctContacts;
    }

    public float getWeekEndDuration() {
        return weekEndDuration;
    }

    public void setWeekEndDuration(float weekEndDuration) {
        this.weekEndDuration = weekEndDuration;
    }

    public float getWeekDayDuration() {
        return weekDayDuration;
    }

    public void setWeekDayDuration(float weekDayDuration) {
        this.weekDayDuration = weekDayDuration;
    }
}
