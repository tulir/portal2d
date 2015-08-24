package net.maunium.Portal2D.Blocks;

import java.util.Comparator;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import net.maunium.Portal2D.BlockRegistry;
import net.maunium.Portal2D.Portal2D;
import net.maunium.Portal2D.Map.Map;
import net.maunium.Portal2D.Util.Vector;

public abstract class AbstractBlock {
	protected boolean canWalkThrough = false, canShootThrough = false, canAttachPortal = false;
	protected int collisionPriority = 1;
	protected Image texture;
	
	public void render(Graphics g, int x, int y) {
		g.drawImage(texture, x, y);
	}
	
	public void onCollide(Portal2D host, GameContainer gc, Map map, Vector v) {}
	
	public boolean canWalkThrough() {
		return canWalkThrough;
	}
	
	public boolean canShootThrough() {
		return canShootThrough;
	}
	
	public boolean canAttachPortal() {
		return canAttachPortal;
	}
	
	public int getCollisionPriority() {
		return collisionPriority;
	}
	
	public static class CollisionSorter implements Comparator<Vector> {
		private Map m;
		
		public CollisionSorter(Map m) {
			this.m = m;
		}
		
		@Override
		public int compare(Vector o1, Vector o2) {
			return Integer.compare(BlockRegistry.getBlockHandler(m.getBlockAt(o1.x, o1.y)).getCollisionPriority(),
					BlockRegistry.getBlockHandler(m.getBlockAt(o2.x, o2.y)).getCollisionPriority());
		}
		
		public static void sort(List<Vector> vs, Map m) {
			vs.sort(new CollisionSorter(m));
		}
	}
}
