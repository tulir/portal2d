package net.maunium.Portal2D.Blocks;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import net.maunium.Portal2D.Portal2D;
import net.maunium.Portal2D.Map.Map;
import net.maunium.Portal2D.Util.Vector;

/**
 * The exit point.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class Finish extends AbstractBlock {
	public static final int ID = 4;
	public static final Color COLORID = new Color(0, 255, 0);
	
	public Finish(Portal2D host) throws SlickException {
		canWalkThrough = false;
		canShootThrough = false;
		canAttachPortal = false;
		collisionPriority = 70;
		texture = host.getImage("blocks/finish");
	}
	
	@Override
	public void onCollide(Portal2D host, GameContainer gc, Map map, Vector v) {
		host.enterState(host.getCurrentStateID() + 1);
	}
}
