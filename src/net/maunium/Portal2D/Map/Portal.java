package net.maunium.Portal2D.Map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import net.maunium.Portal2D.Util.Vector;

/**
 * Wrapper for portals. Contains the texture (blue or orange) and the location as a vector. If the vector is null, the portal will be hidden.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class Portal {
	private Vector location;
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
		if (location == null) return;
		else g.drawImage(i, location.x * 32, location.y * 32);
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
		if (v.sideHit != null && v.x >= 0 && v.y >= 0) location = v;
	}
}
