package client;

import common.Constants;
import common.Player;
import common.messages.Message;
import common.messages.PlayerMotionMessage;
import common.messages.MessageAnalyzer;
import java.net.InetAddress;
import java.net.DatagramPacket;
import Extasys.Network.UDP.Client.ExtasysUDPClient;
import Extasys.Network.UDP.Client.IUDPClient;
import Extasys.Network.UDP.Client.Connectors.UDPConnector;

/**
 * Client connection to the server and other clients.
 */
public class ClientConnection extends ExtasysUDPClient implements Constants {
    private AutoSendMessages fAutoSendMessagesThread;
    private GameEngine engine;

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
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
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
                PlayerMotionMessage message = new PlayerMotionMessage((byte)player.getPlayerID(),
                                                                      player.getPosition(),
                                                                      player.getVelocity(),
                                                                      (float)System.currentTimeMillis());
                
                byte[] msgToSend = message.getByteMessage();
                fMyClient.SendData(msgToSend, 0, msgToSend.length);

                // Sanity Check -> Make sure we are sending out the correct data
                //message = (PlayerMotionMessage) MessageAnalyzer.getMessage(msgToSend);
                //System.out.println(message.getPlayerId() + " " + message.getPosition() + 
                //                   " " + message.getVelocity() + " " + message.getTime());
                Thread.sleep(1000);
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
