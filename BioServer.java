package com.wangwenjun.socks.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by wangwenjun on 2016/4/3.
 */
public class BioServer
{

    public static void main(String[] args)
    {
        startServer();
    }

    static void startServer()
    {

        try {
            ServerSocket serverSocket = new ServerSocket(1999);

            while(true)
            {
                System.out.println("===========");
                Socket clientSocket = serverSocket.accept();
                BioServer.handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void handleClient(final Socket socket) throws IOException {
        System.out.println("Remote Client->" + socket.getRemoteSocketAddress().toString());
        InputStream inputStream = socket.getInputStream();
        byte[] tmp = new byte[1];
        int len = inputStream.read(tmp);
        if(len==1)
        {
            byte protocolVersion = tmp[0];
            System.out.println("The protocol Version->"+protocolVersion);
        }

        int nMethods = inputStream.read();
        System.out.println("The nMethods->"+nMethods);
        int methods = inputStream.read();
        System.out.println("The methods->"+methods);

        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(new byte[]{0x05,0x00});

        tmp = new byte[10];
        System.out.println("tmp[0]>"+tmp[0]);
        System.out.println("tmp[1]>"+tmp[1]);
        System.out.println("tmp[2]>"+tmp[2]);
        System.out.println("tmp[3]>"+tmp[3]);

        String remoteIP = tmp[4]+"."+tmp[5]+"."+tmp[6]+"."+tmp[7];
        System.out.println("remoteIP>"+remoteIP);
        int port = ByteBuffer.wrap(tmp,7,2).asShortBuffer().get()& 0xFFFF;

        System.out.println("port>"+remoteIP);


        ByteBuffer rsv = ByteBuffer.allocate(10);
        rsv.put((byte) 0x05);
        rsv.put((byte) 0x00);
        rsv.put((byte) 0x00);
        rsv.put((byte) 0x01);
        rsv.put(socket.getLocalAddress().getAddress());
        Short localPort = (short) ((socket.getLocalPort()) & 0xFFFF);
        rsv.putShort(localPort);
        tmp = rsv.array();

        outputStream.write(tmp);
        outputStream.flush();


        final Socket resultSocket = new Socket(remoteIP, port);

        new Thread(()->{
            byte[] bytes = new byte[1024];
            int n = 0;
            try {
                OutputStream outputStream1 = resultSocket.getOutputStream();
                while ((n = inputStream.read(bytes)) > 0) {
                    outputStream1.write(bytes, 0, n);
                    outputStream1.flush();
                }
            } catch (Exception e) {
            }
        }).start();


        new Thread(()->{
            byte[] bytes = new byte[1024];
            int n = 0;
            try {
                InputStream inputStream1 = resultSocket.getInputStream();
                while ((n = inputStream1.read(bytes)) > 0) {
                    outputStream.write(bytes, 0, n);
                    outputStream.flush();
                }
            } catch (Exception e) {
            }
        }).start();
    }
}