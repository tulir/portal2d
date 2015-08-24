package net.maunium.Portal2D.Blocks;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import net.maunium.Portal2D.Portal2D;
import net.maunium.Portal2D.Map.Map;
import net.maunium.Portal2D.Util.Vector;

public class Bomb extends AbstractBlock {
	public static final int ID = 5;
	public static final Color COLORID = new Color(255, 0, 255);
	
	public Bomb(Portal2D host) throws SlickException {
		canWalkThrough = false;
		canShootThrough = false;
		canAttachPortal = false;
		collisionPriority = 20;
		texture = host.getImage("blocks/bomb");
	}
	
	@Override
	public void onCollide(Portal2D host, GameContainer gc, Map map, Vector v) {
		host.enterState(0);
		host.points = 0;
	}
}