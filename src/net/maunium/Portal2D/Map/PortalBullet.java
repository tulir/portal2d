package net.maunium.Portal2D.Map;

import java.util.HashMap;
import java.util.Map.Entry;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import net.maunium.Portal2D.BlockRegistry;
import net.maunium.Portal2D.Vector;
import net.maunium.Portal2D.Vector.SideHit;

/**
 * Portal bullet class.
 * 
 * @author Tulir293
 * @author Antti
 * @since 0.1
 */
public class PortalBullet {
	public static Image BLUE_BULLET, ORANGE_BULLET;
	public static final int PIXELS_PER_SECOND = 40 * 32;
	public int x, y;
	public float dx, dy, angle;
	public boolean isBlue;
	
	public PortalBullet(int x, int y, int mouseX, int mouseY, boolean isBlue, int shiftX, int shiftY) {
		this.x = x;
		this.y = y;
		mouseX -= shiftX;
		mouseY -= shiftY;
		this.isBlue = isBlue;
		dy = PIXELS_PER_SECOND * (mouseY - y) / (Math.abs(mouseY - y) + Math.abs(mouseX - x));
		dx = PIXELS_PER_SECOND * (mouseX - x) / (Math.abs(mouseY - y) + Math.abs(mouseX - x));
		
		// Calculate the angle from the player to the mouse in radians.
		double angle = -Math.atan2(dx, dy);
		// Convert the angle to degrees.
		this.angle = (float) Math.toDegrees(angle < 0 ? angle + 2 * Math.PI : angle);
	}
	
	/**
	 * Render the portal bullet.
	 */
	public void render(Graphics g, int shiftX, int shiftY) {
		Image i = isBlue ? BLUE_BULLET : ORANGE_BULLET;
		i.setRotation(angle);
		g.drawImage(i, x + shiftX, y + shiftY);
	}
	
	/**
	 * Update the portal bullet location.
	 */
	public Vector update(int delta, Map map) {
		/*
		 * Increase X and Y by delta times the change of X and Y where delta is the time since the
		 * last update in seconds and the change of X and Y have been calculated in the constructor
		 * using the shooting angle.
		 */
		x += delta / 1000.0f * dx;
		y += delta / 1000.0f * dy;
		
		// Get the block where the bullet is.
		int hitBlock = map.getBlockAt((x - x % 32) / 32, (y - y % 32) / 32);
		// Check if the block allows portal blocks to pass through.
		if (!BlockRegistry.canShootThrough(hitBlock)) {
			// Check if a portal can be attached to the block that was hit.
			if (BlockRegistry.canAttachPortal(hitBlock)) {
				int blockMiddleX = x - x % 32 + 16;
				int blockMiddleY = y - y % 32 + 16;
				HashMap<Integer, Double> possibleValues = new HashMap<Integer, Double>();
				/*
				 * Fairly complicated magic to see if a portal can be validly placed. This will
				 * cover most cases where the portal tries to go in the middle of two blocks.
				 */
				for (int side = 0; side < 4; side++) {
					int test = map.getBlockAt((x - x % 32) / 32 + (side == 0 ? 0 : side - 2), (y - y % 32) / 32 + (side == 3 ? 0 : side - 1));
					if (BlockRegistry.canShootThrough(test)) {
						if ((side == 0 ? 0 : side - 2) * dx <= 0 && (side == 3 ? 0 : side - 1) * dy <= 0) {
							possibleValues.put(side, (double) (Math.abs(blockMiddleX + (side == 0 ? 0 : side - 2) * 16 - x)
									+ Math.abs(blockMiddleY + (side == 3 ? 0 : side - 1) * 16 - y)));
						}
					}
				}
				
				double smallestValue = Double.MAX_VALUE;
				int smallestKey = 0;
				for (Entry<Integer, Double> e : possibleValues.entrySet()) {
					if (e.getValue() < smallestValue) {
						smallestValue = e.getValue();
						smallestKey = e.getKey();
					}
				}
				if (smallestValue < Double.MAX_VALUE) return new Vector((x - x % 32) / 32, (y - y % 32) / 32, SideHit.fromInt(smallestKey));
				else return Vector.NULL;
				/*
				 * The portal checking magic ends here.
				 */
			} else {
				// Portal can't be attached, return a vector null.
				return Vector.NULL;
			}
		}
		// Bullet can continue freely, return null.
		return null;
	}
}
