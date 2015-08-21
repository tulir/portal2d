package net.maunium.Portal2D;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * Wrapper for portals. Contains the texture (blue or orange), x coordinate and y coordinate. Set to -1 to hide portal.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class Portal {
	private int x = -1, y = -1;
	private Image i;
	
	/**
	 * Construct a new Portal with the given texture.
	 */
	public Portal(Image i) {
		this.i = i;
	}
	
	/**
	 * Render this portal at the given location.
	 */
	public void render(Graphics g) {
		if (x == -1 || y == -1) return;
		else g.drawImage(i, x * 32, y * 32);
	}
	
	/**
	 * Get the X coordinate of this Portal.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Set the X coordinate of this Portal.
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Get the Y coordinate of this Portal.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Set the Y coordinate of this Portal.
	 */
	public void setY(int y) {
		this.y = y;
	}
}
