package net.maunium.Portal2D;

/**
 * Vector class. Contains three values, X-coordinate, Y-coordinate and the side that was hit.
 * 
 * @author Antti
 * @since 0.1
 */
public class Vector {
	public int x, y;
	public SideHit sideHit;
	
	public static enum SideHit {
		TOP, BOTTOM, LEFT, RIGHT;
		public static SideHit fromInt(int i) {
			switch (i) {
				case 0:
					return TOP;
				case 1:
					return LEFT;
				case 2:
					return BOTTOM;
				default:
					return RIGHT;
			}
		}
	}
	
	public Vector(int x, int y, SideHit sideHit) {
		this.x = x;
		this.y = y;
		this.sideHit = sideHit;
	}
}
