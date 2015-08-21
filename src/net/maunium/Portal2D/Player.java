package net.maunium.Portal2D;

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
	private final Image i;
	
	/**
	 * Construct a Player with the given texture.
	 */
	public Player(Image i) {
		this.i = i;
	}
	
	/**
	 * Render the player.
	 */
	public void render(Graphics g) {
		g.drawImage(i, x * 32, y * 32);
	}
}
