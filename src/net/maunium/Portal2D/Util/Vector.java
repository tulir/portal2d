package net.maunium.Portal2D.Util;

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
				case 3:
					return RIGHT;
				default:
					return null;
			}
		}
		public static int toInt(SideHit s) {
			switch (s) {
				case TOP:
					return 0;
				case LEFT:
					return 1;
				case BOTTOM:
					return 2;
				case RIGHT:
					return 3;
				default:
					return -1;
			}
		}
	}
	
	public Vector(int x, int y, SideHit sideHit) {
		this.x = x;
		this.y = y;
		this.sideHit = sideHit;
	}
	public Vector(int x, int y) {
		this.x = x;
		this.y = y;
		this.sideHit = null;
	}
}
