package sphereority;

import java.awt.image.ImageObserver;

public interface Stage extends ImageObserver {
	public SpriteCache getSpriteCache();
	public void addActor(Actor a);
	public Player getPlayer();
	public void gameOver();
}
