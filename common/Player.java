package	common;

import java.awt.Graphics2D;

import client.gui.GuiUtils;

/**
 * This (presently) represents a player
 * @author dvanhumb
 * @author smaboshe
 *
 */
public abstract class Player extends WeightedPosition {
	// INSTANCE METHODS
	
	
	// CONSTRUCTORS
	public Player() {
		super();
	}
	
		
	
	// GETTERS
	
	
	
	// SETTERS
	
	
	// OPERATIONS
	public void draw(Graphics2D g, float scale) {
		if (team == TEAM_A)
			g.setColor(TEAM_A_COLOR);
		else if (team == TEAM_B)
			g.setColor(TEAM_B_COLOR);
		else
			g.setColor(TEAMLESS_COLOR);
		
		GuiUtils.drawFilledOctagon(g, Math.round(position.getX()*scale), Math.round(position.getY()*scale), scale*PLAYER_SIZE);
	}

  public void collision(Actor a) {
		if (a instanceof Stone) {
			System.out.println("You hit a stone!");
		}		
	} 
}
