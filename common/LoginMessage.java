package common;

import java.nio.charset.Charset;
import java.nio.ByteBuffer;

public class LoginMessage {
    // Constants
    public static final String CHARSET_NAME = "UTF-8";
    public static final String NAME_HEADER = "NAME=";
    public static final String PASS_HEADER = "PASS=";
    public static final String PORT_HEADER = "PORT=";
    public static final String SEPERATOR = "~";

    // get the whole message as a string
    public static String getMessageString(byte [] messagedata){
	Charset charset = Charset.forName(CHARSET_NAME);
	ByteBuffer buffer = ByteBuffer.allocate(messagedata.length);
	buffer.put(messagedata);
	buffer.rewind();
	String message = new String(charset.decode(buffer).array());

	return message;
    }
    /*
     * login message
    */
    public static boolean isLoginMessage(byte [] messagedata){
	String messagestring = getMessageString(messagedata);
	return messagestring.startsWith("LOGIN" + SEPERATOR);
    }
    public static byte[] getLoginMessage(String username, String passwd){
	Charset charset = Charset.forName(CHARSET_NAME);
	String message = new String("LOGIN" + SEPERATOR + "NAME=" + username + SEPERATOR +  "PASS=" + passwd + SEPERATOR);
	return charset.encode(message).array();
    }
    public static String getUserName(byte [] messagedata){
	String message = getMessageString(messagedata);
	int start = message.indexOf(NAME_HEADER) + NAME_HEADER.length();
	int end = message.indexOf(SEPERATOR,start);

	return message.substring(start,end);
    }
    public static String getUserPass(byte [] messagedata){
	String message = getMessageString(messagedata);
	int start = message.indexOf(PASS_HEADER) + PASS_HEADER.length();
	int end = message.indexOf(SEPERATOR,start);

	return message.substring(start,end);
    }
    /*
     * Login Success Message
    */
    public static boolean isLoginSuccessMessage(byte [] messagedata){
	String message = getMessageString(messagedata);
	return message.startsWith("SUCCESS" + SEPERATOR);
    }
    public static byte [] getLoginSuccessMessage(int port){
	Charset charset = Charset.forName(CHARSET_NAME);
	String message = new String("SUCCESS" + SEPERATOR + PORT_HEADER + Integer.toString(port) + SEPERATOR);
	return charset.encode(message).array();
    }
    public static int getPort(byte [] messagedata){
	Charset charset = Charset.forName(CHARSET_NAME);
	ByteBuffer buffer = ByteBuffer.allocate(messagedata.length);
	buffer.put(messagedata);
	buffer.rewind();
	String message = new String(charset.decode(buffer).array());

	int start = message.indexOf(PORT_HEADER) + PORT_HEADER.length();
	int end = message.indexOf(SEPERATOR,start);
	
	return Integer.parseInt(message.substring(start,end));
    }
}
