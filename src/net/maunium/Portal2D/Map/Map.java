package net.maunium.Portal2D.Map;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.maunium.Portal2D.BlockRegistry;
import net.maunium.Portal2D.Portal2D;
import net.maunium.Portal2D.Util.Vector;

/**
 * Container for maps.
 * 
 * @author Tulir293
 * @author Antti
 * @since 0.1
 */
public class Map extends BasicGameState {
	public static final float MOVE_VELOCITY = 0.005f, JUMP_VELOCITY = 0.000008f;
	public static final int MS_BETWEEN_BULLETS = 500;
	private final int id;
	public final String name;
	protected final Image img;
	protected final Portal2D host;
	protected double angle;
	protected int[][] blocks;
	protected Portal portal_blue, portal_orange;
	protected List<PortalBullet> bullets;
	protected Player p;
	
	/**
	 * Construct a map based on the given Image.
	 */
	public Map(Portal2D host, Image img, String name, int id) throws SlickException {
		this.id = id;
		this.host = host;
		this.img = img;
		this.name = name;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		p = new Player(host.getImage("player"), host.getImage("player_eye"));
		bullets = new ArrayList<PortalBullet>();
		
		portal_blue = new Portal(host.getImage("blocks/portal_blue"));
		portal_orange = new Portal(host.getImage("blocks/portal_orange"));
		
		blocks = new int[img.getWidth()][];
		for (int x = 0; x < img.getWidth(); x++) {
			blocks[x] = new int[img.getHeight()];
			for (int y = 0; y < img.getHeight(); y++) {
				Color c = img.getColor(x, y);
				
				// The player spawn point
				if (c.getRed() == 255 && c.getGreen() == 0 && c.getBlue() == 0) {
					p.x = x;
					p.y = y;
				} else blocks[x][y] = BlockRegistry.getBlockId(c);
			}
		}
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		// Set the background color to RGB 50, 50, 50.
		g.setBackground(new Color(50, 50, 50));
		// Loop through the blocks.
		for (int x = 0; x < blocks.length; x++) {
			for (int y = 0; y < blocks[x].length; y++) {
				// Render each existing block. Ignore non-existing block positions.
				if (blocks[x][y] != Portal2D.TILE_NONE) BlockRegistry.render(g, blocks[x][y], x, y);
			}
		}
		
		for (PortalBullet pb : bullets)
			pb.render(g);
			
		// Render the portals.
		portal_blue.render(g);
		portal_orange.render(g);
		
		// Render the player.
		p.render(g, angle);
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		Player p = getPlayer();
		
		/*
		 * Update player location/velocity and handle movement input.
		 */
		p.update(gc.getInput(), this, delta);
		
		/*
		 * Portal bullet shooting.
		 */
		if (gc.getInput().isMousePressed(0) || gc.getInput().isMousePressed(1)) {
			bullets.add(
					new PortalBullet(p.x * 32 + 16, p.y * 32 + 16, gc.getInput().getMouseX(), gc.getInput().getMouseY(), gc.getInput().isMouseButtonDown(1)));
		}
		
		/*
		 * Handle bullet updates.
		 */
		for (int i = 0; i < bullets.size(); i++) {
			Vector v = bullets.get(i).update(delta, blocks);
			if (v == null) continue;
			if (v.x == -1 || v.y == -1) bullets.remove(i);
			else {
				boolean blue = bullets.get(i).blue;
				Portal p1 = blue ? portal_blue : portal_orange, p2 = blue ? portal_orange : portal_blue;
				if (p2.getLocation().equals(v)) p2.setLocation(Vector.NULL);
				p1.setLocation(v);
				bullets.remove(i);
			}
		}
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame game) throws SlickException {
		((AppGameContainer) gc).setDisplayMode(img.getWidth() * 32, img.getHeight() * 32, false);
	}
	
	@Override
	public int getID() {
		return id;
	}
	
	/**
	 * Get the block type at the given location.
	 * 
	 * @return The block type, or null if no block at given location.
	 */
	public int getBlockAt(int x, int y) {
		return blocks[x][y];
	}
	
	/**
	 * Get the player in this map.
	 */
	public Player getPlayer() {
		return p;
	}
	
	private Vector rotateVector(double x, double y) {
		double angle = Math.atan2(x, y);
		if (angle < 0) angle += Math.PI * 2;
		Vector rotatedVector = new Vector((int) (x * Math.cos(angle) - y * Math.sin(angle)), (int) (x * Math.sin(angle) + y * Math.cos(angle)));
		return rotatedVector;
	}
	
	/*
	 * TODO |-!!!------!!!!------!!!-| TODO |------------------------| TODO | FIX COLLISION CHECKING | TODO |------------------------| TODO
	 * |-!!!------!!!!------!!!-| It currently assumes player positions are in pixels, while they are in floating grid points.
	 */
//	private boolean checkCollision() {
//		// Returns true if a spike was hit.
//		int playerTileX = (int) (p.x - p.x % 32) / 32;
//		int playerTileY = (int) (p.y - p.y % 32) / 32;
//		for (int mx = -1; mx < 2; mx++) {
//			for (int my = -1; my < 2; my++) {
//				if (checkCollisionWith(playerTileX + mx, playerTileY + my)) return true;
//			}
//		}
//		return false;
//	}
//	
//	private boolean checkCollisionWith(int x, int y) {
//		// Return true if a spike gets hit.
//		if (Math.abs(p.x - x) < 32 && Math.abs(p.y - y) < 32) {
//			if (Math.abs(p.x - x) >= Math.abs(p.y - y)) {
//				// We want to create as little lag as possible, so we politely
//				// Show the player the door with the least moving required.
//				p.dx = 0;
//				if (p.x - x < 0) {
//					p.x -= p.x % 32;
//				} else {
//					p.x += 32 - p.x % 32;
//				}
//			} else {
//				p.dy = 0;
//				if (p.y - y < 0) {
//					p.y -= p.y % 32;
//				} else {
//					p.y += 32 - p.y % 32;
//				}
//			}
//			if (blocks[x][y] == BlockType.BOMB) return true;
//		}
//		return false;
//	}
//	
//	/**
//	 * Checks collision between the player and the two portals. If a player is to teleport, this method also does that.
//	 * 
//	 * @author Antti
//	 */
//	private void checkPortalCollision() {
//		if (portal_blue == null || portal_orange == null) return;
//		if (!checkCollisionWithPortal(portal_blue)) {
//			checkCollisionWithPortal(portal_orange);
//		}
//		return;
//	}
//	
//	private boolean checkCollisionWithPortal(Portal portal) {
//		if (Math.abs(p.x - portal.getLocation().x) < 32 && Math.abs(p.y - portal.getLocation().y) < 32) {
//			if (Math.abs(p.y - portal.getLocation().y) < 2) {
//				if (p.x - portal.getLocation().x < 0) {
//					if (portal.getLocation().sideHit == SideHit.LEFT) teleport(portal);
//					else return false;
//				} else {
//					if (portal.getLocation().sideHit == SideHit.RIGHT) teleport(portal);
//					else return false;
//				}
//			} else if (Math.abs(p.x - portal.getLocation().x) < 2) {
//				if (p.y - portal.getLocation().y < 0) {
//					if (portal.getLocation().sideHit == SideHit.TOP) teleport(portal);
//					else return false;
//				} else {
//					if (portal.getLocation().sideHit == SideHit.BOTTOM) teleport(portal);
//					else return false;
//				}
//			} else return false;
//		} else return false;
//		return true;
//	}
//	
//	private void teleport(Portal currentPortal) {
//		Portal targetPortal = currentPortal == portal_blue ? portal_orange : portal_blue;
//		float newDX = p.dx, newDY = p.dy;
//		
//		// Get the amount of rotations needed
//		int rotationsNeeded = SideHit.toInt(currentPortal.getLocation().sideHit) - SideHit.toInt(targetPortal.getLocation().sideHit);
//		if (rotationsNeeded < 0) {
//			rotationsNeeded += 4;
//		}
//		
//		// Do the rotations
//		for (int rotations = 0; rotations < rotationsNeeded; rotations++) {
//			Vector tempVector = rotateVector(newDX, newDY);
//			newDX = tempVector.x;
//			newDY = tempVector.y;
//		}
//		// Apply the new speed vector
//		p.dx = newDX;
//		p.dy = newDY;
//		
//		// Then after modifying the speed, teleport to the correct tile.
//		int x = SideHit.toInt(targetPortal.getLocation().sideHit);
//		int cx = x == 0 ? 0 : x - 2;
//		int cy = x == 3 ? 0 : x - 1;
//		
//		p.x = (targetPortal.getLocation().x + cx) * 32;
//		p.y = (targetPortal.getLocation().y + cy) * 32;
//	}
}
