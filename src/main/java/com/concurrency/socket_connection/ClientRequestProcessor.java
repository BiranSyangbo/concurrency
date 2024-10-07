package com.concurrency.socket_connection;

import java.io.IOException;
import java.net.Socket;

public class ClientRequestProcessor {

    private final Socket socket;

    public ClientRequestProcessor(ClientConnection clientConnection) {
        this.socket = clientConnection.getSocket();
    }

    public void process() {
        try {
            printInfo("Getting messages", "");
            String message = MessageUtils.getMessage(socket);
            printInfo("got message", message);
            Thread.sleep(1000);
            printInfo(" sending reply: ", message);
            MessageUtils.sendMessage(socket, "Processed: " + message);
            printInfo("sent", "");
            closeIgnoringException(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printInfo(String printMsg, String message) {
        System.out.printf("Server[%s::%s]:  %s %s\n", Thread.currentThread().getName(), Thread.currentThread().getId(), printMsg, message);
    }

    private void closeIgnoringException(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
