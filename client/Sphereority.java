package client;
 
/**
 * This is the main client application
 */
 
import common.*;
import java.awt.BorderLayout;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import javax.swing.*;
 
public class Sphereority extends Thread implements Constants
{
    public static final String[] MAP_LIST = new String[]
        { "circles", "mercury", "random_1", "sample_map", "widefield" };
 
    public static Logger logger;
 
    private ClientConnection connection;
 
    private GameEngine game;
 
    private static ClientLogonDialog loginWindow;
 
    private static JDialog gameWindow;
 
    public Sphereority(GameEngine game, ClientConnection connection)
    {
        this.game = game;
        this.connection = connection;
    }
 
    public static void main(String[] args)
    {
        // Get the log level from the command-line if one is supplied
        initialiseLogger(args);
        logger.log(Level.INFO, "Started game");
 
        // Report the current log level to the log file
        logger.log(logger.getLevel(), "Log Level set to: " + logger.getLevel());

        // Create and display the LoginDialog
        loginWindow = new ClientLogonDialog(null);

        boolean bot = false;
        
        // Do this if we are not in debug mode
        if(args.length > 0) {
            for(String arg : args)
                if (bot = arg.equals("-debug"))
                    break;
        }
            // If the user quit the dialog, we must quit
        if (!bot && !loginWindow.show())
            System.exit(0);
        
        // Else play the game
        logger.config("Server Name: " + loginWindow.getServerName());
 
        Map map;
        GameEngine game;
        ClientConnection connection = null;
        
        do
        {
            // This grabs a random map on startup
            map = new Map(MAP_LIST[4]);
            Random random = new Random();
            byte userId = (byte)random.nextInt(100);
            String userName = bot ? "bot" + userId : loginWindow.getUserName();
            game = new GameEngine(map, userId, userName, bot);
 
            // Attempt to start a connection
            try
            {
                // Raw multicasting connection
                //connection = new ClientRawMulticastConnection(game);

                // ExtaSys multicasting connection
                connection = new ClientExtaSysConnection(InetAddress.getByName(SERVER_ADDRESS),SERVER_PORT, game);
                ((ClientExtaSysConnection)connection).establishServerConnection();
                
                // Set up the game gameWindow
                gameWindow = new JDialog();
                gameWindow.setTitle(CLIENT_WINDOW_NAME + " - " + userName);
                gameWindow.setModal(true);
     
                gameWindow.getContentPane().add(game.getGameViewArea(), BorderLayout.CENTER);
     
                gameWindow.pack();
                gameWindow.setLocationRelativeTo(null);
     
                Sphereority s = new Sphereority(game, connection);
            
                s.start();
     
                game.registerActionListeners(gameWindow);
                // Play the game once:
                gameWindow.setVisible(true);
                game.unregisterActionListeners(gameWindow);
            } catch (Exception ex)
            {
                JOptionPane.showMessageDialog(null, "Failed to connect to server.", "Sphereority", JOptionPane.ERROR_MESSAGE);
            }
            
            // Stop the game and the connection
            game.gameOver();
            connection.stop();
            // Show the login dialog again
        } while (!bot && loginWindow != null && loginWindow.show());
        // If quit, don't loop
 
        // TEMP: this is for testing only:
        if (gameWindow != null)
        	gameWindow.dispose();
 
        logger.log(Level.INFO, "Exiting game");
        System.exit(0);
    }
 
    public void run()
    {
        try
        {
            // Start the game
            game.play();
            // Start the client connection
            connection.start();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
 
		/*
		 * Allow the logger level to be set as a command-line paramater.
		 * The default logger level is CONFIG by default.
		 */
    public static void initialiseLogger(String[] args)
    {
        // Client application logging
        logger = Logger.getLogger(CLIENT_LOGGER_NAME);
        
        // Set the default log level if it is not specified
        logger.setLevel(Level.CONFIG);
        
        // Get the log level from the command-line if one is supplied
        if (args.length > 0)
        {
            /*
             * Logger levels: SEVERE (highest value) WARNING INFO CONFIG FINE
             * FINER FINEST (lowest value)
             */
 
            String level = args[0].trim().toUpperCase();
 
            if (level.equals("SEVERE"))
            {
                logger.setLevel(Level.SEVERE);
            } else if (level.equals("WARNING"))
            {
                logger.setLevel(Level.WARNING);
            } else if (level.equals("INFO"))
            {
                logger.setLevel(Level.INFO);
            } else if (level.equals("CONFIG"))
            {
                logger.setLevel(Level.CONFIG);
            } else if (level.equals("FINE"))
            {
                logger.setLevel(Level.FINE);
            } else if (level.equals("FINER"))
            {
                logger.setLevel(Level.FINER);
            } else if (level.equals("FINEST"))
            {
                logger.setLevel(Level.FINEST);
            }
        } 
 
        try
        {
            logger.addHandler(new FileHandler(CLIENT_LOG_PATH));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
