package server;

import common.Constants;
import common.messages.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

class ServerGameEngine extends Thread {
    // port to listen on for new connection threads
    private int         listenport;

    // serverchannel to listen with
    private ServerSocketChannel     listenchannel;
    // Vector to store sockets to connections
    private Hashtable               tcpchannels;
    private DatagramChannel			udpchannel = null;
    private Hashtable				usernames;
    private byte					nextplayer=1;

    ServerGameEngine (){
    	usernames = new Hashtable();
    	tcpchannels = new Hashtable();
    }
    public void run(){
        System.out.println("ServerGameEngine Running");
    }  
    public synchronized byte newClient(String username){
    	usernames.put(new Integer(nextplayer),username);
        System.out.println("ServerGameEngine: new client: " + username);
        return nextplayer++;
    }
    public synchronized void clientChannels(byte playerid, SocketChannel tcpchannel, DatagramChannel udpchan){
    	tcpchannels.put(new Integer(playerid), tcpchannel);
    	if (udpchannel == null)
    		udpchannel = udpchan;
    	System.out.printf("ServerGameEngine.java: player %d channels registered\n", playerid);
    }
    public synchronized void newTCPMessage(Message message){
    	//byte mtype = message.getMessageType();
    	try{
    		byte player = message.getPlayerId();
    		byte [] bytes = message.getByteMessage();
    		ByteBuffer buf = ByteBuffer.allocate(bytes.length);
    		buf.put(bytes);
    		SocketChannel channel;
    		for (int i=1; i<nextplayer; i++){
    			if (i != (int)player){
    				channel = (SocketChannel) tcpchannels.get(new Integer(i));
    				channel.write(buf);
    			}
    		}
    	}
    	catch (Exception e){
    		e.printStackTrace();
    	}
        System.out.println("ServerGameEngine: TCP message received");
    }
    public synchronized void newUDPMessage(Message message){
        System.out.println("ServerGameEngine: UDP message received");
    }

}
