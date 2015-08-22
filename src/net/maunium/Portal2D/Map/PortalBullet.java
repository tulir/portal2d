package net.maunium.Portal2D.Map;

import java.util.HashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import net.maunium.Portal2D.BlockRegistry;
import net.maunium.Portal2D.Portal2D;
import net.maunium.Portal2D.Util.Vector;
import net.maunium.Portal2D.Util.Vector.SideHit;

/**
 * Portal bullet.
 * 
 * @author Tulir293
 * @author Antti
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
	
	public Vector update(int delta, int[][] blocks) {
		x += delta / 1000.0f * dx;
		y += delta / 1000.0f * dy;
		
		int hitBlock = blocks[(int) (x - x % 32) / 32][(int) (y - y % 32) / 32];
		if (hitBlock != Portal2D.TILE_NONE && BlockRegistry.isSolid(hitBlock)) {
			if (hitBlock == Portal2D.TILE_LIGHT) {
				int blockMiddleX = (int) (x - x % 32) + 16;
				int blockMiddleY = (int) (y - y % 32) + 16;
				HashMap<Integer, Double> possibleValues = new HashMap<Integer, Double>();
				// Checking if there can possibly be a portal there.
				for (int side = 0; side < 4; side++) {
					int test = blocks[(int) (x - x % 32) / 32 + (side == 0 ? 0 : side - 2)][(int) (y - y % 32) / 32 + (side == 3 ? 0 : side - 1)];
					int testX = (int) (x - x % 32) / 32 + (side == 0 ? 0 : side - 2);
					int testY = (int) (y - y % 32) / 32 + (side == 3 ? 0 : side - 1);
					int blockX = (int) (x - x % 32) / 32;
					int blockY = (int) (y - y % 32) / 32;
					if (BlockRegistry.isSolid(hitBlock)) {
						if ((side == 0 ? 0 : side - 2) * dx <= 0 && (side == 3 ? 0 : side - 1) * dy <= 0) {
							if (Math.abs(blockMiddleX + (side == 0 ? 0 : side - 2) * 16 - x)
									+ Math.abs(blockMiddleY + (side == 3 ? 0 : side - 1) * 16 - y) <= 16) { return new Vector((int) (x - x % 32) / 32,
											(int) (y - y % 32) / 32, SideHit.fromInt(side)); }
						}
					}
				}
			} else {
				return Vector.NULL;
			}
		}
		return null;
	}
}
