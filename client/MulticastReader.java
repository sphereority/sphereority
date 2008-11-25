package client;

import common.*;
import common.messages.*;

import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Client connection to the server and other clients.
 */
public class MulticastReader extends Thread implements Constants { 
    private MulticastSocket socket;
    private Pipe.SinkChannel pipe;

    private InetAddress myMCastGroup;
    private int myMCastPort;

    public MulticastReader(MulticastSocket socket, Pipe.SinkChannel pipe) {
        this.socket = socket;
        this.pipe   = pipe;
    }
    
    /**
     *
     */
    public void run() {
        byte[] buf = new byte[1000];
        DatagramPacket packet = new DatagramPacket(buf,buf.length);   
        while(true) {
            try {
                socket.receive(packet);
                System.out.printf("Recieved packet of length %d\n", packet.getLength());
                pipe.write(ByteBuffer.wrap(packet.getData()));
            }
            catch (Exception ex) {
            }
            Thread.yield();
        }
    }
}