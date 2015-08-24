package net.maunium.Portal2D.Blocks;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

import net.maunium.Portal2D.Portal2D;

public class BlockLight extends AbstractBlock {
	public static final int ID = 2;
	public static final Color COLORID = new Color(200, 200, 200);
	
	public BlockLight(Portal2D host) throws SlickException {
		canWalkThrough = false;
		canShootThrough = false;
		canAttachPortal = true;
		collisionPriority = 50;
		texture = host.getImage("blocks/tile_light");
	}
}