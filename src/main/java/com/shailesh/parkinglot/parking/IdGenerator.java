package com.shailesh.parkinglot.parking;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {

    private AtomicInteger currentId;
    private final String format;

    public IdGenerator(Integer currentId, String format) {
        this.currentId = new AtomicInteger(currentId);
        this.format = format;
    }

    public String nextId(){
        Integer id = currentId.getAndAdd(1);
        return String.format(format,id);
    }

}
