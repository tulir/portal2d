package net.maunium.Portal2D.Blocks;

import java.util.Comparator;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import net.maunium.Portal2D.BlockRegistry;
import net.maunium.Portal2D.Portal2D;
import net.maunium.Portal2D.Vector;
import net.maunium.Portal2D.Map.Map;

/**
 * Base class for all block handlers.
 * 
 * @author Tulir293
 * @since 0.1
 */
public abstract class AbstractBlock {
	protected boolean canWalkThrough = false, canShootThrough = false, canAttachPortal = false;
	protected int collisionPriority = 1;
	protected Image texture;
	
	/**
	 * Render this block at the given grid position using the given Graphics instance.
	 */
	public void render(Graphics g, int x, int y) {
		g.drawImage(texture, x, y);
	}
	
	/**
	 * Handle a collision with the given Vector location.
	 * 
	 * @param host The game main class instance in use.
	 * @param gc The game container in use.
	 * @param map The map in use.
	 * @param v The vector location where the block is located at.
	 */
	public void onCollide(Portal2D host, GameContainer gc, Map map, Vector v) {}
	
	/**
	 * Check if the player should be able to pass through this block type.
	 */
	public boolean canWalkThrough() {
		return canWalkThrough;
	}
	
	/**
	 * Check if portal bullets should be able to pass through this block type.
	 */
	public boolean canShootThrough() {
		return canShootThrough;
	}
	
	/**
	 * Check if portal bullets can be attached to this block type.
	 */
	public boolean canAttachPortal() {
		return canAttachPortal;
	}
	
	/**
	 * Get the collision priority of this block. Higher priorities are handled first.
	 */
	public int getCollisionPriority() {
		return collisionPriority;
	}
	
	/**
	 * A Comparator to use for collision lists to sort them.
	 * 
	 * @author Tulir293
	 * @since 0.1
	 */
	public static class CollisionSorter implements Comparator<Vector> {
		private Map m;
		
		public CollisionSorter(Map m) {
			this.m = m;
		}
		
		@Override
		public int compare(Vector o1, Vector o2) {
			return Integer.compare(BlockRegistry.getBlockHandler(m.getBlockAt(o2.x, o2.y)).getCollisionPriority(),
					BlockRegistry.getBlockHandler(m.getBlockAt(o1.x, o1.y)).getCollisionPriority());
		}
		
		/**
		 * Sort the given list of Vectors using the blocks in the given map.
		 */
		public static void sort(List<Vector> vs, Map m) {
			vs.sort(new CollisionSorter(m));
		}
	}
}
