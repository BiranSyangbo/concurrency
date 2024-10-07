package com.concurrency.consumer_producer_problem;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConsumerProducerProblemWithReentrantLock {
    public static void main(String[] args) throws InterruptedException {
        var consumer = new ConsumerProducerReentrant(1);
        Thread producerTh = new Thread(new CustomProducerRunnable(consumer));
        Thread consumerTh = new Thread(new CustomConsumerRunnable(consumer));

        producerTh.start();
        consumerTh.start();

        producerTh.join();
        consumerTh.join();
    }
}

record CustomConsumerRunnable(ConsumerProducerReentrant consumerReentrant) implements Runnable {

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                String consumer = consumerReentrant.consumer();
                Thread.sleep(2000);
                System.out.printf("(%s)Consumed Message:: %s\n", Thread.currentThread().getName(), consumer);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}

record CustomProducerRunnable(ConsumerProducerReentrant producerReentrant) implements Runnable {

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                producerReentrant.producer("Hello " + i);
                System.out.printf("(%s)Produce Message:: %d \n", Thread.currentThread().getName(), i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}

class ConsumerProducerReentrant {
    private final ReentrantLock lock;
    private final Condition nonEmpty;
    private final Queue<String> message;
    private final int size;
    private final Condition notFull;

    ConsumerProducerReentrant(int size) {
        this.lock = new ReentrantLock();
        this.nonEmpty = this.lock.newCondition();
        this.notFull = this.lock.newCondition();
        this.message = new ArrayDeque<>(size);
        this.size = size;
    }

    public void producer(String value) throws InterruptedException {
        try {
            this.lock.lockInterruptibly();
            while (this.message.size() == size) {
                notFull.await();
            }
            message.add(value);
            this.nonEmpty.signal();
        } finally {
            this.lock.unlock();
        }
    }

    public String consumer() throws InterruptedException {
        this.lock.lockInterruptibly();
        try {
            while (message.isEmpty()) {
                nonEmpty.await();
            }
            String poll = message.poll();
            this.notFull.signal();
            return poll;
        } finally {
            this.lock.unlock();
        }
    }
}