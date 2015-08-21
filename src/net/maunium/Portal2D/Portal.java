package net.maunium.Portal2D;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Portal {
	private int x = -1, y = -1;
	private Image i;
	
	public Portal(Image i) {
		this.i = i;
	}
	
	public void render(Graphics g) {
		if (x == -1 || y == -1) return;
		else g.drawImage(i, x * 32, y * 32);
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
