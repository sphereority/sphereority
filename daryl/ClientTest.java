package daryl;

import java.awt.*;
import javax.swing.*;

import common.*;
import client.ClientViewArea;
import client.gui.*;

public class ClientTest implements ActionCallback
{	
	public static void main(String[] args)
	{
		JFrame window = new JFrame("Sphereority client test");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ClientViewArea cva = new ClientViewArea();
		cva.setPlayer(new Player());
		window.getContentPane().add(cva, BorderLayout.CENTER);
		window.addKeyListener(cva);
		
		SimpleButton sb = new SimpleButton(-10, 10, 50, 15, "Quit", Color.red);
		sb.addCallback(new ClientTest());
		cva.addWidget(sb);
		cva.addWidget(new SimpleButton(2, 450, 50, 15, "Test", Color.orange));
		
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

	public void actionCallback(InteractiveWidget source, int buttons)
	{
		System.out.println("Quit button pressed, exiting...");
		System.exit(0);
	}
}
