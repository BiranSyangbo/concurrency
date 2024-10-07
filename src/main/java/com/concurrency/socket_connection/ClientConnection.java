package com.concurrency.socket_connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientConnection {

    private final Socket socket;


    public ClientConnection(ServerSocket serverSocket) throws IOException {
        this.socket = serverSocket.accept();
    }

    public Socket getSocket() {
        return this.socket;
    }
}
