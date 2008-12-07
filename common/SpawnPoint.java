package common;

import common.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpawnPoint implements Constants {
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

    int x; // x coordinate in map units
    int y; // y coordinate in map units
    
    /**
     * Creates a new spawn point object with input x coordinate and y coordinate.
     * @param x the x coordinate in map units
     * @param y the y coordinate in map units
     */
    public SpawnPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public SpawnPoint(common.Position pos) {
        x = (int) (pos.getX() - 0.5f);
        y = (int) (pos.getY() - 0.5f);
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public Position getPosition()
    {
    	return new Position(x + 0.5f, y + 0.5f);
    }
}
