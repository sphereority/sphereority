package client.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import common.Constants;
import common.Player;

public class PlayerHealthMeter extends ProgressMeter implements Constants
{
    private Player player;
    
    public PlayerHealthMeter(int x, int y, int width, int height, Color c, Player p)
    {
        super(x, y, width, height, c, DEFAULT_ACTOR_HEALTH, MAXIMUM_ACTOR_HEALTH);
        
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
        if (player != null)
            setValue(player.getHealth());
        
        super.draw(g, windowWidth, windowHeight);
    }
}
