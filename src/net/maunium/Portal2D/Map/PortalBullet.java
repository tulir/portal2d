package net.maunium.Portal2D.Map;

/**
 * Portal bullet.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class PortalBullet {
	public float x, y, dx, dy;
	public boolean blue;
	
	public PortalBullet(boolean blue) {
		this(0, 0, 0, 0, blue);
	}
	
	public PortalBullet(float x, float y, boolean blue) {
		this(x, y, 0, 0, blue);
	}
	
	public PortalBullet(float x, float y, float dx, float dy, boolean blue) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.blue = blue;
	}
}
