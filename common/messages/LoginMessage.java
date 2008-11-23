package common.messages;

import java.nio.charset.Charset;
import java.nio.ByteBuffer;

public class LoginMessage {
	// Constants
	private static final String CHARSET_NAME = "UTF-8";
	private static final String NAME_HEADER = "NAME=";
	private static final String PASS_HEADER = "PASS=";
	private static final String PORT_HEADER = "PORT=";
	private static final String FAIL_REASON_HEADER  = "REASON=";
	private static final String SEPERATOR = "~";
	
	// Constructors

	
	
	// Getters
	
	
	
	// Setters
	
	
	
	// Operations
	
	/*
	 * get the whole message as a string
	 */
	public static String getMessageString(byte [] messagedata) {
		Charset charset = Charset.forName(CHARSET_NAME);
		ByteBuffer buffer = ByteBuffer.allocate(messagedata.length);
		buffer.put(messagedata);
		buffer.rewind();
		String message = new String(charset.decode(buffer).array());
		return message;
	}
	/*
	 * Login message
	 */
	public static boolean isLoginMessage(byte [] messagedata) {
		String messagestring = getMessageString(messagedata);
		return messagestring.startsWith("LOGIN" + SEPERATOR);
	}

	public static byte[] getLoginMessage(String username, String passwd) {
		System.out.println("LoginMessage.getLoginMessage");
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
		int end = message.indexOf(SEPERATOR, start);

		return Integer.parseInt(message.substring(start, end));
	}
	
	/*
	 * LOGIN FAILURE
	 */
	public static boolean isLoginFailureMessage(byte [] messagedata){
		String message = getMessageString(messagedata);
		return message.startsWith("FAILURE" + SEPERATOR);
	}
	
	public static byte [] getLoginFailreMessage(String reason){
		Charset charset = Charset.forName(CHARSET_NAME);
		String message = new String("FAILURE" + SEPERATOR + FAIL_REASON_HEADER + reason + SEPERATOR);
		return charset.encode(message).array();
	}
	
	public static String getFailureReason(byte [] messagedata){
		String message = getMessageString(messagedata);
		int start = message.indexOf(FAIL_REASON_HEADER) + FAIL_REASON_HEADER.length();
		int end = message.indexOf(SEPERATOR,start);
		return message.substring(start,end);
	}
}
