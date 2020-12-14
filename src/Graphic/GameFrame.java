/*** In The Name of Allah ***/
package Graphic;


import Components.Images;
import Components.NameColors;
import Server.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * The window on which the rendering is performed.
 * This example uses the modern BufferStrategy approach for double-buffering, 
 * actually it performs triple-buffering!
 * For more information on BufferStrategy check out:
 *    http://docs.oracle.com/javase/tutorial/extra/fullscreen/bufferstrategy.html
 *    http://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferStrategy.html
 * 
 * @author Seyed Mohammad Ghaffarian
 */
public class GameFrame extends JFrame {
	
	public static final int GAME_HEIGHT = 720;                  // 720p game resolution
	public static final int GAME_WIDTH = 16 * GAME_HEIGHT / 9;  // wide aspect ratio

	//uncomment all /*...*/ in the class for using Tank icon instead of a simple circle
	/*private BufferedImage image;*/

	private long lastRender;
	private ArrayList<Float> fpsHistory;

	private BufferStrategy bufferStrategy;
	
	public GameFrame(String title) {
		super(title);
		setResizable(false);
		setUndecorated(true);
		setSize(GAME_WIDTH, GAME_HEIGHT);
		lastRender = -1;
		fpsHistory = new ArrayList<>(100);
		GameFrame gameFrame = this;
//		addKeyListener(new KeyListener() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//
//			}
//
//			@Override
//			public void keyPressed(KeyEvent e) {
//				if(e.getKeyCode() ==  KeyEvent.VK_ESCAPE)
//				{
//					gameFrame.setVisible(false);
//					gameFrame.dispose();
//					new MainMenu();
//				}
//			}
//
//			@Override
//			public void keyReleased(KeyEvent e) {
//
//			}
//		});

	/*	try{
			image = ImageIO.read(new File("Icon.png"));
		}
		catch(IOException e){
			System.out.println(e);
		}*/
	}
	
	/**
	 * This must be called once after the JFrame is shown:
	 *    frame.setVisible(true);
	 * and before any rendering is started.
	 */
	public void initBufferStrategy() {
		// Triple-buffering
		createBufferStrategy(3);
		bufferStrategy = getBufferStrategy();
	}

	
	/**
	 * Game rendering with triple-buffering using BufferStrategy.
	 */
	public void render(Game state) {
		// Render single frame
		do {
			// The following loop ensures that the contents of the drawing buffer
			// are consistent in case the underlying surface was recreated
			do {
				// Get a new graphics context every time through the loop
				// to make sure the strategy is validated
				Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
				try {
					doRendering(graphics, state);
				} finally {
					// Dispose the graphics
					graphics.dispose();
				}
				// Repeat the rendering if the drawing buffer contents were restored
			} while (bufferStrategy.contentsRestored());

			// Display the buffer
			bufferStrategy.show();
			// Tell the system to do the drawing NOW;
			// otherwise it can take a few extra ms and will feel jerky!
			Toolkit.getDefaultToolkit().sync();

		// Repeat the rendering if the drawing buffer was lost
		} while (bufferStrategy.contentsLost());
	}
	
	/**
	 * Rendering all game elements based on the game state.
	 */
	private void doRendering(Graphics2D g2d, Game state) {
		// Draw background


		int yLine = GAME_HEIGHT/(state.getMap().length-1);
		int xLine = GAME_WIDTH/(state.getMap()[0].length-1);
		for(int i = 0;i < state.getMap().length;i++)
		{
			for(int j = 0;j < state.getMap()[0].length;j++)
			{
				if((i+j)%2==0)
				{
					g2d.setColor(Color.WHITE);
				}
				else
				{
					g2d.setColor(SystemColor.lightGray);
				}
				g2d.fillRect(j*xLine, i*yLine, (i+1)*xLine ,(j+1) * yLine);
			}
		}
		// Draw ball
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(10));
		for(Wall wall:state.getWalls())
		{
			if(wall.isBreakable())
				g2d.setColor(Color.cyan);
			else
				g2d.setColor(Color.BLACK);

			g2d.drawLine((int)wall.x1,(int)wall.y1,(int)wall.x2,(int)wall.y2);
		}

		g2d.setStroke(new BasicStroke(2));

		for(Bullet bullet : state.getBullets())
		{
			g2d.drawOval((int)bullet.getX(),(int)bullet.getY(),5,5);
		}

		for(PowerUp powerUp : state.powerUps)
		{
			try {
				g2d.drawImage(ImageIO.read(new File("src\\Sources\\crateWood.png")), powerUp.x, powerUp.y , null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		g2d.setStroke(new BasicStroke(8));
		g2d.setFont(g2d.getFont().deriveFont(18.0f));
		int i = 0;
		for(UserState userState: state.getTeam1())
		{
			if(!state.isTeamMatch())
			{
				g2d.setColor(NameColors.getColor(i));
				i++;
			}
			else
			{
				g2d.setColor(Color.GREEN);
			}
			if(userState.isDead()) continue;
			BufferedImage rotated;
			if(userState.getUser() != null)
				rotated = rotateImageByDegrees(Images.getTankBuffer(userState.getUser().getColor()) , userState.getAngle());
			else
				rotated = rotateImageByDegrees(Images.getTankBuffer(0) , userState.getAngle());
			int x = (42 - rotated.getWidth()) / 2;
			int y = (46 - rotated.getHeight()) / 2;
			if(userState.getUser() != null)
				drawCenteredString(g2d,userState.getUser().getNickname()+ " " + userState.getKills() + "/" + userState.getDeaths(),new Rectangle((int)userState.getX()+15,(int)userState.getY()-40,5,20),g2d.getFont().deriveFont(18.0f));
			else
				drawCenteredString(g2d,"Ai " + i + " " + userState.getKills() + "/" + userState.getDeaths(),new Rectangle((int)userState.getX()+15,(int)userState.getY()-40,5,20),g2d.getFont().deriveFont(18.0f));
			g2d.drawImage(rotated,(int) userState.getX()+x,(int)userState.getY()+y,null);
			if(userState.hasShield())
			{
				g2d.setColor(Color.CYAN);
				g2d.drawOval((int)userState.getX(),(int)userState.getY(),40,40);
			}
			g2d.setColor(Color.BLACK);
			g2d.drawLine((int)userState.getX()-10,(int)userState.getY()-15,(int)userState.getX()+55,(int)userState.getY()-15);
			g2d.setColor(Color.GREEN);
			g2d.drawLine((int)userState.getX()-10,(int)userState.getY()-15,(int)userState.getX()+(int)(55 * ((double)userState.getHP() / (double)state.getTankHp())),(int)userState.getY()-15);
		}

		for(UserState userState: state.getTeam2())
		{
			g2d.setColor(Color.RED);
			if(userState.isDead()) continue;
			BufferedImage rotated;
			if(userState.getUser() != null)
				rotated = rotateImageByDegrees(Images.getTankBuffer(userState.getUser().getColor()) , userState.getAngle());
			else
				rotated = rotateImageByDegrees(Images.getTankBuffer(1) , userState.getAngle());
			int x = (42 - rotated.getWidth()) / 2;
			int y = (46 - rotated.getHeight()) / 2;
			if(userState.getUser() != null)
			drawCenteredString(g2d,userState.getUser().getNickname() + " " + userState.getKills() + "/" + userState.getDeaths(),new Rectangle((int)userState.getX()+15,(int)userState.getY()-40,5,20),g2d.getFont().deriveFont(18.0f));
			else
				drawCenteredString(g2d,"Ai " + i + " " + userState.getKills() + "/" + userState.getDeaths(),new Rectangle((int)userState.getX()+15,(int)userState.getY()-40,5,20),g2d.getFont().deriveFont(18.0f));
			g2d.drawImage(rotated,(int) userState.getX()+x,(int)userState.getY()+y,null);
			if(userState.hasShield())
			{
				g2d.setColor(Color.CYAN);
				g2d.drawOval((int)userState.getX(),(int)userState.getY(),40,40);
			}
			g2d.setColor(Color.BLACK);
			g2d.drawLine((int)userState.getX()-10,(int)userState.getY()-15,(int)userState.getX()+55,(int)userState.getY()-15);
			g2d.setColor(Color.GREEN);
			g2d.drawLine((int)userState.getX()-10,(int)userState.getY()-15,(int)userState.getX()+(int)(55 * ((double)userState.getHP() / (double)state.getTankHp())),(int)userState.getY()-15);
		}

		if(!state.getWinner().equals(""))
		{
			if(state.getWinner().contains("-"))
				drawCenteredString(g2d,"Winners: " +state.getWinner(),new Rectangle(0,0,GAME_WIDTH,GAME_HEIGHT),g2d.getFont().deriveFont(50.0f));
			else drawCenteredString(g2d,"Winner: " +state.getWinner(),new Rectangle(0,0,GAME_WIDTH,GAME_HEIGHT),g2d.getFont().deriveFont(50.0f));
		}


	}

	/**
	 * Draw a String centered in the middle of a Rectangle.
	 *
	 * @param g The Graphics instance.
	 * @param text The String to draw.
	 * @param rect The Rectangle to center the text in.
	 */
	public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
		// Get the FontMetrics
		FontMetrics metrics = g.getFontMetrics(font);
		// Determine the X coordinate for the text
		int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		// Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
		int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		// Set the font
		g.setFont(font);
		// Draw the String
		g.drawString(text, x, y);
	}

	public BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {

		double rads = Math.toRadians(angle);
		double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
		int w = img.getWidth();
		int h = img.getHeight();
		int newWidth = (int) Math.floor(w * cos + h * sin);
		int newHeight = (int) Math.floor(h * cos + w * sin);

		BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = rotated.createGraphics();
		AffineTransform at = new AffineTransform();
		at.translate((newWidth - w) / 2, (newHeight - h) / 2);

		int x = w / 2;
		int y = h / 2;

		at.rotate(rads, x, y);
		g2d.setTransform(at);
		g2d.drawImage(img, 0, 0, this);
		g2d.dispose();

		return rotated;
	}
}
