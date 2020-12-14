/*** In The Name of Allah ***/
package Graphic;


import Client.MainMenu;
import Client.Network;
import Client.UserInfo;
import Server.Game;

/**
 * A very simple structure for the main game loop.
 * THIS IS NOT PERFECT, but works for most situations.
 * Note that to make this work, none of the 2 methods 
 * in the while loop (update() and render()) should be 
 * long running! Both must execute very quickly, without 
 * any waiting and blocking!
 * 
 * Detailed discussion on different game loop design
 * patterns is available in the following link:
 *    http://gameprogrammingpatterns.com/game-loop.html
 * 
 * @author Seyed Mohammad Ghaffarian
 */
public class GameLoop implements Runnable {
	
	/**
	 * Frame Per Second.
	 * Higher is better, but any value above 24 is fine.
	 */
	public static final int FPS = 60;
	
	private GameFrame canvas;
	private KeyboardState state;

	public GameLoop(GameFrame frame) {
		canvas = frame;
	}
	
	/**
	 * This must be called before the game loop starts.
	 */
	public void init() {
		state = new KeyboardState();
		canvas.addKeyListener(state.getKeyListener());
	}

	@Override
	public void run() {
		Network.started();
		while (true) {
			try {
				//
				Game gameState = Network.updateGame(state);
				if(gameState == null)continue;
				if (!gameState.isJoined(UserInfo.user)) {
					Network.leaveRoom();
					new MainMenu();
					canvas.setVisible(false);
					canvas.dispose();
					return;
				}
				canvas.render(gameState);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
