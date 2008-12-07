package client;

import common.Constants;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class ClientLogonDialog implements ActionListener, KeyEventDispatcher, Constants {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

	protected JDialog dialog;
	
	// Entry widgets
	protected JTextField entryServer, entryName;
	protected JPasswordField entryPassword;

	// Buttons
	protected JButton buttonOkay, buttonCancel;

	// Labels
	protected JLabel statusMessage, labelServer, labelName, labelPassword;
	
	// Results of the dialog
	protected String serverName;
	protected String userName;
	protected String serverPassword;
	protected boolean result;
	
	public ClientLogonDialog(Frame owner)
	{
		dialog = new JDialog(owner, true);
		dialog.setContentPane(new JPanel(new BorderLayout()));
		
		entryServer = new JTextField("SphereorityServer");
		entryServer.addActionListener(this);
		entryName = new JTextField("User");
		entryName.addActionListener(this);
		entryPassword = new JPasswordField();
		entryPassword.addActionListener(this);
		statusMessage = new JLabel();
		statusMessage.setForeground(Color.red);
		statusMessage.setVisible(false);
		
		buttonOkay = new JButton("Login");
		buttonOkay.addActionListener(this);
		buttonOkay.setMnemonic('L');
		buttonCancel = new JButton("Quit");
		buttonCancel.addActionListener(this);
		buttonCancel.setMnemonic('Q');
		
		JPanel panel = new JPanel(new GridLayout(0, 2));
		panel.add(labelServer = new JLabel("Server IP or name:"));
		panel.add(entryServer);
		panel.add(labelName = new JLabel("User name:"));
		panel.add(entryName);
		panel.add(labelPassword = new JLabel("Server password:"));
		panel.add(entryPassword);
		
		dialog.getContentPane().add(panel, BorderLayout.CENTER);
		
		panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(statusMessage);
		panel.add(buttonOkay);
		panel.add(buttonCancel);
		
		dialog.getContentPane().add(panel, BorderLayout.SOUTH);
		
		dialog.pack();
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if (source.equals(buttonCancel))
		{
			cancel();
		}
		else if (source.equals(entryServer) && entryName.getText().length() < 1)
		{
			entryName.grabFocus();
		}
		else if (source.equals(buttonOkay) || source instanceof JTextField)
		{
			login();
		}
	}
	
	public boolean show()
	{
		// Reset the labels
		labelServer.setForeground(Color.black);
		labelName.setForeground(Color.black);
		labelPassword.setForeground(Color.black);
		statusMessage.setVisible(false);
		// Center the dialog on the parent
		dialog.setLocationRelativeTo(dialog.getParent());
		dialog.setVisible(true);
		
		return result;
	}
	
	protected void cancel()
	{
		result = false;
		dialog.setVisible(false);
		logger.log(Level.INFO, "Exiting game");
	}
	
	protected boolean login()
	{
		result = true;
		if (entryServer.getText().length() < 1)
		{
			statusMessage.setText("Need server name!");
			labelServer.setForeground(Color.red);
			result = false;
		}
		else
			labelServer.setForeground(Color.black);
		if (entryName.getText().length() < 1)
		{
			if (result)
				statusMessage.setText("Need user name!");
			else
				statusMessage.setText("Need server and user name!");
			labelName.setForeground(Color.red);
			result = false;
		}
		else
			labelName.setForeground(Color.black);
		
		if (result)
		{
			statusMessage.setVisible(false);
			serverName = entryServer.getText();
			serverPassword = new String(entryPassword.getPassword());
			userName = entryName.getText();
			
			dialog.setVisible(false);
		}
		else
			statusMessage.setVisible(true);
		
		return result;
	}

	public String getServerName()
	{
		return serverName;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public String getPassword()
	{
		return serverPassword;
	}

	public boolean dispatchKeyEvent(KeyEvent e)
	{
		// If the Escape key was pressed, press the cancel button
		if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ESCAPE)
			cancel();
		
		return false;
	}
}
