package net.maunium.Portal2D.Map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * Container for the player.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class Player {
	/** The position and movement vectors of this player. */
	public float x, y, dx, dy;
	/** The texture of this player. */
	private Image image, eye;
	
	/**
	 * Construct a Player with the given texture.
	 */
	public Player(Image i, Image eye) {
		image = i;
		this.eye = eye;
	}
	
	/**
	 * Render the player.
	 */
	public void render(Graphics g, double d) {
		eye.setRotation((float) d - 45.0f);
		g.drawImage(image, x * 32, y * 32);
		g.drawImage(eye, x * 32, y * 32);
	}
}
