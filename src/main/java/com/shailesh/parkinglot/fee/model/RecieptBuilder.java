package com.shailesh.parkinglot.fee.model;

import java.time.LocalDateTime;

public class RecieptBuilder {

    private String id;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Double fees;

    private RecieptBuilder(){}

    public RecieptBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public RecieptBuilder setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
        return this;
    }

    public RecieptBuilder setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
        return this;
    }

    public RecieptBuilder setFees(Double fees) {
        this.fees = fees;
        return this;
    }

    public static RecieptBuilder builder(){
        return new RecieptBuilder();
    }

    public Reciept build(){
        return new Reciept(id, entryTime, exitTime, fees);
    }
}
