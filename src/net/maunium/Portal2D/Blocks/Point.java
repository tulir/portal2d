package net.maunium.Portal2D.Blocks;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import net.maunium.Portal2D.BlockRegistry;
import net.maunium.Portal2D.Portal2D;
import net.maunium.Portal2D.Map.Map;
import net.maunium.Portal2D.Util.Vector;

/**
 * A collectable point.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class Point extends AbstractBlock {
	public static final int ID = 3;
	public static final Color COLORID = new Color(0, 100, 0);
	
	public Point(Portal2D host) throws SlickException {
		canWalkThrough = true;
		canShootThrough = true;
		canAttachPortal = false;
		collisionPriority = 80;
		texture = host.getImage("blocks/point");
	}
	
	@Override
	public void onCollide(Portal2D host, GameContainer gc, Map map, Vector v) {
		map.setBlockAt(v.x, v.y, BlockRegistry.TILE_NULL);
		host.points++;
	}
}
