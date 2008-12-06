package server;

import common.Constants;
import common.Player;
import common.messages.Message;
import common.messages.MessageAnalyzer;
import common.messages.PlayerJoinMessage;
import common.messages.PlayerMotionMessage;
import common.messages.ProjectileMessage;
import common.messages.LoginMessage;

import Extasys.Network.UDP.Server.Listener.MulticastListener;
import Extasys.Network.UDP.Server.Listener.UDPListener;
import Extasys.Network.UDP.Server.ExtasysUDPServer;
import Extasys.Network.UDP.Server.IUDPServer;
import java.util.TreeMap;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
 
/**
 * Establishes the connection used to inform clients of what to do.
 */
public class ServerConnection extends ExtasysUDPServer implements IUDPServer, Constants {

    protected ServerGameEngine engine;
    protected InetSocketAddress gameAddress;
    public static Logger logger = Logger.getLogger(SERVER_LOGGER_NAME);
    
    /**
     * Start the connection to the server.
     */ 
    public ServerConnection(InetAddress listenerIP, int port, ServerGameEngine engine) throws Exception
    {
        super("SphereorityClient", "The server connection for Sphereority", 8, 32);
        this.engine = engine;
        this.gameAddress = new InetSocketAddress(InetAddress.getByName(PLAYER_MCAST_ADDRESS),MCAST_PORT);
        this.AddListener("SphereorityServer", listenerIP, port, 10240, 10000, true);
    }

    @Override
    public void OnDataReceive(UDPListener listener, DatagramPacket packet)
    {
        try
        {
            Message message = MessageAnalyzer.getMessage(packet.getData());
            
            // Ignore the message if it is sent by the server
            if(message.isAck())
                return;
        
            switch(message.getMessageType()) {
                case PlayerJoin:
                    PlayerJoinMessage pj = (PlayerJoinMessage) message;
                    pj.setAck(true);
                        
                    // Processing a new player?
                    if(pj.getPlayerId() == -1) {
                        pj.setPlayerId(engine.processPlayerJoin(pj));
                        
                        // Send a message via the Server
                        SendMessage(listener,
                                    pj,
                                    listener.getIPAddress(),
                                    listener.getPort());
                    }
                    // Asking for information about an existing user
                    else {
                        pj.setName(engine.getPlayerName(pj.getPlayerId()));
                        
                        SendMessage(listener,
                                    pj,
                                    listener.getIPAddress(),
                                    listener.getPort());
                    }
                    
                    break;
                    
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
      
    /**
     * Sends a Sphereority message via a UDPListener
     * @param listener
     * @param message
     * @param address
     * @param port
     */
    protected void SendMessage(UDPListener listener, Message message, 
                                InetAddress address, int port) throws Exception{
        byte[] msg = message.getByteMessage();
        DatagramPacket p = new DatagramPacket(msg,0,msg.length,address,port);
        listener.SendData(p);
    }
    
    /**
     * Add a new listener to this server.
     * @param name is the listener's name.
     * @param ipAddress is the listener's IP address.
     * @param port is the listener's udp port.
     * @param readBufferSize is the maximum size of bytes the listener can use to read incoming bytes at a time.
     * @param readDataTimeOut is the maximum time in milliseconds a client can use to send data to the listener.
     * @return the listener.
     */
    public UDPListener AddListener(String name, InetAddress ipAddress, int port, int readBufferSize, int readDataTimeOut, boolean isMulticast)
    {
        UDPListener listener;
        listener = isMulticast ? new MulticastListener(this, name, ipAddress, port, readBufferSize, readDataTimeOut) :
                                 new UDPListener(this, name, ipAddress, port, readBufferSize, readDataTimeOut);
        fListeners.add(listener);
        return listener;
    }
}
