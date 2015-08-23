package net.maunium.Portal2D.Map;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
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
import net.maunium.Portal2D.Util.Vector.SideHit;

/**
 * Container for maps.
 * 
 * @author Tulir293
 * @author Antti
 * @since 0.1
 */
public class Map extends BasicGameState {
	public static final int MS_BETWEEN_BULLETS = 500;
	public final String name;
	protected final Image rawMap;
	private final Portal2D host;
	private final int id;
	protected int shiftX = 0, shiftY = 0, drawAreaWidth, drawAreaHeight;
	protected int[][] blocks;
	protected Portal portal_blue, portal_orange;
	protected List<PortalBullet> bullets;
	protected Player player;
	
	/**
	 * Construct a map based on the given Image.
	 */
	public Map(Portal2D host, Image rawMap, String name, int id) throws SlickException {
		this.id = id;
		this.host = host;
		this.rawMap = rawMap;
		this.name = name;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		player = new Player(host.getImage("player"), host.getImage("player_eye"));
		
		portal_blue = new Portal(host.getImage("blocks/portal_blue"));
		portal_orange = new Portal(host.getImage("blocks/portal_orange"));
		
		resetMap();
	}
	
	public void resetMap() {
		portal_blue.setLocation(Vector.NULL);
		portal_orange.setLocation(Vector.NULL);
		bullets = new ArrayList<PortalBullet>();
		
		shiftX = 0;
		shiftY = 0;
		
		blocks = new int[rawMap.getWidth()][];
		for (int x = 0; x < rawMap.getWidth(); x++) {
			blocks[x] = new int[rawMap.getHeight()];
			for (int y = 0; y < rawMap.getHeight(); y++) {
				Color c = rawMap.getColor(x, y);
				
				// The player spawn point
				if (c.getRed() == 255 && c.getGreen() == 0 && c.getBlue() == 0) {
					player.x = x;
					player.y = y;
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
				if (blocks[x][y] > Portal2D.TILE_NONE) BlockRegistry.render(g, blocks[x][y], x, y, shiftX, shiftY);
			}
		}
		
		for (PortalBullet pb : bullets)
			pb.render(g, shiftX, shiftY);
			
		// Render the portals.
		portal_blue.render(g, shiftX, shiftY);
		portal_orange.render(g, shiftX, shiftY);
		
		// Render the player.
		player.render(g, shiftX, shiftY);
		
		g.setColor(Color.white);
		g.drawString("Points: " + host.points, 5, 5);
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		Player p = getPlayer();
		
		if (gc.getInput().isKeyPressed(Keyboard.KEY_ESCAPE)) {
			game.enterState(0);
		} else if (gc.getInput().isKeyPressed(Keyboard.KEY_R)) {
			portal_blue.setLocation(Vector.NULL);
			portal_orange.setLocation(Vector.NULL);
		}
		
		// Temporary controls for shifting the view.
		if (gc.getInput().isKeyPressed(Keyboard.KEY_LEFT)) shiftX -= 32;
		else if (gc.getInput().isKeyPressed(Keyboard.KEY_RIGHT)) shiftX += 32;
		if (gc.getInput().isKeyPressed(Keyboard.KEY_UP)) shiftY -= 32;
		else if (gc.getInput().isKeyPressed(Keyboard.KEY_DOWN)) shiftY += 32;
		
		/*
		 * Update player location/velocity and handle movement input.
		 */
		p.update(gc.getInput(), this, delta, shiftX, shiftY);
		
		/*
		 * Portal bullet shooting.
		 */
		if (gc.getInput().isMousePressed(0) || gc.getInput().isMousePressed(1)) {
			bullets.add(new PortalBullet((int) (p.x * 32 + 16), (int) (p.y * 32 + 16), gc.getInput().getMouseX(), gc.getInput().getMouseY(),
					gc.getInput().isMouseButtonDown(1), shiftX, shiftY));
		}
		
		/*
		 * Handle bullet updates.
		 */
		for (int i = 0; i < bullets.size(); i++) {
			Vector v = bullets.get(i).update(delta, this);
			if (v == null) continue;
			if (v.x == -1 || v.y == -1) bullets.remove(i);
			else {
				boolean blue = bullets.get(i).isBlue;
				Portal p1 = blue ? portal_blue : portal_orange, p2 = blue ? portal_orange : portal_blue;
				if (p2.getLocation().equals(v)) p2.setLocation(Vector.NULL);
				p1.setLocation(v);
				bullets.remove(i);
			}
		}
		
		// Check collisions with portals.
		checkPortals();
		
		// Check collisions with blocks and handle them accordingly.
		List<Vector> vs = checkCollision();
		for (Vector v : vs) {
			switch (getBlockAt(v.x, v.y)) {
				case Portal2D.TILE_BOMB:
					// The player hit a bomb. Reset score and go to the menu.
					host.points = 0;
					game.enterState(0);
					break;
				case Portal2D.TILE_POINT:
					// The player hit a point. Collect it.
					blocks[v.x][v.y] = Portal2D.TILE_NONE;
					host.points++;
					break;
				case Portal2D.TILE_FINISH:
					game.enterState(game.getCurrentStateID() + 1);
					break;
			}
		}
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame game) throws SlickException {
		((AppGameContainer) gc).setDisplayMode(rawMap.getWidth() * 32, rawMap.getHeight() * 32, false);
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		resetMap();
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
		if (x > -1 && y > -1 && blocks.length > x && blocks[x].length > y) return blocks[x][y];
		else return Portal2D.TILE_NONE;
	}
	
	/**
	 * Get the player in this map.
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Check if the player is colliding with something.
	 * 
	 * @return The Vector location of the block the player collided with.
	 */
	public List<Vector> checkCollision() {
		List<Vector> hitBlocks = new ArrayList<Vector>();
		int playerTileX = (int) player.x;
		int playerTileY = (int) player.y;
		for (int mx = -1; mx < 2; mx++) {
			for (int my = -1; my < 2; my++) {
				Vector v = checkCollisionWith(playerTileX + mx, playerTileY + my);
				if (!v.equals(Vector.NULL)) hitBlocks.add(v);
			}
		}
		return hitBlocks;
	}
	
	private Vector checkCollisionWith(int x, int y) {
		// Return true if a spike gets hit. This is the block in that tile.
		int blockAt = getBlockAt(x, y);
		if (Math.abs(player.x - x) < 1 && Math.abs(player.y - y) < 1) {
			if (!BlockRegistry.isSolid(blockAt)) return new Vector(x, y);
			// We want to create as little lag as possible, so we politely show the player the door with the least
			// moving required.
			if (Math.abs(player.x - x) >= Math.abs(player.y - y) - 6 / 32) {
				player.dx = 0;
				if (player.x - x < 0) player.x += (int) player.x - player.x;
				else player.x += (int) player.x - player.x + 1;
			} else {
				player.dy = 0;
				if (player.y - y < 0) player.y += (int) player.y - player.y;
				else player.y += (int) player.y - player.y + 1;
			}
			return new Vector(x, y);
		}
		return Vector.NULL;
	}
	
	/**
	 * Checks collision between the player and the two portals. If the player has collided with a portal, this will
	 * teleport them to the opposite portal. This also ignores everything if either portal has not been set.
	 */
	public void checkPortals() {
		// Make sure both portals have been set.
		if (portal_blue.getLocation().equals(Vector.NULL) || portal_orange.getLocation().equals(Vector.NULL)) return;
		// Check if the player is collided with the blue portal. If not, check with the orange one.
		if (!checkCollisionWithPortal(portal_blue)) checkCollisionWithPortal(portal_orange);
		return;
	}
	
	/**
	 * Check a collision with a single portal.
	 * 
	 * @return True if the player collided with a portal and was teleported. False otherwise.
	 */
	private boolean checkCollisionWithPortal(Portal portal) {
		if (Math.abs(player.x - portal.getLocation().x) < 1 && Math.abs(player.y - portal.getLocation().y) < 1) {
			if (Math.abs(player.y - portal.getLocation().y) < 5f / 32f) {
				if (player.x - portal.getLocation().x < 0) {
					if (portal.getLocation().sideHit == SideHit.LEFT) teleport(portal);
					else return false;
				} else {
					if (portal.getLocation().sideHit == SideHit.RIGHT) teleport(portal);
					else return false;
				}
			} else if (Math.abs(player.x - portal.getLocation().x) < 5f / 32f) {
				if (player.y - portal.getLocation().y < 0) {
					if (portal.getLocation().sideHit == SideHit.TOP) teleport(portal);
					else return false;
				} else {
					if (portal.getLocation().sideHit == SideHit.BOTTOM) teleport(portal);
					else return false;
				}
			} else return false;
		} else return false;
		return true;
	}
	
	/**
	 * Teleport the player from the given portal to the opposite one.
	 */
	private void teleport(Portal currentPortal) {
		Portal targetPortal = currentPortal == portal_blue ? portal_orange : portal_blue;
		
		int hitSide = SideHit.toInt(currentPortal.getLocation().sideHit);
		
		// Get the amount of rotations needed
		int rotationsNeeded = hitSide - SideHit.toInt(targetPortal.getLocation().sideHit);
		if (hitSide % 2 != 0) {
			player.dx *= -1;
			player.dy = 0;
		} else {
			player.dy *= -1;
			player.dx = 0;
		}
		
		float newDX = (float) (player.dx * Math.cos(-Math.PI / 2 * rotationsNeeded) - player.dy * Math.sin(-Math.PI / 2 * rotationsNeeded));
		player.dy = (float) (player.dx * Math.sin(-Math.PI / 2 * rotationsNeeded) + player.dy * Math.cos(-Math.PI / 2 * rotationsNeeded));
		// Apply the new speed vector
		player.dx = newDX;
		
		// Then after modifying the speed, teleport to the correct tile.
		int x = SideHit.toInt(targetPortal.getLocation().sideHit);
		int cx = x == 0 ? 0 : x - 2;
		int cy = x == 3 ? 0 : x - 1;
		
		player.x = targetPortal.getLocation().x + cx * (33 / 32);
		player.y = targetPortal.getLocation().y + cy * (33 / 32);
	}
	private void updateDisplay(GameContainer gc) throws SlickException{
		int windowWidth = gc.getWidth();
		int windowHeight = gc.getHeight();
		int preferredWindowWidth = windowWidth;
		int preferredWindowHeight = windowHeight;
		if (windowWidth > rawMap.getWidth()*32) {
			preferredWindowWidth = rawMap.getWidth()*32;
		} 
		if (windowHeight > rawMap.getHeight()*32) {
			preferredWindowHeight = rawMap.getHeight()*32;
		}
		((AppGameContainer) gc).setDisplayMode(preferredWindowWidth, preferredWindowHeight, false);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
