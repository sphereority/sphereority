package server;

import common.Constants;
import java.net.InetAddress;

import java.io.IOException;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

class SphereorityServer implements Constants {
	public static Logger logger = Logger.getLogger(SERVER_LOGGER_NAME);

	public static void main (String [] args){
	    initialiseLogger(args);

	    // Report the current log level to the log file
	    logger.log(logger.getLevel(), "Log Level set to: " +  logger.getLevel());

	    try {
	        ServerGameEngine engine = new ServerGameEngine();
	        ServerConnection connection = new ServerConnection(InetAddress.getByName(SERVER_ADDRESS),
	                                                           SERVER_PORT,
	                                                           engine);
	        connection.Start();
	        // System.out.println("Server Started: Waiting for connections");
			    logger.log(Level.INFO, "Server Started: Waiting for connections");

	        while(true) {
	            Thread.yield();
	        }
	    }
	    catch (Exception e){
	        e.printStackTrace();
	    }
	}

	/*
	 * Allow the logger level to be set as a command-line paramater.
	 * The default logger level is CONFIG by default.
	 */
	public static void initialiseLogger(String[] args) {
		// Server application logging
	  logger = Logger.getLogger(SERVER_LOGGER_NAME);

	  // Get the log level from the command-line if one is supplied
	  if (args.length > 0) {
	    /*
	     * Logger levels:
       * SEVERE (highest value)
       * WARNING
       * INFO
       * CONFIG
       * FINE
       * FINER
       * FINEST (lowest value)
	     */

	    String level = args[0].trim().toUpperCase();

	    if (level.equals("SEVERE")) {
	      logger.setLevel(Level.SEVERE);
	    }
	    else if (level.equals("WARNING")) {
	      logger.setLevel(Level.WARNING);
	    }
	    else if (level.equals("INFO")) {
	      logger.setLevel(Level.INFO);
	    }
	    else if (level.equals("CONFIG")) {
	      logger.setLevel(Level.CONFIG);
	    }
	    else if (level.equals("FINE")) {
	      logger.setLevel(Level.FINE);
	    }
	    else if (level.equals("FINER")) {
	      logger.setLevel(Level.FINER);
	    }
	    else if (level.equals("FINEST")) {
	      logger.setLevel(Level.FINEST);
	    }
	  }
	  else {
	    // Set the default log level if it is not specified
	    logger.setLevel(Level.CONFIG);
	  }

	  try {
	    logger.addHandler(new FileHandler(SERVER_LOG_PATH));
	  }
	  catch (IOException e) {
	    e.printStackTrace();
		}
	}
}