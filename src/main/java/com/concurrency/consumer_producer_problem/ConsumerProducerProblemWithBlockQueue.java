package com.concurrency.consumer_producer_problem;

import java.util.concurrent.ArrayBlockingQueue;

public class ConsumerProducerProblemWithBlockQueue {

    public static void main(String[] args) throws InterruptedException {
        ConsumerProducer consumerProducer = new ConsumerProducer(2);
        Thread producer = new Thread(new ProducerRunnable(consumerProducer));
        Thread consumer = new Thread(new ConsumerRunnable(consumerProducer));
        Thread consumera = new Thread(new ConsumerRunnable(consumerProducer));

        producer.start();
        consumer.start();
        consumera.start();

        producer.join();
        consumer.join();
        consumera.join();

    }
}


class ConsumerProducer {
    private final ArrayBlockingQueue<String> sharedResource;

    ConsumerProducer(int size) {
        this.sharedResource = new ArrayBlockingQueue<>(size);
    }

    public void produce(String message) throws InterruptedException {
        this.sharedResource.put(message);
        System.out.printf("Produce Item:: %s \n", message);
    }

    public String consume() throws InterruptedException {
        return sharedResource.take();
    }
}

record ProducerRunnable(ConsumerProducer consumerProducer) implements Runnable {


    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                consumerProducer.produce("Sent Message: " + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}

record ConsumerRunnable(ConsumerProducer consumerProducer) implements Runnable {

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                String consume = consumerProducer.consume();
                System.out.printf("(%s)Consumed Message:: %s \n",Thread.currentThread().getName(), consume);
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

