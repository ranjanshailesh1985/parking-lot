package com.shailesh.parkinglot.fee.model;

import java.time.LocalDateTime;

public class Reciept {

    private final String id;
    private final LocalDateTime entryTime;
    private final LocalDateTime exitTime;
    private final Double fees;

    public Reciept(String id, LocalDateTime entryTime, LocalDateTime exitTime, Double fees) {
        this.id = id;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.fees = fees;
    }

    public LocalDateTime getEntryTime() {
        return this.entryTime;
    }

    public LocalDateTime getExitTime() {
        return this.exitTime;
    }

    public Double getFees() {
        return this.fees;
    }
}
