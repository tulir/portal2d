package net.maunium.Portal2D.Blocks;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

import net.maunium.Portal2D.Portal2D;

public class BlockDark extends AbstractBlock {
	public static final int ID = 1;
	public static final Color COLORID = new Color(50, 50, 50);
	
	public BlockDark(Portal2D host) throws SlickException {
		canWalkThrough = false;
		canShootThrough = false;
		canAttachPortal = false;
		collisionPriority = 10;
		texture = host.getImage("blocks/tile_dark");
	}
}
