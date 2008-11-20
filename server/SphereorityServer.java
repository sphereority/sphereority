package server;

import common.Constants;
import java.nio.channels.SocketChannel;

class SphereorityServer {
    public static void main (String [] args){
        try {
            ServerGameEngine sge = new ServerGameEngine();
            NetworkListener nl = new NetworkListener(Constants.DEFAULT_PORT,sge);
    
            nl.start();
            sge.start();
        }
        catch (Exception e){
            System.out.print("SphereorityServer.java\n\n");
        }
    }
}
