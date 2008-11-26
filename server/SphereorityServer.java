package server;

import common.Constants;
import java.nio.channels.SocketChannel;

class SphereorityServer {
	// SINGLETONS
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

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
