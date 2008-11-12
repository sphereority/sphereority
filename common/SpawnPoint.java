package common;

public class SpawnPoint {
    int x; // x coordinate in map units
    int y; // y coordinate in map units
    
    /**
     * Creates a new spawn point object with input x coordinate and y coordinate.
     * @param x the x coordinate in map units
     * @param y the y coordinate in map units
     */
    SpawnPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}
