package com.concurrency;

import com.concurrency.socket_connection.ConnectionManager;
import com.concurrency.socket_connection.MessageUtils;
import com.concurrency.socket_connection.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;
import java.net.Socket;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientTest {
    private static final int PORT = 1234;
    private static final int TIMEOUT = 2000;

    Thread thread;
    Server server;

    @BeforeEach
    void test() throws IOException {
        server = new Server(new ConnectionManager(PORT, TIMEOUT));
        thread = new Thread(server);
        thread.start();
    }

    @AfterEach
    public void shutdown() throws InterruptedException {
        if (server != null) {
            server.stopProcessing();
            thread.join();
        }
    }

    @Test()
    @Timeout(value = 10)
    public void shouldRunInUnder10Seconds() throws InterruptedException {
        Thread[] threads = new Thread[2];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new TrivialClient(i + 1));
            threads[i].start();
        }

        for (Thread value : threads) {
            value.join();
        }
    }

    @Test
//    @Timeout(value = 10, unit = TimeUnit.MILLISECONDS)
    void testTimeout() throws InterruptedException {
        assertThat("Hello").isEqualTo("Hi");
    }


    private class TrivialClient implements Runnable {

        final int clientNumber;

        public TrivialClient(int clientNumber) {
            this.clientNumber = clientNumber;
        }

        @Override
        public void run() {
            try {
                connectSendReceive(clientNumber);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void connectSendReceive(int clientNumber) throws IOException {
        System.out.printf("Client %2d: connecting \n", clientNumber);
        Socket socket = new Socket("localhost", PORT);
        System.out.printf("Client %2d: Sending message \n", clientNumber);
        MessageUtils.sendMessage(socket, Integer.toString(clientNumber));
        System.out.printf("Client %2d: getting reply \n", clientNumber);
        MessageUtils.getMessage(socket);
        System.out.printf("Client %2d: Finished \n", clientNumber);
        socket.close();

    }

}
