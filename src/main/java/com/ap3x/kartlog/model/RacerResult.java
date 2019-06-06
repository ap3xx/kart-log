package com.ap3x.kartlog.model;

import java.util.Objects;

public class RacerResult {

    private Integer pilotCode;
    private String pilotName;
    private Long raceTotalTime;
    private Integer lastCompleteLap;

    public Integer getPilotCode() {
        return pilotCode;
    }

    public void setPilotCode(Integer pilotCode) {
        this.pilotCode = pilotCode;
    }

    public String getPilotName() {
        return pilotName;
    }

    public void setPilotName(String pilotName) {
        this.pilotName = pilotName;
    }

    public Long getRaceTotalTime() {
        return raceTotalTime;
    }

    public void setRaceTotalTime(Long raceTotalTime) {
        this.raceTotalTime = raceTotalTime;
    }

    public Integer getLastCompleteLap() {
        return lastCompleteLap;
    }

    public void setLastCompleteLap(Integer lastCompleteLap) {
        this.lastCompleteLap = lastCompleteLap;
    }

    public String getPilot() {
        return this.pilotCode + " - " + this.pilotName;
    }

    @Override
    public boolean equals(Object o) {
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
