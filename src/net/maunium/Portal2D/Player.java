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
	public float x, y, dx, dy;
	private final Image i;
	
	public Player(Image i) {
		this.i = i;
	}
	
	public void render(Graphics g) {
		g.drawImage(i, x * 32, y * 32);
	}
}
