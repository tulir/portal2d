package net.maunium.Portal2D.Map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import net.maunium.Portal2D.Vector;

/**
 * Wrapper for portals. Contains the texture (blue or orange) and the location as a vector. If the vector is null, the
 * portal will be hidden.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class Portal {
	private Vector location;
	private Image image;
	
	/**
	 * Construct a new Portal with the given texture.
	 */
	public Portal(Image i) {
		image = i;
		location = Vector.NULL;
	}
	
	/**
	 * Render this portal at its location.
	 */
	public void render(Graphics g, int shiftX, int shiftY) {
		if (location == null) return;
		
		image.setRotation(0);
		switch (location.sideHit) {
			case NULL:
				return;
			case LEFT:
				image.rotate(90);
			case BOTTOM:
				image.rotate(90);
			case RIGHT:
				image.rotate(90);
			case TOP:
		}
		
		g.drawImage(image, location.x * 32 + shiftX, location.y * 32 + shiftY);
	}
	
	/**
	 * Get the location of this portal.
	 */
	public Vector getLocation() {
		return location;
	}
	
	/**
	 * Set the location of this portal.
	 */
	public void setLocation(Vector v) {
		if (v.sideHit != null) location = v;
	}
}
