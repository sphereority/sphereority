package client;

import common.messages.*;

public interface ClientConnection
{
	public void start();
    public void stop();
    public void stopSendingMessages();
    
    public void sendMessage(Message msg) throws Exception;
}
