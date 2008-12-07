package client.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import common.Player;

public class PlayerHealthMeter extends ProgressMeter
{
    private Player player;
    
    public PlayerHealthMeter(int x, int y, int width, int height, Color c, Player p)
    {
        super(x, y, width, height, c, p.getHealth(), 1000);
        
        player = p;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player p)
    {
        player = p;
    }
    
    public void draw(Graphics2D g, int windowWidth, int windowHeight)
    {
        setValue(player.getHealth());
        
        super.draw(g, windowWidth, windowHeight);
    }
}
