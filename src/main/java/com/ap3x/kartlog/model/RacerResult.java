package com.ap3x.kartlog.model;

import java.time.Duration;
import java.util.Objects;

public class RacerResult {

    private Integer pilotCode;
    private String pilotName;
    private Long raceTotalTime;
    private Integer lastCompleteLap;
    private Float lapSpeedSum;
    private Duration timeAfterFirstRacer;

    public RacerResult() {
    }

    public RacerResult(Integer pilotCode, String pilotName, Long raceTotalTime, Integer lastCompleteLap, Float lapSpeedSum) {
        this.pilotCode = pilotCode;
        this.pilotName = pilotName;
        this.raceTotalTime = raceTotalTime;
        this.lastCompleteLap = lastCompleteLap;
        this.lapSpeedSum = lapSpeedSum;
        this.timeAfterFirstRacer = null;
    }

    public Integer getPilotCode() {
        return pilotCode;
    }

    public void setPilotCode(final Integer pilotCode) {
        this.pilotCode = pilotCode;
    }

    public String getPilotName() {
        return pilotName;
    }

    public void setPilotName(final String pilotName) {
        this.pilotName = pilotName;
    }

    public Long getRaceTotalTime() {
        return raceTotalTime;
    }

    public void setRaceTotalTime(final Long raceTotalTime) {
        this.raceTotalTime = raceTotalTime;
    }

    public Integer getLastCompleteLap() {
        return lastCompleteLap;
    }

    public void setLastCompleteLap(final Integer lastCompleteLap) {
        this.lastCompleteLap = lastCompleteLap;
    }

    public Float getLapSpeedSum() {
        return lapSpeedSum;
    }

    public void setLapSpeedSum(final Float lapSpeedSum) {
        this.lapSpeedSum = lapSpeedSum;
    }

    public Duration getTimeAfterFirstRacer() {
        return timeAfterFirstRacer;
    }

    public void setTimeAfterFirstRacer(final Duration timeAfterFirstRacer) {
        this.timeAfterFirstRacer = timeAfterFirstRacer;
    }

    public String getPilot() {
        return this.pilotCode + " - " + this.pilotName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RacerResult result = (RacerResult) o;
        return Objects.equals(pilotCode, result.pilotCode) &&
                Objects.equals(pilotName, result.pilotName) &&
                Objects.equals(raceTotalTime, result.raceTotalTime) &&
                Objects.equals(lastCompleteLap, result.lastCompleteLap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pilotCode, pilotName, raceTotalTime, lastCompleteLap);
    }

    @Override
    public String toString() {
        return "RacerResult{" +
                "pilotCode=" + pilotCode +
                ", pilotName='" + pilotName + '\'' +
                ", raceTotalTime=" + raceTotalTime +
                ", lastCompleteLap=" + lastCompleteLap +
                '}';
    }
}
