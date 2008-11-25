package server;

import common.Constants;
import common.messages.*;
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
    public synchronized void newClient(String username, SocketChannel tcpchannel, DatagramChannel udpchan){
        System.out.println("ServerGameEngine: new client: " + username);
    }
    public synchronized void newTCPMessage(Message message){
    	//byte mtype = message.getMessageType();
        System.out.println("ServerGameEngine: TCP message received");
    }
    public synchronized void newUDPMessage(Message message){
        System.out.println("ServerGameEngine: UDP message received");
    }

}
