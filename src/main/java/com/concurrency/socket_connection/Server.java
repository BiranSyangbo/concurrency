package com.concurrency.socket_connection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    volatile boolean keepProcessing = true;

    private final ConnectionManager connectionManager;
    private final ClientScheduler clientScheduler;

    public Server(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.clientScheduler = new ThreadPerRequestScheduler();
    }

    @Override
    public void run() {
        System.out.println("Server Starting");
        while (keepProcessing) {
            try {
                ClientConnection clientConnection = connectionManager.awaitClient();
                ClientRequestProcessor requestProcessor
                        = new ClientRequestProcessor(clientConnection);
                clientScheduler.schedule(requestProcessor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopProcessing() {
        this.connectionManager.shutDown();
        this.keepProcessing = false;
    }

}

interface ClientScheduler {
    void schedule(ClientRequestProcessor clientRequestProcess);
}

class ThreadPerRequestScheduler implements ClientScheduler {

    @Override
    public void schedule(ClientRequestProcessor clientRequestProcess) {
        Runnable runnable = clientRequestProcess::process;
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        executorService.submit(runnable);
    }
}