package net.maunium.Portal2D.Map;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.maunium.Portal2D.BlockRenderer;
import net.maunium.Portal2D.BlockRenderer.BlockType;
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
	public static final float MOVE_VELOCITY = 0.005f, JUMP_VELOCITY = 0.000008f;
	private final int id;
	private final Image img;
	private final Portal2D host;
	private double angle;
	private BlockType[][] blocks;
	private Portal portal_blue, portal_orange;
	private Player p;
	
	/**
	 * Construct a map based on the given Image.
	 */
	public Map(Portal2D host, Image img, int id) throws SlickException {
		this.id = id;
		this.host = host;
		this.img = img;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		p = new Player(host.getImage("player"), host.getImage("player_eye"));
		
		portal_blue = new Portal(host.getImage("blocks/portal_blue"));
		portal_orange = new Portal(host.getImage("blocks/portal_orange"));
		
		blocks = new BlockType[img.getWidth()][];
		for (int x = 0; x < img.getWidth(); x++) {
			blocks[x] = new BlockType[img.getHeight()];
			for (int y = 0; y < img.getHeight(); y++) {
				Color c = img.getColor(x, y);
				
				// The player spawn point
				if (c.getRed() == 255 && c.getGreen() == 0 && c.getBlue() == 0) {
					p.x = x;
					p.y = y;
				} else blocks[x][y] = BlockRenderer.getBlockType(c);
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
				if (blocks[x][y] != null) BlockRenderer.render(g, blocks[x][y], x, y);
			}
		}
		
		// Render the portals.
		portal_blue.render(g);
		portal_orange.render(g);
		
		// Render the player.
		p.render(g, angle);
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		Player p = getPlayer();
		
		// Calculate the angle from the player to the mouse in radians.
		double ang = -Math.atan2(p.x * 32 + p.size - gc.getInput().getMouseX(), p.y * 32 + p.size - gc.getInput().getMouseY());
		// Convert the angle to degrees.
		angle = Math.toDegrees(ang < 0 ? ang + 2 * Math.PI : ang);
		
		/*
		 * Handle left/right presses.
		 */
		boolean a = gc.getInput().isKeyDown(Keyboard.KEY_A), d = gc.getInput().isKeyDown(Keyboard.KEY_D);
		if (a && !d) p.dx = -MOVE_VELOCITY;
		else if (!a && d) p.dx = MOVE_VELOCITY;
		else p.dx = 0.0f;
		
		/*
		 * Handle up/down presses.
		 */
		if (gc.getInput().isKeyDown(Keyboard.KEY_SPACE) && p.dy == 0 && getBlockAt((int) (p.x + 0.5), (int) p.y + 1) != null) p.dy = 0.004f;
		else if (gc.getInput().isKeyDown(Keyboard.KEY_S)) p.dy = -MOVE_VELOCITY;
		else if (gc.getInput().isKeyDown(Keyboard.KEY_W)) p.dy = MOVE_VELOCITY;
		else if (p.dy > 0.0f) p.dy -= delta * JUMP_VELOCITY;
		else if (p.dy < 0.0f) p.dy = 0.0f;
		
		/*
		 * Update the player position.
		 */
		p.x += delta * p.dx;
		p.y -= delta * p.dy;
		
		/*
		 * Portal shooting
		 */
		if (gc.getInput().isMouseButtonDown(0) || gc.getInput().isMouseButtonDown(1))
			shootPortal(gc.getInput().isMousePressed(0) ? portal_blue : portal_orange);
	}
	
	public void shootPortal(Portal po) {
		Player p = getPlayer();
		Vector rt = rayTrace(angle, (int) (p.x * 32 + p.size), (int) (p.y * 32 + p.size));
		if (validPortalBlock(rt)) po.setLocation(rt);
	}
	
	public boolean validPortalBlock(Vector rt) {
		// TODO: Check if portal can be placed.
		return true;
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
	public BlockType getBlockAt(int x, int y) {
		return blocks[x][y];
	}
	
	/**
	 * Get the player in this map.
	 */
	public Player getPlayer() {
		return p;
	}
	
	/**
	 * Ray trace method. TODO: Add proper javadoc
	 * 
	 * @author Antti
	 */
	private Vector rayTrace(double angle, int playerX, int playerY) {
		if (angle % 90 == 0) {
			angle += 1 / 10000;
		}
		Vector portalHit = new Vector(0, 0, Vector.SideHit.TOP);
		double b = Math.tan(Math.toRadians(angle));
		double c = playerY - b * playerX;
		
		Vector directionVector = new Vector(0, 0, Vector.SideHit.TOP);
		
		if (angle < 180) {
			directionVector.y = 1;
		} else {
			directionVector.y = -1;
		}
		if (angle > 90 && angle < 270) {
			directionVector.x = -1;
		} else {
			directionVector.x = 1;
		}
		
		double currentX = playerX;
		int nextVerticalLimit = 0; // Y-position of the limit
		int nextHorizontalLimit = 0; // X-position of the limit
		double distToVertLim;
		double distToHorLim;
		while (true) {
			// Getting the next limits.
			// x
			if (currentX % 32 == 0) {
				nextHorizontalLimit = (int) (currentX / 32 + directionVector.x) * 32;
			} else {
				nextHorizontalLimit = (int) (currentX - currentX % 32);
				if (directionVector.x > 0) {
					nextHorizontalLimit += 32;
				}
			}
			
			// y
			if ((currentX * b + c) % 32 == 0) {
				nextVerticalLimit = (int) ((currentX * b + c) / 32 + directionVector.y) * 32;
			} else {
				nextVerticalLimit = (int) (currentX * b + c - (currentX * b + c) % 32);
				if (directionVector.y > 0) {
					nextVerticalLimit += 32;
				}
			}
			
			// Calculating the distances to the limits.
			distToVertLim = Math.abs((nextVerticalLimit - b * currentX - c) / b - currentX);
			distToHorLim = Math.abs(nextHorizontalLimit - currentX);
			
			// Testing which limit is closer.
			if (distToVertLim <= distToHorLim) {
				BlockRenderer.BlockType blockType = blocks[(int) ((currentX - currentX % 32) / 32)][nextVerticalLimit / 32 + directionVector.y];
				playerX += directionVector.y * distToVertLim;
				if (blockType == BlockRenderer.BlockType.LIGHT) {
					// 0 top 1 left 2 bottom 3 right
					portalHit.sideHit = SideHit.fromInt(directionVector.y + 1);
					// We get the x and y values of the blockwe're in.
					portalHit.x = (int) ((currentX - currentX % 32) / 32);
					portalHit.y = nextVerticalLimit / 32 + directionVector.y;
					return portalHit;
					// It's either null (there's nothing there) or there's a place we can add a portal or
					// There is a place we can't add a portal to. So, If we can't add a portal there we
					// Just return.
				} else if (blockType != null) return null;
			} else {
				// We get the block that is at the position we crashed to.
				BlockRenderer.BlockType blockType = blocks[nextHorizontalLimit / 32
						+ directionVector.x][(int) ((b * currentX + c - (b * currentX + c) % 32) / 32)];
				playerX += directionVector.x * distToVertLim;
				// If we can place a portal on the surface, we do so, returning the surface.
				if (blockType == BlockRenderer.BlockType.LIGHT) {
					// 0 top 1 left 2 bottom 3 right
					portalHit.sideHit = SideHit.fromInt(directionVector.x + 2);
					portalHit.x = nextHorizontalLimit / 32 + directionVector.x;
					// From x we calculate y.
					portalHit.y = (int) ((b * currentX + c - (b * currentX + c) % 32) / 32);
					return portalHit;
					// It's either null (there's nothing there) or there's a place we can add a portal or
					// There is a place we can't add a portal to. So, If we can't add a portal there we
					// Just return.
				} else if (blockType != null) return null;
			}
		}
	}
	
	private Vector rotateVector(double x, double y) {
		double angle = Math.atan2(x, y);
		angle = angle < 0 ? angle + Math.PI * 2 : angle;
		
		Vector rotatedVector = new Vector((int) (x * Math.cos(angle) - y * Math.sin(angle)), (int) (x * Math.sin(angle) + y * Math.cos(angle)));
		return rotatedVector;
	}
	
	private boolean checkCollision() {
		// Returns true if a spike was hit.
		int playerTileX = (int) (p.x - p.x % 32) / 32;
		int playerTileY = (int) (p.y - p.y % 32) / 32;
		for (int mx = -1; mx < 2; mx++) {
			for (int my = -1; my < 2; my++) {
				if (checkCollisionWith(playerTileX + mx, playerTileY + my)) { return true; }
			}
		}
		return false;
	}
	
	private boolean checkCollisionWith(int x, int y) {
		// Return true if a spike gets hit.
		if (Math.abs(p.x - x) < 32 && Math.abs(p.y - y) < 32) {
			if (Math.abs(p.x - x) >= Math.abs(p.y - y)) {
				// We want to create as little lag as possible, so we politely
				// Show the player the door with the least moving required.
				p.dx = 0;
				if (p.x - x < 0) {
					p.x -= p.x % 32;
				} else {
					p.x += 32 - p.x % 32;
				}
			} else {
				p.dy = 0;
				if (p.y - y < 0) {
					p.y -= p.y % 32;
				} else {
					p.y += 32 - p.y % 32;
				}
			}
			if (blocks[x][y] == BlockType.SPIKE) { return true; }
		}
		return false;
	}
}
