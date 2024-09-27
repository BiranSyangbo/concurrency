package com.concurrency.dining_philoshopers_problem;

import java.util.ArrayList;
import java.util.List;

public class DiningPhilosopherProblem {

    private static final int NUMBER_OF_CHOPSTICKS = 5;
    private static final int NUMBER_OF_PHILOSOPHERS = 5;
    private static List<Thread> threads = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < NUMBER_OF_PHILOSOPHERS; i++) {
            Runnable runnable = new DinningTable(i + 1);
            Thread thread = new Thread(runnable);
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

}

class DinningTable implements Runnable {

    int philosopher;

    public DinningTable(int philosopher) {
        this.philosopher = philosopher;
    }

    @Override
    public void run() {
        processing();
    }

    private void processing() {
        System.out.printf("Philosopher %d is thinking \n", philosopher);
    }
}
