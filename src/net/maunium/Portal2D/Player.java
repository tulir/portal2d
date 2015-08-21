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
	private float x, y;
	private final Image i;
	
	public Player(Image i) {
		this.i = i;
	}
	
	public void render(Graphics g) {
		g.drawImage(i, x * 32, y * 32);
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
}
