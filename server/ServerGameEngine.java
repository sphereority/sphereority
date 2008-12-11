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
    private Vector<PlayerInfo> playerInfo;
    
    private final byte INIT = 1;
    private final byte MAX_PLAYERS = 64;
    
    public ServerGameEngine (long gamestarttime){
        avaliableUserIDs = new LinkedList<Byte>();
        // Populate the set with the avaliable userIds
        for(byte i = INIT; i <= MAX_PLAYERS; i++)
            avaliableUserIDs.offer(i);
        
    	playerInfo = new Vector<PlayerInfo>();
    }
    
    /**
     * Processes a PlayerJoin message.
     * @param message The message for login.
     * @return The id that is assigned to the player.
     */
    public synchronized byte processPlayerJoin(PlayerJoinMessage message) {
        byte playerId = message.getPlayerId();
        
        // Don't process if we are out of avaliable user IDs
        if(avaliableUserIDs.size() == 0 || nameInUse(message.getName())) {
            playerId = -2;
        }
        else {
            playerId = avaliableUserIDs.poll();
            playerInfo.add(new PlayerInfo(playerId,
                                          message.getAddress(),
                                          message.getName()));
            logger.log(Level.INFO, message.getName() + "has joined the game with ID " + playerId);
            logger.log(Level.INFO,"Avaliable Player IDs: " + avaliableUserIDs.size());
        }
        return playerId;
    }
    
    /**
     * Processes a PlayerJoin message.
     * @param message The message for logout.
     */
    public synchronized void processPlayerLeave(PlayerLeaveMessage message) {
        byte playerId = message.getPlayerId();
        for(int i = 0; i < playerInfo.size() ; i++) {
            if(playerInfo.get(i).getPlayerId() == playerId) {
                PlayerInfo info = playerInfo.remove(i);
                avaliableUserIDs.offer(playerId);
                logger.log(Level.INFO,info.getName() + " has left the game");
                logger.log(Level.INFO,"Avaliable Player IDs: " + avaliableUserIDs.size());
                break;
            }
        }
    }
    
    public String getPlayerName(byte playerId) {
        for(PlayerInfo player : playerInfo) {
            if(player.getPlayerId() == playerId)
                return player.getName();
        }
        return null;
    }
    
    public InetSocketAddress getAddress(byte playerId) {
        for(PlayerInfo player : playerInfo) {
            if(player.getPlayerId() == playerId)
                return player.getAddress();
        }
        return null;
    }
    
    public boolean nameInUse(String name) {
        for(PlayerInfo player : playerInfo) {
            if(player.getName().equals(name))
                return true;
        }
        return false;
    }
}

class PlayerInfo implements Comparable {
    private byte playerId;
    private InetSocketAddress address;
    private String name;
    
    public PlayerInfo(byte playerId, InetSocketAddress address, String name) {
        this.playerId = playerId;
        this.address = address;
        this.name = name;
    }
    
    public int compareTo(Object o) {
        if(o instanceof PlayerInfo)
            return new Byte(playerId).compareTo(((PlayerInfo )o).getPlayerId());
        if(o instanceof Byte)
            return new Byte(playerId).compareTo((Byte)o);
        return 0;
    }
    
    public byte getPlayerId() {
        return playerId;
    }
    
    public InetSocketAddress getAddress() {
        return address;
    }
    
    public String getName() {
        return name;
    }
}
