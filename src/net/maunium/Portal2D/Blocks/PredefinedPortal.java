package net.maunium.Portal2D.Blocks;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import net.maunium.Portal2D.Portal2D;
import net.maunium.Portal2D.Map.Map;
import net.maunium.Portal2D.Util.Vector;
import net.maunium.Portal2D.Util.Vector.SideHit;

public class PredefinedPortal extends AbstractBlock {
	// WORK IN PROGRESS!!
	// NOTICE THE ARROW TEXTURE! IT GOES TO THE HOLE AND POINTS AT THE 
	// TARGET PORTAL
	public static final int ID = 7;
	public static final Color COLORID = new Color(255, 255, 255);
	public int arrowAngle;
	public boolean isActive;
	// Opacity changes the target
	private PredefinedPortal targetPortal;
	private Vector positionVector;
	// private Image ArrowTexture;
	
	public PredefinedPortal(Portal2D host, PredefinedPortal targetPortal, int thisX, int thisY) throws SlickException {
		canWalkThrough = true;
		canShootThrough = false;
		canAttachPortal = false;
		collisionPriority = 75;
		texture = host.getImage("blocks/portal_predefined");
		
		setTargetPortal(thisX,thisY,targetPortal);
	}
	public PredefinedPortal(Portal2D host) throws SlickException {
		canWalkThrough = true;
		canShootThrough = false;
		canAttachPortal = false;
		collisionPriority = 75;
		texture = host.getImage("blocks/portal_predefined");
	}
	public void setTargetPortal(int thisX, int thisY, PredefinedPortal targetPortal) {
		positionVector = new Vector(thisX,thisY,SideHit.NULL);
		this.targetPortal = targetPortal;
		updateArrowAngle();
	}
	private static int updateArrowAngle() {
		// Why doesn't this work?
		// return (int)Math.atan2(targetPortal.positionVector.x-positionVector.x, positionVector.y-targetPortal.positionVector.y);
		return -1;
	}
	
	@Override
	public void onCollide(Portal2D host, GameContainer gc, Map map, Vector v) {
			
		if(Math.abs(map.player.x - positionVector.x) < 1.0/2.0 && Math.abs(map.player.y - positionVector.y) < 1.0/2.0) {
			if (isActive) {
				map.player.x = targetPortal.positionVector.x;
				map.player.y = targetPortal.positionVector.y;
				targetPortal.isActive = false;
			}
		} else {
			isActive = true;
		}
	}
		@Override
	public void render(Graphics g, int x, int y) {
		g.drawImage(texture, x, y);
		if (isActive) {
			// Draw the arrow with the rotation given by arrowAngle
		}
	}	
}
