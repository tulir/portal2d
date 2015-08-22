package net.maunium.Portal2D.Map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import net.maunium.Portal2D.Util.Vector;

/**
 * Wrapper for portals. Contains the texture (blue or orange), x coordinate and y coordinate. Set to -1 to hide portal.
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
	
	public Vector getLocation() {
		return location;
	}
	
	public void setLocation(Vector v) {
		if (v.sideHit != null && v.x >= 0 && v.y >= 0) location = v;
	}
}
