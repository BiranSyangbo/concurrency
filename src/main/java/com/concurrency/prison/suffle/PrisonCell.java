package com.concurrency.prison.suffle;

import java.util.concurrent.atomic.AtomicInteger;

public class PrisonCell {

    private boolean light;
    private final AtomicInteger counter;
    private final int totalNumberOfPrison;

    public PrisonCell(boolean light, int totalNumberOfPrison) {
        this.light = light;
        this.totalNumberOfPrison = totalNumberOfPrison;
        this.counter = new AtomicInteger(0);
    }

    public synchronized void toggleLightOnly() {
        this.light = !this.light;
        notifyAll();
    }

    public synchronized void toggleLight() {
        this.light = !this.light;
        int i = this.counter.incrementAndGet();
        System.out.println("I " + i);
        notifyAll();
    }

    public synchronized boolean isLightOn() {
        return this.light;
    }

    public synchronized int getCounter() {
        return this.counter.get();
    }

    public synchronized boolean allPrisonCount() {
        return getCounter() == totalNumberOfPrison - 1;
    }

}
