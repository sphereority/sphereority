package client;
 
import common.Constants;
import common.messages.Message;
import common.messages.MessageAnalyzer;
import common.messages.PlayerJoinMessage;
import common.messages.PlayerMotionMessage;
import common.messages.ProjectileMessage;
import common.Player;
import Extasys.Network.UDP.Client.Connectors.UDPConnector;
import Extasys.Network.UDP.Client.ExtasysUDPClient;
import Extasys.Network.UDP.Client.IUDPClient;
import Extasys.Network.UDP.Client.Connectors.UDPConnector;
import Extasys.Network.UDP.Client.Connectors.MulticastConnector;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
 
/**
 * Client connection to the server and other clients.
 */
public class ClientConnection extends ExtasysUDPClient implements Constants {
    private SendUpdateMessages fSendUpdateMessagesThread;
    private GameEngine engine;
    public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);
 
    /**
     * Creates a client connection
     */
    public ClientConnection(InetAddress remoteHostIP, int remoteHostPort, GameEngine engine) {
        super("SphereorityClient", "The client connection for sphereority", 8,16);
        this.engine = engine;
 
        // Add a UDP connector to this UDP client.
        // You can add more than one connectors if you need to.
        AddConnector("SphereorityConnector", 10240, 8000, remoteHostIP, remoteHostPort,true);
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
            if(message.getPlayerId() == engine.localPlayer.getPlayerID())
                return;
 
            switch(message.getMessageType()) {
                case PlayerMotion:
                    PlayerMotionMessage pm = (PlayerMotionMessage)message;
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
                    engine.processPlayerJoin(pj);
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
    
    public void SendMessage(Message message) throws Exception {
        byte[] msgToSend = message.getByteMessage();
        super.SendData(msgToSend, 0, msgToSend.length);
    }
    
    /**
     * Start sending the messages.
     */
    public void StartSendingMessages() {
        StopSendingMessages();
        fSendUpdateMessagesThread = new SendUpdateMessages(this,engine);
        fSendUpdateMessagesThread.start();
    }
 
    /**
     * Stop sending the messages.
     */
    public void StopSendingMessages() {
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
class SendUpdateMessages extends Thread
{
    private ClientConnection fMyClient;
    private GameEngine engine;
    private boolean fActive = true;

    public SendUpdateMessages(ClientConnection client, GameEngine engine)
    {
        fMyClient = client;
        this.engine = engine;
    }

    //@Override
    public void run()
    {
        int messageCount = 0;
        while (fActive)
        {
            try
            {
                Player player = engine.localPlayer;
                byte playerId = (byte)player.getPlayerID();
                
                // Send where the player is now
                fMyClient.SendMessage(new PlayerMotionMessage((byte)player.getPlayerID(),
                                                              player.getPosition(),
                                                              player.getVelocity(),
                                                              (float)System.currentTimeMillis()));
                
                // Go through all the projectiles in the game
                for(common.Projectile p : engine.bulletList) {
                    // Check if this is our own projectile and whether
                    // we have sent information about it
                    if(!p.isDelivered() && !(p.getOwner() != playerId)) {
                        // Deliver the information about the projectile
                        fMyClient.SendMessage(new ProjectileMessage(playerId,
                                                                    p.getStartPosition(),
                                                                    p.getDirection()));
                        p.delivered();
                    }
                }
                Thread.sleep(500);
            }
            catch (Exception ex)
            {
                Dispose();
                fMyClient.StopSendingMessages();
            }
        }
    }
    
    public void Dispose()
    {
        fActive = false;
    }
}
