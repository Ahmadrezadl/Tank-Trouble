package Graphic;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;

/**
 * This class holds the state of game and all of its elements.
 * This class also handles user inputs, which affect the game state.
 * 
 * @author Seyed Mohammad Ghaffarian
 */
public class KeyboardState implements Serializable {

	public boolean gameOver;

	private boolean keyUP, keyDOWN, keyRIGHT, keyLEFT , keySPACE , keyESC;
	private KeyHandler keyHandler;
	
	public KeyboardState() {

		gameOver = false;
		//
		keyUP = false;
		keyDOWN = false;
		keyRIGHT = false;
		keyLEFT = false;
		keySPACE = false;
		keyESC = false;
		//
		keyHandler = new KeyHandler();
	}
	
	/**
	 * The method which updates the game state.
	 */
	
	public KeyListener getKeyListener() {
		return keyHandler;
	}


	public boolean isKeyUP() {
		return keyUP;
	}

	public boolean isKeyDOWN() {
		return keyDOWN;
	}

	public boolean isKeyRIGHT() {
		return keyRIGHT;
	}

	public boolean isKeyLEFT() {
		return keyLEFT;
	}
	public boolean isKeySPACE() {
		return keySPACE;
	}

	public boolean isKeyESC() {
		return keyESC;
	}

	public void random() {
		keyUP = true;
		if(System.currentTimeMillis()%2 == 0) keyLEFT = true;
		else keyRIGHT = true;
	}

    public void generateRandom() {
		double rand = Math.random();
		if(rand > 0.95)
		{
			if(keyRIGHT)
			{
				keyLEFT = true;
				keyRIGHT = false;
			}
			else
			{
				keyLEFT = false;
				keyRIGHT = true;
			}
		}
    }

    /**
	 * The keyboard handler.
	 */
	class KeyHandler extends KeyAdapter implements Serializable {


		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode())
			{
				case KeyEvent.VK_UP:
					keyUP = true;
					break;
				case KeyEvent.VK_DOWN:
					keyDOWN = true;
					break;
				case KeyEvent.VK_LEFT:
					keyLEFT = true;
					break;
				case KeyEvent.VK_RIGHT:
					keyRIGHT = true;
					break;
				case KeyEvent.VK_ESCAPE:
					gameOver = true;
					break;
				case KeyEvent.VK_SPACE:
					keySPACE = true;
					break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode())
			{
				case KeyEvent.VK_UP:
					keyUP = false;
					break;
				case KeyEvent.VK_DOWN:
					keyDOWN = false;
					break;
				case KeyEvent.VK_LEFT:
					keyLEFT = false;
					break;
				case KeyEvent.VK_RIGHT:
					keyRIGHT = false;
					break;
				case KeyEvent.VK_SPACE:
					keySPACE = false;
					break;
			}
		}

	}

	/**
	 * The mouse handler.
	 */
}

