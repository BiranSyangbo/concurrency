package com.concurrency.dining_philoshopers_problem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosopherProblem {

    private static final int NUMBER_OF_RESOURCES = 5;
    private static final List<Thread> threads = new ArrayList<>(NUMBER_OF_RESOURCES);
    private static final List<ReentrantLock> CHOPSTICKS_LOCK = new ArrayList<>(NUMBER_OF_RESOURCES);

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            ReentrantLock chopstick = new ReentrantLock();
            CHOPSTICKS_LOCK.add(chopstick);
        }

        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            Runnable runnable = new DinningTableRunnable(i, CHOPSTICKS_LOCK);
            Thread thread = new Thread(runnable);
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

}

record DinningTableRunnable(int philosopher, List<ReentrantLock> chopsticks) implements Runnable {

    @Override
    public void run() {
        try {
            while (true) {
                thinking();
                eating();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void eating() {
        ReentrantLock ownLock = chopsticks.get(philosopher);
        ReentrantLock rightLock = chopsticks.get((philosopher + 1) % chopsticks.size());
        ReentrantLock first = (philosopher == 0) ? rightLock : ownLock;
        ReentrantLock second = (philosopher == 0) ? ownLock : rightLock;
        first.lock();
        try {
            second.lock();
            System.out.printf("Philosophers: %d is eating \n", philosopher);
            Thread.sleep((int) (Math.random() * 1000));
            second.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            first.unlock();
        }
        System.out.printf("Philosophers: %d is finished eating \n", philosopher);

    }

    private void thinking() throws InterruptedException {
        System.out.printf("Philosophers: %d is thinking \n", philosopher);
        Thread.sleep((int) (Math.random() * 1000));
    }

    private void processing() throws InterruptedException {

        ReentrantLock reentrantLock = chopsticks.get(philosopher);
        reentrantLock.lock();
        ReentrantLock reentrantLock1 = chopsticks.get(philosopher + 1 % 5);
        reentrantLock1.lock();
        wait(300);
        System.out.printf("Philosophers: %d is eating \n", philosopher);
        reentrantLock.unlock();
        reentrantLock1.unlock();
        System.out.printf("Philosophers: %d is finished eating \n", philosopher);
    }
}
