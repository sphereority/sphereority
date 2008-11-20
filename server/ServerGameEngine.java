package server;

import common.Constants;
import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.*;

class ServerGameEngine extends Thread {
    // port to listen on for new connection threads
    private int         listenport;

    // serverchannel to listen with
    private ServerSocketChannel         listenchannel;
    // Vector to store sockets to connections
    private Vector                      socketchannels;

    ServerGameEngine (){
    }
    public void run(){
        System.out.println("ServerGameEngine Running");
    }
    public synchronized void newClient(String username, ObjectOutputStream tcpstream, DatagramChannel udpchan){
        System.out.println("ServerGameEngine: new client: " + username);
    }
}
