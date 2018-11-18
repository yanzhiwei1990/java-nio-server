package com.jenkov.nioserver.example;

import com.jenkov.nioserver.*;
import com.jenkov.nioserver.http.HttpMessageReaderFactory;
import com.jenkov.nioserver.tansmit.TransmitMessageReaderFactory;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by jjenkov on 19-10-2015.
 */
public class Main {

    public static void main(String[] args) throws IOException {
    	startHttpListener();
    	startTransmitListener();
    }

    private static void startHttpListener() throws IOException {
    	String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: 38\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body>Hello World!</body></html>";

        byte[] httpResponseBytes = httpResponse.getBytes("UTF-8");

        IMessageProcessor messageProcessor = (request, writeProxy) -> {
        	//RuntimeException e = new RuntimeException("process is here");
            //e.fillInStackTrace();
            //System.out.println("@@@@@@@@ " + e.getStackTrace() + "\n message = " + e.getMessage());
            //e.printStackTrace();
            //System.out.println("Message Received from socket: " + request.socketId);
        	System.out.println("Message Received from socket: " + request.socketId + ", detail:" + request.connectInfo);

            Message response = writeProxy.getMessage();
            response.socketId = request.socketId;
            response.writeToMessage(httpResponseBytes);

            writeProxy.enqueue(response);
        };

        Server server = new Server("HTTP", 19999, new HttpMessageReaderFactory(), messageProcessor);

        server.start();
    }
    
    private static void startTransmitListener() throws IOException {
    	String transmitResponse = "transmit server connect ok";

        byte[] transmitResponseBytes = transmitResponse.getBytes("UTF-8");

        IMessageProcessor messageProcessor = (request, writeProxy) -> {
            //System.out.println("Message Received from tansmit client socket: " + request.socketId);
        	System.out.println("Message Received from tansmit client socket: " + request.socketId + ", detail:" + request.connectInfo);
        	
            Message response = writeProxy.getMessage();
            response.socketId = request.socketId;
            response.writeToMessage(transmitResponseBytes);

            writeProxy.enqueue(response);
        };

        Server server = new Server("TRANSMIT", 19900, new TransmitMessageReaderFactory(), messageProcessor);

        server.start();
    }
}
