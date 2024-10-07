package com.concurrency.socket_connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Objects;

public class ConnectionManager {

    private final ServerSocket serverSocket;

    public ConnectionManager(int port, int milliSecondsTimeout) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.serverSocket.setSoTimeout(milliSecondsTimeout);
    }

    public ClientConnection awaitClient() throws IOException {
        return new ClientConnection(serverSocket);
    }

    public void shutDown() {
        if (Objects.nonNull(this.serverSocket)) {
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
