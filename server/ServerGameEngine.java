package server;

import common.Constants;
import common.messages.*;

import java.net.InetSocketAddress;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class ServerGameEngine implements Constants {
	public static Logger logger = Logger.getLogger(SERVER_LOGGER_NAME);

    private Queue<Byte> avaliableUserIDs;
    private Hashtable<Byte,String> userNames;
    private Hashtable<Byte,InetSocketAddress> addresses;
    
    private byte nextPlayer;

    private final byte INIT = 0;
    private final byte MAX_PLAYERS = 32;
    
    public ServerGameEngine (){
        avaliableUserIDs = new LinkedList<Byte>();
        // Populate the set with the avaliable userIds
        for(byte i = 0; i < MAX_PLAYERS; i++)
            avaliableUserIDs.offer(i);
        
    	userNames = new Hashtable<Byte,String>();
        addresses = new Hashtable<Byte,InetSocketAddress>();
        nextPlayer = 0;
    }
    
    /**
     * Processes a login message.
     * @param message The message for login.
     * @return The id that is assigned to the player.
     */
     /*
    public byte processLoginMessage(LoginMessage message) {
        byte playerId = message.getPlayerId();
        
        synchronized(this) {
            // Don't process if we are out of avaliable user IDs
            if(avaliableUserIDs.size() == 0) {
                playerId = -1;
            }
            else {
                playerId = avaliableUserIDs.poll();
                userNames.put(playerId,message.getPlayerName());
                addresses.put(playerId,message.getAddress());
                
                logger.log(logger.getLevel(), "New Player Added: " 
                                                + message.getPlayerName());
                System.out.println("New Player Added: " 
                                                + message.getPlayerName());
            }
        }
        return playerId;
    }*/
    
    /**
     * Processes a PlayerJoin message.
     * @param message The message for login.
     * @return The id that is assigned to the player.
     */
    public synchronized byte processPlayerJoin(PlayerJoinMessage message) {
        byte playerId = message.getPlayerId();
        
        // Don't process if we are out of avaliable user IDs
        if(avaliableUserIDs.size() == 0) {
            playerId = -1;
        }
        else {
            playerId = avaliableUserIDs.poll();
            userNames.put(playerId,message.getName());
            addresses.put(playerId,message.getAddress());
            
            //logger.log(logger.getLevel(), "New Player Added: " + message.getName());
            //System.out.println("New Player Added: " + message.getName());
            logger.log(Level.INFO, "New Player Added: " + message.getName());
        }
        return playerId;
    }
    
    public Set<Byte> getPlayers() {
        return userNames.keySet();
    }
    
    public String getPlayerName(byte playerId) {
        return userNames.get(playerId);
    }
    
    public InetSocketAddress getAddress(byte playerId) {
        return addresses.get(playerId);
    }
}
