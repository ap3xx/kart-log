package com.ap3x.kartlog.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

public class LapLog {

    private LocalTime logTime;
    private Integer pilotCode;
    private String pilotName;
    private Integer lapNumber;
    private Duration lapTime;
    private Float lapSpeed;

    public LapLog() {
    }

    public LapLog(String pilotName) {
        this.pilotName = pilotName;
    }

    public LapLog(LocalTime logTime, Integer pilotCode, String pilotName, Integer lapNumber, Duration lapTime, Float lapSpeed) {
        this.logTime = logTime;
        this.pilotCode = pilotCode;
        this.pilotName = pilotName;
        this.lapNumber = lapNumber;
        this.lapTime = lapTime;
        this.lapSpeed = lapSpeed;
    }

    public LocalTime getLogTime() {
        return logTime;
    }

    public void setLogTime(LocalTime logTime) {
        this.logTime = logTime;
    }

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

    public Integer getLapNumber() {
        return lapNumber;
    }

    public void setLapNumber(Integer lapNumber) {
        this.lapNumber = lapNumber;
    }

    public Duration getLapTime() {
        return lapTime;
    }

    public void setLapTime(Duration lapTime) {
        this.lapTime = lapTime;
    }

    public Float getLapSpeed() {
        return lapSpeed;
    }

    public void setLapSpeed(Float lapSpeed) {
        this.lapSpeed = lapSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LapLog lapLog = (LapLog) o;
        return Objects.equals(logTime, lapLog.logTime) &&
                Objects.equals(pilotCode, lapLog.pilotCode) &&
                Objects.equals(pilotName, lapLog.pilotName) &&
                Objects.equals(lapNumber, lapLog.lapNumber) &&
                Objects.equals(lapTime, lapLog.lapTime) &&
                Objects.equals(lapSpeed, lapLog.lapSpeed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logTime, pilotCode, pilotName, lapNumber, lapTime, lapSpeed);
    }
}
