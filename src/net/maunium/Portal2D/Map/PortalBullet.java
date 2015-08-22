package net.maunium.Portal2D.Map;

import org.newdawn.slick.Image;

import net.maunium.Portal2D.BlockRenderer.BlockType;
import net.maunium.Portal2D.Util.Vector;

/**
 * Portal bullet.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class PortalBullet {
	public static Image BLUE_BULLET, ORANGE_BULLET;
	public static final int PIXELS_PER_SECOND = 20 * 32;
	public float x, y, dx, dy;
	public boolean blue;
	
	public PortalBullet(boolean blue) {
		this(0f, 0f, 0f, 0f, blue);
	}
	
	public PortalBullet(float x, float y, int mouseX, int mouseY, boolean blue) {
		this.x = x;
		this.y = y;
		this.blue = blue;
		dy = PIXELS_PER_SECOND * (mouseY - y) / (Math.abs(mouseY - y) + Math.abs(mouseX - x));
		dx = PIXELS_PER_SECOND - dy;
		
	}
	
	public PortalBullet(float x, float y, float dx, float dy, boolean blue) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.blue = blue;
	}
	
	public Vector update(int delta, BlockType[][] blocks) {
		x += delta / 1000 * dx;
		y += delta / 1000 * dy;
		
		BlockType hitBlock = blocks[(int) (x - x % 32) / 32][(int) (x - x % 32) / 32];
		if (hitBlock.isSolid()) {
		
		}
		return null;
	}
}
