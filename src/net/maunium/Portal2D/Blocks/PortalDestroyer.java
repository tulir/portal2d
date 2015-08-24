package net.maunium.Portal2D.Blocks;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import net.maunium.Portal2D.BlockRegistry;
import net.maunium.Portal2D.Portal2D;
import net.maunium.Portal2D.Map.Map;
import net.maunium.Portal2D.Util.Vector;

public class PortalDestroyer extends AbstractBlock {
	public static final int ID = 6;
	public static final Color COLORID = new Color(0, 150, 255);
	
	public PortalDestroyer(Portal2D host) throws SlickException {
		canWalkThrough = true;
		canShootThrough = false;
		canAttachPortal = false;
		collisionPriority = 80;
		texture = host.getImage("blocks/portal_destroyer");
	}
	
	@Override
	public void onCollide(Portal2D host, GameContainer gc, Map map, Vector v) {
		map.portal_blue.setLocation(Vector.NULL);
		map.portal_orange.setLocation(Vector.NULL);
	}
}
