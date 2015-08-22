package net.maunium.Portal2D.Map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import net.maunium.Portal2D.BlockRenderer.BlockType;
import net.maunium.Portal2D.Util.Vector;
import net.maunium.Portal2D.Util.Vector.SideHit;

/**
 * Portal bullet.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class PortalBullet {
	public static Image BLUE_BULLET, ORANGE_BULLET;
	public static final int PIXELS_PER_SECOND = 20 * 32;
	public float x, y, dx, dy, angle;
	public boolean blue;
	
	public PortalBullet(boolean blue) {
		this(0f, 0f, 0f, 0f, blue);
	}
	
	public PortalBullet(float x, float y, int mouseX, int mouseY, boolean blue) {
		this.x = x;
		this.y = y;
		this.blue = blue;
		dy = PIXELS_PER_SECOND * (mouseY - y) / (Math.abs(mouseY - y) + Math.abs(mouseX - x));
		dx = PIXELS_PER_SECOND * (mouseX - x) / (Math.abs(mouseY - y) + Math.abs(mouseX - x));
		
		// Calculate the angle from the player to the mouse in radians.
		double angle = -Math.atan2(dx, dy);
		// Convert the angle to degrees.
		this.angle = (float) Math.toDegrees(angle < 0 ? angle + 2 * Math.PI : angle);
	}
	
	public PortalBullet(float x, float y, float dx, float dy, boolean blue) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.blue = blue;
	}
	
	public void render(Graphics g) {
		Image i = blue ? BLUE_BULLET : ORANGE_BULLET;
		i.setRotation(angle);
		g.drawImage(i, x, y);
	}
	
	public Vector update(int delta, BlockType[][] blocks) {
		x += delta / 1000.0f * dx;
		y += delta / 1000.0f * dy;
		
		BlockType hitBlock = blocks[(int) (x - x % 32) / 32][(int) (y - y % 32) / 32];
		if (hitBlock != null && hitBlock.isSolid()) {
			if (hitBlock == BlockType.LIGHT) {
				int blockMiddleX = (int) (x - x % 32) + 16;
				int blockMiddleY = (int) (y - y % 32) + 16;
				if (Math.abs(blockMiddleX - x) < Math.abs(blockMiddleY - y)) {
					if (blockMiddleY - y < 0) {
						return new Vector((int) (x - x % 32) / 32, (int) (y - y % 32) / 32, SideHit.BOTTOM);
					} else {
						return new Vector((int) (x - x % 32) / 32, (int) (y - y % 32) / 32, SideHit.TOP);
					}
				} else {
					if (blockMiddleX - x < 0) {
						return new Vector((int) (x - x % 32) / 32, (int) (y - y % 32) / 32, SideHit.RIGHT);
					} else {
						return new Vector((int) (x - x % 32) / 32, (int) (y - y % 32) / 32, SideHit.LEFT);
					}
				}
			} else {
				return Vector.NULL;
			}
		}
		return null;
	}
}
