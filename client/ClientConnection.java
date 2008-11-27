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
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
 
/**
 * Client connection to the server and other clients.
 */
public class ClientConnection extends ExtasysUDPClient implements Constants {
    private AutoSendMessages fAutoSendMessagesThread;
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
        super.AddConnector("SphereorityConnector", 10240, 8000, remoteHostIP, remoteHostPort);
    }
 
    @Override
    /**
     * How to handle received messages.
     * @param connector The connector the message was received from.
     * @param packet The packet the message was sent from.
     */
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
                    System.out.println("PlayerMotion: " + pm.getPlayerId() + " "
                                                        + pm.getPosition() + " "
                                                        + pm.getVelocity() + " "
                                                        + pm.getTime());
                    break;
                case PlayerJoin:
                    PlayerJoinMessage pjoinm = (PlayerJoinMessage) message;
                    System.out.println("PlayerJoin: " + pjoinm.getName() + 
                                       " wants to join");
                    engine.processPlayerJoin(pjoinm);
                    break;
                case Projectile:
                    ProjectileMessage projmsg = (ProjectileMessage) message;
                    System.out.println("Projectile: " + projmsg.getPlayerId());
                    engine.processProjectile(projmsg);
                    break;
 
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
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
        fAutoSendMessagesThread = new AutoSendMessages(this,engine);
        fAutoSendMessagesThread.start();
    }
 
    /**
     * Stop sending the messages.
     */
    public void StopSendingMessages() {
        if (fAutoSendMessagesThread != null)
        {
            fAutoSendMessagesThread.Dispose();
            fAutoSendMessagesThread.interrupt();
        }
    }
}
 
/**
 * Thread used for sending messages
 */
class AutoSendMessages extends Thread
{
    private ClientConnection fMyClient;
    private GameEngine engine;
    private boolean fActive = true;
 
    public AutoSendMessages(ClientConnection client, GameEngine engine)
    {
        fMyClient = client;
        this.engine = engine;
    }
 
    @Override
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
                
                for(common.Projectile p : engine.bulletList) {
                    if(!p.isDelivered() && !(p.getOwner() != playerId)) {
                        fMyClient.SendMessage(new ProjectileMessage(playerId,
                                                                    p.getStartPosition(),
                                                                    p.getDirection()));
                    }
                }
                // Sanity Check -> Make sure we are sending out the correct data
                //message = (PlayerMotionMessage) MessageAnalyzer.getMessage(msgToSend);
                //System.out.println(message.getPlayerId() + " " + message.getPosition() + 
                //                   " " + message.getVelocity() + " " + message.getTime());
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
