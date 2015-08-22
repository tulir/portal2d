package net.maunium.Portal2D.Map;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import net.maunium.Portal2D.BlockRegistry;

/**
 * Container for the player.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class Player {
	public static final float MOVE_VELOCITY = 0.005f, JUMP_VELOCITY = 0.008f, GRAVITY_CHANGE = 0.00001f, GRAVITY = -0.1f;
	public final int pixelRadius = 15, pixelDiameter = 30;
	public final float tileRadius = pixelRadius / 32, tileDiameter = pixelDiameter / 32;
	/** The position and movement vectors of this player. */
	public float x, y, dx, dy;
	/** The texture of this player. */
	private Image image, eye;
	
	/**
	 * Construct a Player with the given texture.
	 */
	public Player(Image i, Image eye) {
		image = i;
		this.eye = eye;
	}
	
	/**
	 * Render the player.
	 */
	public void render(Graphics g, double d) {
		eye.setRotation((float) d - 45.0f);
		g.drawImage(image, x * 32, y * 32);
		g.drawImage(eye, x * 32, y * 32);
	}
	
	public void update(Input i, Map m, int delta) {
		int mX = i.getMouseX(), mY = i.getMouseY();
		
		// Calculate the angle from the player to the mouse in radians.
		double ang = -Math.atan2(x * 32 + pixelRadius - mX, y * 32 + pixelRadius - mY);
		// Convert the angle to degrees.
		m.angle = Math.toDegrees(ang < 0 ? ang + 2 * Math.PI : ang);
		
		/*
		 * Handle left/right presses.
		 */
		boolean a = i.isKeyDown(Keyboard.KEY_A), d = i.isKeyDown(Keyboard.KEY_D);
		if (a && !d) dx = -MOVE_VELOCITY;
		else if (!a && d) dx = MOVE_VELOCITY;
		else dx = 0.0f;
		
		/*
		 * Handle up/down presses.
		 */
		int below = m.getBlockAt((int) (x + 0.5), (int) y + 1);
		if (i.isKeyDown(Keyboard.KEY_SPACE) && dy == 0 && BlockRegistry.isSolid(below)) dy = JUMP_VELOCITY;
		else if (dy > GRAVITY) dy -= delta * GRAVITY_CHANGE;
		
		/*
		 * Update player location.
		 */
		x += delta * dx;
		y -= delta * dy;
	}
}
