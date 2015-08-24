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
	/** The default velocity values/changes. */
	public static final float MOVE_VELOCITY = 0.005f, MOVE_VELOCITY_AIR = 0.0005f, JUMP_VELOCITY = 0.008f, GRAVITY_CHANGE = 0.00001f, GRAVITY = -0.1f;
	/** The angle from the player to the mouse. */
	public double eyeAngle;
	/** The radius and diameter of the player in pixels. */
	public final int pixelRadius = 15, pixelDiameter = 30;
	/** The radius and diameter of the player in tiles. */
	public final float tileRadius = pixelRadius / 32, tileDiameter = pixelDiameter / 32;
	/** The position and movement vectors of this player. */
	public float x, y, dx, dy;
	/** The texture of this player. */
	private Image body, eye;
	
	/**
	 * Construct a Player with the given texture.
	 */
	public Player(Image body, Image eye) {
		this.body = body;
		this.eye = eye;
	}
	
	/**
	 * Render the player.
	 */
	public void render(Graphics g, int shiftX, int shiftY) {
		eye.setRotation((float) eyeAngle - 45.0f);
		g.drawImage(body, x * 32 + shiftX, y * 32 + shiftY);
		g.drawImage(eye, x * 32 + shiftX, y * 32 + shiftY);
	}
	
	public void update(Input input, Map map, int delta, int shiftX, int shiftY) {
		int mX = input.getMouseX(), mY = input.getMouseY();
		
		// Calculate the angle from the player to the mouse in radians.
		eyeAngle = -Math.atan2(x * 32 + pixelRadius - mX + shiftX, y * 32 + pixelRadius - mY + shiftY);
		// Convert the angle to degrees.
		eyeAngle = Math.toDegrees(eyeAngle < 0 ? eyeAngle + 2 * Math.PI : eyeAngle);
		
		boolean onGround = dy == 0 && (!BlockRegistry.canWalkThrough(map.getBlockAt((int) x, (int) y + 1))
				|| !BlockRegistry.canWalkThrough(map.getBlockAt((int) (x + 1), (int) y + 1)));
				
		/*
		 * Handle left/right presses.
		 */
		// Get the status of the A and D keys.
		boolean a = input.isKeyDown(Keyboard.KEY_A), d = input.isKeyDown(Keyboard.KEY_D);
		
		// If the player is on the ground, give full control over movement.
		if (onGround) {
			// If A is down but D is not, set horizontal velocity to negative MOVE_VELOCITY.
			if (a && !d) dx = -MOVE_VELOCITY;
			// If A is not down but D is, set horizontal velocity to positive MOVE_VELOCITY.
			else if (!a && d) dx = MOVE_VELOCITY;
			// If neither or both keys are down, set horizontal velocity to zero.
			else dx = 0.0f;
		} else {
			if (a && !d && dx >= -MOVE_VELOCITY) dx -= MOVE_VELOCITY_AIR;
			else if (!a && d && dx <= MOVE_VELOCITY) dx += MOVE_VELOCITY_AIR;
		}
		
		// If the horizontal velocity is positive, decrease it by GRAVITY_CHANGE.
		if (dx > 0f) dx -= GRAVITY_CHANGE;
		// If the horizontal velocity is negative, increase it by GRAVITY_CHANGE.
		else if (dx < 0f) dx += GRAVITY_CHANGE;
		
		/*
		 * Handle up/down presses.
		 */
		// If space key is pressed and the player is on the ground, set vertical velocity to JUMP_VELOCITY.
		if (input.isKeyPressed(Keyboard.KEY_SPACE) && onGround) dy = JUMP_VELOCITY;
		// If the vertical velocity is above the gravity amount, decrease it by GRAVITY_CHANGE.
		else if (dy > GRAVITY) dy -= delta * GRAVITY_CHANGE;
		
		/*
		 * Update player location.
		 */
		x += delta * dx;
		y -= delta * dy;
	}
}
