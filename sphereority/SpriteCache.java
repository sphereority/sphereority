package sphereority;
/**
 * Curso Básico de desarrollo de Juegos en Java - Invaders
 * 
 * (c) 2004 Planetalia S.L. - Todos los derechos reservados. Prohibida su reproducción
 * 
 * http://www.planetalia.com
 * 
 */

import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

public class SpriteCache extends ResourceCache{
	
	protected Object loadResource(URL url) {
		try {
			return ImageIO.read(url);
		} catch (Exception e) {
			System.out.println("Image Error "+url);
			System.out.println("Error : "+e.getClass().getName()+" "+e.getMessage());
			System.exit(0);
			return null;
		}
	}
	
	public BufferedImage getSprite(String name) {
		return (BufferedImage)getResource(name);
	}
}
