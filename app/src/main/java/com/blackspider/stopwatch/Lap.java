package com.blackspider.stopwatch;

public class Lap {
    private final int lapNumber;
    private final String splitTime;
    private final String totalTime;

    public Lap(int lapNumber, String splitTime, String totalTime) {
        this.lapNumber = lapNumber;
        this.splitTime = splitTime;
        this.totalTime = totalTime;
    }

    public int getLapNumber() {
        return lapNumber;
    }

    public String getSplitTime() {
        return splitTime;
    }

    public String getTotalTime() {
        return totalTime;
    }
}
