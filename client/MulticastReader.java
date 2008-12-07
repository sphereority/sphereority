package client;

//import common.messages.*;
//import java.awt.event.*;
//import java.io.*;
//import java.nio.charset.Charset;
//import java.util.*;
//import javax.swing.*;
import common.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client connection to the server and other clients.
 */
public class MulticastReader extends Thread implements Constants { 
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

    private MulticastSocket socket;
    private Pipe.SinkChannel pipe;
    private InetAddress myself;

    public MulticastReader(MulticastSocket socket, Pipe.SinkChannel pipe) {
        this.socket = socket;
        this.pipe   = pipe;
        try
        {
        	myself = InetAddress.getByName("localhost");
        	System.out.printf("My name is %s\n", myself.toString());
        }
        catch (UnknownHostException er) { }
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
                //if (packet.getAddress().equals(myself)) continue;
                
                //System.out.printf("Recieved packet of length %d from %s\n", packet.getLength(), packet.getAddress().toString());
                pipe.write(ByteBuffer.wrap(packet.getData()));
            }
            catch (Exception ex) {
            }
            Thread.yield();
        }
    }
}