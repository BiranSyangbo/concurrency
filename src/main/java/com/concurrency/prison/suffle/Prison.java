package com.concurrency.prison.suffle;

import java.util.ArrayList;
import java.util.List;

public class Prison {
    public static void main(String[] args) {
        int nPrisoners = Runtime.getRuntime().availableProcessors();
        PrisonCell prisonCell = new PrisonCell(false, nPrisoners);
        List<Thread> prisonThreads = new ArrayList<>();
        for (int i = 0; i < nPrisoners; i++) {
            PrisonRunnable prisonRunnable = new PrisonRunnable(prisonCell, i == 0);
            Thread thread = new Thread(prisonRunnable);
            thread.start();
            prisonThreads.add(thread);
        }

        for (Thread prisonThread : prisonThreads) {
            try {
                prisonThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("All prisoners have been freed!");

    }
}

class PrisonRunnable implements Runnable {

    private final PrisonCell prisonCell;
    private boolean isFirstVisit = true;
    private final boolean isCounter;

    public PrisonRunnable(PrisonCell prisonCell, boolean isCounter) {
        this.prisonCell = prisonCell;
        this.isCounter = isCounter;
    }

    @Override
    public void run() {
        while (true) {
            try {
                long id = Thread.currentThread().getId();
                Thread.sleep((long) (10000 * Math.random()));
                if (this.isCounter) {
                    if (!prisonCell.isLightOn()) {
                        System.out.printf("Counter Prisoner %d turns the switch on \n", id);
                        prisonCell.toggleLightOnly();
                    }
                } else if (isFirstVisit && prisonCell.isLightOn()) {
                    System.out.printf("Prisoner %d turns the switch on \n", id);
                    prisonCell.toggleLight();
                    this.isFirstVisit = false;

                } else {
//                    System.out.printf("Prisoner %d finds the switch on and leaves it unchanged \n", id);
                }
                if (prisonCell.allPrisonCount()) {
                    System.out.printf("All prisoners have visited at least once. Prisoner %d declares freedom! \n", id);
                    break;
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}