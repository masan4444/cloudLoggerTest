package com.example.masan.cloudloggertest;

import android.util.Log;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by masan on 2016/10/25.
 */

public class CloudLoggerSocket {
    private PrintStream printStream;
    private Socket socket;
    private InetAddress inetAddress;
    private int portNumber;

    private List<LinkedList<String>> dataList;

    public CloudLoggerSocket(InetAddress inetAddress, int portNumber) {
        dataList = new LinkedList<LinkedList<String>>();

        this.inetAddress = inetAddress;
        this.portNumber = portNumber;

        set();

        Log.d("CloudLoggerSocket","Create CloudLoggerSocket");
    }

    public void set() {
        try {
            socket = new Socket(inetAddress, portNumber);
            printStream = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bufferedWrite(LinkedList<String> data) {
        dataList.add(data);
        send();
    }

    public void send() {
    }

    public void writeAndSend(LinkedList<String> data) {
        if (socket == null) {
            set();
        }
        StringBuilder sb = new StringBuilder();
        for (String str : data) {
            sb.append(str + ",");
        }
        int index = sb.lastIndexOf(",");
        sb.deleteCharAt(index); //http://yamato-java.blogspot.jp/2011/09/public-class-first-public-static-void.html
        printStream.println(sb);
    }

    public void close() {
    }
}
