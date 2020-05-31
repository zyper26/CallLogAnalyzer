package com.example.socialization.CallFeatures;

public class CallLogInfo {
    private String name;
    private String number;
    private String callType;
    private long date;
    private long duration;
    private Boolean socialStatus = Boolean.FALSE;
    private float weekFrequency1, weekDuration1;
    private float weekFrequency2, weekDuration2;
    private float weekFrequency3, weekDuration3;
    private float weekFrequency4, weekDuration4;
    private float weekFrequency5, weekDuration5;
    private float weekFrequency6, weekDuration6;
    private float weekFrequency7, weekDuration7;
    private float weekFrequency8, weekDuration8;
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

    public float getWeekFrequency1() {
        return weekFrequency1;
    }

    public void setWeekFrequency1(float weekFrequency1) {
        this.weekFrequency1 = weekFrequency1;
    }

    public float getWeekDuration1() {
        return weekDuration1;
    }

    public void setWeekDuration1(float weekDuration1) {
        this.weekDuration1 = weekDuration1;
    }

    public float getWeekDuration2() {
        return weekDuration2;
    }

    public void setWeekDuration2(float weekDuration2) {
        this.weekDuration2 = weekDuration2;
    }

    public float getWeekFrequency2() {
        return weekFrequency2;
    }

    public void setWeekFrequency2(float weekFrequency2) {
        this.weekFrequency2 = weekFrequency2;
    }

    public float getWeekFrequency3() {
        return weekFrequency3;
    }

    public void setWeekFrequency3(float weekFrequency3) {
        this.weekFrequency3 = weekFrequency3;
    }

    public float getWeekDuration3() {
        return weekDuration3;
    }

    public void setWeekDuration3(float weekDuration3) {
        this.weekDuration3 = weekDuration3;
    }

    public float getWeekDuration4() {
        return weekDuration4;
    }

    public void setWeekDuration4(float weekDuration4) {
        this.weekDuration4 = weekDuration4;
    }

    public float getWeekFrequency4() {
        return weekFrequency4;
    }

    public void setWeekFrequency4(float weekFrequency4) {
        this.weekFrequency4 = weekFrequency4;
    }

    public float getWeekFrequency5() {
        return weekFrequency5;
    }

    public void setWeekFrequency5(float weekFrequency5) {
        this.weekFrequency5 = weekFrequency5;
    }

    public float getWeekDuration5() {
        return weekDuration5;
    }

    public void setWeekDuration5(float weekDuration5) {
        this.weekDuration5 = weekDuration5;
    }

    public float getWeekDuration6() {
        return weekDuration6;
    }

    public void setWeekDuration6(float weekDuration6) {
        this.weekDuration6 = weekDuration6;
    }

    public float getWeekFrequency6() {
        return weekFrequency6;
    }

    public void setWeekFrequency6(float weekFrequency6) {
        this.weekFrequency6 = weekFrequency6;
    }

    public float getWeekFrequency7() {
        return weekFrequency7;
    }

    public void setWeekFrequency7(float weekFrequency7) {
        this.weekFrequency7 = weekFrequency7;
    }

    public float getWeekDuration7() {
        return weekDuration7;
    }

    public void setWeekDuration7(float weekDuration7) {
        this.weekDuration7 = weekDuration7;
    }

    public float getWeekDuration8() {
        return weekDuration8;
    }

    public void setWeekDuration8(float weekDuration8) {
        this.weekDuration8 = weekDuration8;
    }

    public float getWeekFrequency8() {
        return weekFrequency8;
    }

    public void setWeekFrequency8(float weekFrequency8) {
        this.weekFrequency8 = weekFrequency8;
    }
}
