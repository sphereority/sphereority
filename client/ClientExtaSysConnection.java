package client;

import common.Player;
import common.Constants;
import common.SpawnPoint;
import common.messages.Message;
import common.messages.MessageAnalyzer;
import common.messages.PlayerJoinMessage;
import common.messages.PlayerMotionMessage;
import common.messages.ProjectileMessage;
import common.messages.LoginMessage;

import Extasys.Network.UDP.Client.Connectors.UDPConnector;
import Extasys.Network.UDP.Client.ExtasysUDPClient;
import Extasys.Network.UDP.Client.IUDPClient;
import Extasys.Network.UDP.Client.Connectors.UDPConnector;
import Extasys.Network.UDP.Client.Connectors.MulticastConnector;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client connection to the server and other clients.
 */
public class ClientExtaSysConnection extends ExtasysUDPClient implements IUDPClient, Constants, ClientConnection
{
    private SendUpdateMessages fSendUpdateMessagesThread;
    private GameEngine engine;
    private boolean isConnected;
    public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);
 
    /**
     * Creates a client connection
     */
    public ClientExtaSysConnection(InetAddress remoteHostIP, int remoteHostPort, GameEngine engine) throws Exception{
        super("SphereorityClient", "The client connection for sphereority", 8,16);
        this.engine = engine;
        isConnected = false;
        // Add a UDP connector to this UDP client.
        // You can add more than one connectors if you need to.
        AddConnector("ServerConnector", 10240, 8000, remoteHostIP, remoteHostPort,true);
        
        // Try to connect to the server.
        establishServerConnection();
    }

    /**
     * Starts the ClientConnection
     */
    // @Override
    public void start()  {
        try {
            // Check if we are connected to the server!
            if(!isConnected)
                throw new Exception("We're not connected to the server!");
                
            System.out.println("Starting...");
            // Restart all the connectors
            super.Start();
            // Start sending the messages.
            startSendingMessages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    /**
     * Stops the ClientConnection
     */
    // @Override
    public void stop()  {    
        super.Stop();
        System.out.println("Stopping...");
        stopSendingMessages();
    }

    /**
     * Attempts to establish a connection to the server.
     */
    public void establishServerConnection() throws Exception {
        logger.log(logger.getLevel(),"Establishing Server Connection");
        System.out.println("Establishing Server Connection");
        
        // Start the connector to the server.
        super.Start();
        
        InetSocketAddress serverAddress
                    = new InetSocketAddress(SERVER_ADDRESS,SERVER_PORT);
        SpawnPoint sp = new SpawnPoint(engine.localPlayer.getPosition());
        
        // Request to the server that we log in.
        // Note: Do not need to specify address here.
        // Is here just to avoid a nullpointer when writing the message
        sendMessage(new PlayerJoinMessage((byte)-1,
                                          engine.localPlayer.getPlayerName(),
                                          serverAddress,
                                          sp,
                                          false),
                                          0);
    
        // Make sure that we only wait at most 10 seconds
        long waitTime = System.currentTimeMillis() + 10000;
        
        // Wait until we have logged in and prepared the game
        while(!isConnected) {
            Thread.yield();
            
            // Stop attempting to connect if 10 seconds has past
            if(System.currentTimeMillis() > waitTime) {
                throw new Exception("Unable to connect to server!");
            }
        }
        
        super.Stop();
        logger.log(logger.getLevel(),"Sent login message");
    }
    
    /**
     * How to handle received messages.
     * @param connector The connector the message was received from.
     * @param packet The packet the message was sent from.
     */
    //@Override
    public void OnDataReceive(UDPConnector connector, DatagramPacket packet) {
        try {
            // Retrieve the message
            Message message = MessageAnalyzer.getMessage(packet.getData());
            
            // Ignore messages that are sent to yourself
            if(message.getPlayerId() == engine.localPlayer.getPlayerID() ||
               message.getPlayerId() == -1)
                return;
            
            switch(message.getMessageType()) {
                case PlayerMotion:
                    PlayerMotionMessage pm = (PlayerMotionMessage)message;
                    // New player was added?
                    engine.processPlayerMotion(pm);
                    logger.log(Level.INFO,"PlayerMotion: " + pm.getPlayerId()
                                                           + " "
                                                           + pm.getPosition() 
                                                           + " "
                                                           + pm.getVelocity() 
                                                           + " "
                                                           + pm.getTime());
                    break;
                case PlayerJoin:
                    PlayerJoinMessage pj = (PlayerJoinMessage) message;
                    logger.log(Level.INFO,"PlayerJoin: " + pj.getPlayerId());
                    String myName = engine.localPlayer.getPlayerName();
                    // Waiting for a server message?
                    if(!isConnected) {
                        handleLogin(pj);
                    }
                    // Playing the game and message is not about me?
                    else if(pj.getPlayerId() != -2 && !pj.getName().equals(myName)) {
                        engine.processPlayerJoin(pj);
                    }
                    break;
                case Projectile:
                    ProjectileMessage p = (ProjectileMessage) message;
                    logger.log(Level.INFO,"Projectile: " + p.getPlayerId()
                                                         + " "
                                                         + p.getStartPosition()
                                                         + " "
                                                         + p.getDirection());
                    engine.processProjectile(p);
                    break;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Handles login given a PlayerJoin Message.
     */
    protected void handleLogin(PlayerJoinMessage message) throws Exception {
        String myName = engine.localPlayer.getPlayerName();
        // Message is from the server?
        if(!message.isAck())
            return;
        // Message is intended for me?
        if(!message.getName().equals(myName))
            return;
        
        // Check for success or failure
        if(message.getPlayerId() == -2) {
            throw new Exception("Unable to login!");
        }
        else {
            engine.localPlayer.setPlayerID(message.getPlayerId());
            AddConnector("GameConnector", 10240, 8000,
                          message.getAddress().getAddress(),
                          message.getAddress().getPort(),true);
            isConnected = true;
        }
    }
    
    /**
     * Add a new connector to this client that can be either unicast or multicast.
     * @param name is the name of the connector.
     * @param readBufferSize is maximum number of bytes the connector can read at a time.
     * @param readTimeOut is the maximum time in milliseconds the connector can use to read incoming data.
     * @param serverIP is the server's ip address the connector will use to send data.
     * @param serverPort is the server's udp port.
     * @return the connector.
     */
    public UDPConnector AddConnector(String name, int readBufferSize, int readTimeOut, InetAddress serverIP, int serverPort, boolean isMulticast)
    {
        UDPConnector connector;
        connector = isMulticast ? new MulticastConnector(this, name, readBufferSize, readTimeOut, serverIP, serverPort) :
                                  new UDPConnector(this, name, readBufferSize, readTimeOut, serverIP, serverPort);
        getConnectors().add(connector);
        return connector;
    }
    
    /**
     * Sends a Sphereority message via all the connectors.
     */
    public void sendMessage(Message message) throws Exception {
        byte[] msgToSend = message.getByteMessage();
        for(UDPConnector connector : getConnectors()) {
            connector.SendData(msgToSend, 0, msgToSend.length);
        }
    }
    /**
     * Sends a Sphereority message via all the connectors.
     */
    public void sendMessage(Message message, int connector) throws Exception {
        byte[] msgToSend = message.getByteMessage();
        getConnectors().get(connector).SendData(msgToSend, 0, msgToSend.length);
    }
    
    /**
     * Start sending the messages.
     */
    public void startSendingMessages() {
        stopSendingMessages();
        fSendUpdateMessagesThread = new SendUpdateMessages(this,engine);
        fSendUpdateMessagesThread.start();
    }
 
    /**
     * Stop sending the messages.
     */
    public void stopSendingMessages() {
        if (fSendUpdateMessagesThread != null)
        {
            fSendUpdateMessagesThread.Dispose();
            fSendUpdateMessagesThread.interrupt();
        }
    }
}
 
/**
 * Thread used for sending messages
 */
class SendUpdateMessages extends Thread implements Constants
{
    private ClientExtaSysConnection fMyClient;
    private GameEngine engine;
    private boolean fActive = true;

    public SendUpdateMessages(ClientExtaSysConnection client, GameEngine engine)
    {
        fMyClient = client;
        this.engine = engine;
    }

    //@Override
    public void run()
    {
        int messageCount = 0;
        int gameConnector = 1, serverConnector = 0;
        
        for (int checkNames = 0; fActive; checkNames++)
        {
            try
            {
                Player localPlayer = engine.localPlayer;
                byte playerId = (byte)localPlayer.getPlayerID();
                
                // Send where the player is now
                fMyClient.sendMessage(new PlayerMotionMessage((byte)localPlayer.getPlayerID(),
                                                              localPlayer.getPosition(),
                                                              localPlayer.getVelocity(),
                                                              (float)System.currentTimeMillis()),
                                                              gameConnector);
                
                // Go through all the projectiles in the game
                for(common.Projectile p : engine.bulletList) {
                    // Check if this is our own projectile and whether
                    // we have sent information about it
                    if(!p.isDelivered() && !(p.getOwner() != playerId)) {
                        // Deliver the information about the projectile
                        fMyClient.sendMessage(new ProjectileMessage(playerId,
                                                                    p.getStartPosition(),
                                                                    p.getDirection()),
                                                                    gameConnector);
                        p.delivered();
                    }
                }
                
                // Resolve names that have not been found
                if(checkNames == 4) {
                    for(Player player : engine.playerList) {
                        if (player.getPlayerName().equals(RESOLVING_NAME)) {
                            fMyClient.sendMessage(new PlayerJoinMessage((byte)player.getPlayerID(),
                                                    RESOLVING_NAME,
                                                    new InetSocketAddress(SERVER_ADDRESS,SERVER_PORT),
                                                    new SpawnPoint(player.getPosition())),
                                                    serverConnector);
                        }
                    }
                }
                
                Thread.sleep(500);
            }
            catch (Exception ex)
            {
                Dispose();
                fMyClient.stopSendingMessages();
            }
        }
    }
    
    public void Dispose()
    {
        fActive = false;
    }
}
