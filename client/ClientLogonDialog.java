package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ClientLogonDialog implements ActionListener, KeyListener
{
	protected JDialog dialog;
	
	protected JTextField entryServer, entryName;
	protected JPasswordField entryPassword;
	protected JButton buttonOkay, buttonCancel;
	protected JLabel statusMessage;
	
	// Results of the dialog
	protected String serverName;
	protected String userName;
	protected String serverPassword;
	protected boolean result;
	
	/*
	 * TODO: Fixed key-detection code!
	 */
	
	public ClientLogonDialog(Frame owner)
	{
		dialog = new JDialog(owner, true);
		dialog.setContentPane(new JPanel(new BorderLayout()));
		
		entryServer = new JTextField();
		entryServer.addActionListener(this);
		entryName = new JTextField();
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
		panel.add(new JLabel("Server IP or name:"));
		panel.add(entryServer);
		panel.add(new JLabel("User name:"));
		panel.add(entryName);
		panel.add(new JLabel("Server password:"));
		panel.add(entryPassword);
		
		dialog.getContentPane().add(panel, BorderLayout.CENTER);
		
		panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(statusMessage);
		panel.add(buttonOkay);
		panel.add(buttonCancel);
		
		dialog.getContentPane().add(panel, BorderLayout.SOUTH);
		
		dialog.pack();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if (source.equals(buttonCancel))
		{
			cancel();
		}
		else if (source.equals(buttonOkay))
		{
			login();
		}
		else
		{
			dialog.transferFocus();
		}
	}
	
	public boolean show()
	{
		// Center the dialog on the parent
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
		
		return result;
	}
	
	protected void cancel()
	{
		result = false;
		dialog.setVisible(false);
	}
	
	protected boolean login()
	{
		result = true;
		if (entryServer.getText().length() < 1)
		{
			statusMessage.setText("Need server name!");
			result = false;
		}
		if (entryName.getText().length() < 1)
		{
			if (result)
				statusMessage.setText("Need user name!");
			else
				statusMessage.setText("Need server and user names!");
			result = false;
		}
		
		if (result)
		{
			serverName = entryServer.getText();
			serverPassword = new String(entryPassword.getPassword());
			userName = entryName.getText();
			
			dialog.setVisible(false);
		}
		else
			for (Component c : dialog.getContentPane().getComponents())
				c.validate();
		
		return result;
	}

	public void keyPressed(KeyEvent e)
	{
		keyTyped(e);
	}
	public void keyReleased(KeyEvent e) { }
	public void keyTyped(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			dialog.setVisible(false);
		}
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
}
