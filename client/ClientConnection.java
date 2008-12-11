package client;

import common.messages.*;

public interface ClientConnection
{
	public void start() throws Exception;
    public void stop();
    public void stopSendingMessages();
    
    public void sendMessage(Message msg) throws Exception;
}
