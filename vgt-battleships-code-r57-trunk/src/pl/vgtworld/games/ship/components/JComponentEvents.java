package pl.vgtworld.games.ship.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Component that displays information about Hitsch and ship sinks of individual players.
 * 
 * @author VGT
 * @version 1.0
 */
public class JComponentEvents
	extends JComponent
	{
	/**
	 * The minimum and preferred Width of the component.
	 */
	private static final int Width = 40;
	/**
	 * The minimum and preferred Height of the component.
	 */
	private static final int Height = 40;
	/**
	 * Font size of displayed messages.
	 */
	private static final int FONT_Size = 30;
	/**
	 * The background color of the component.
	 */
	private Color oColorBackground;
	/**
	 * The color of the component's message font.
	 */
	private Color oColorFont;
	/**
	 * Message font object.
	 */
	private Font oFont;
	/**
	 * The left player's messaging object.
	 */
	private JLabel oLeftPlayer;
	/**
	 * The right player's messaging object.
	 */
	private JLabel oRightPlayer;
	/**
	 * Timer hiding the message of the left player.
	 */
	private Timer oTimerLeft;
	/**
	 * Timer hiding the message of the right player.
	 */
	private Timer oTimerRight;
	/**
	 * Component graphic background.
	 */
	private Image oBackgroundImg;
	/**
	 * Private class that handles the action of hiding the message for the left player.
	 */
	private class ActionClearLeft
		extends AbstractAction
		{
		public void actionPerformed(ActionEvent oEvent)
			{
			oLeftPlayer.setText("");
			oLeftPlayer.repaint();
			}
		}
	/**
	 * Private class that supports the actions of hiding the message for the right player.
	 */
	private class ActionClearRight
		extends AbstractAction
		{
		public void actionPerformed(ActionEvent oEvent)
			{
			oRightPlayer.setText("");
			oRightPlayer.repaint();
			}
		}
	/**
	 * Default constructor.
	 */
	public JComponentEvents()
		{
		//background img
		URL oImgUrl = getClass().getResource("/pl/vgtworld/games/ship/img/events-bg.png");
		if (oImgUrl != null)
			{
			try
				{
				oBackgroundImg = ImageIO.read(oImgUrl);
				}
			catch (IOException e)
				{
				oBackgroundImg = null;
				}
			}
		else
			oBackgroundImg = null;
		oColorBackground = new Color(255, 255, 255);
		oColorFont = new Color(255, 255, 255);
		oFont = new Font("Serif", Font.BOLD, FONT_Size);
		oLeftPlayer = new JLabel("", JLabel.CENTER);
		oLeftPlayer.setFont(oFont);
		oLeftPlayer.setVerticalAlignment(JLabel.CENTER);
		oLeftPlayer.setForeground(oColorFont);
		JPanel oLeftPlayerContainer = new JPanel();
		oLeftPlayerContainer.setOpaque(false);
		oLeftPlayerContainer.add(oLeftPlayer);
		oRightPlayer = new JLabel("", JLabel.CENTER);
		oRightPlayer.setVerticalAlignment(JLabel.CENTER);
		oRightPlayer.setFont(oFont);
		oRightPlayer.setForeground(oColorFont);
		JPanel oRightPlayerContainer = new JPanel();
		oRightPlayerContainer.setOpaque(false);
		oRightPlayerContainer.add(oRightPlayer);
		oTimerLeft = new Timer(1000, new ActionClearLeft());
		oTimerLeft.setRepeats(false);
		oTimerRight = new Timer(1000, new ActionClearRight());
		oTimerRight.setRepeats(false);
		
		setMinimumSize(new Dimension(Width, Height));
		setPreferredSize(new Dimension(Width, Height));
		setLayout(new GridLayout());
		
		add(oLeftPlayerContainer);
		add(oRightPlayerContainer);
		}
	/**
	 * The method displays a message to the left player and triggers a timer to hide the message after one second.
	 * 
	 * @param sText The content of the displayed message.
	 */
	public void setLeftMessage(String sText)
		{
		oLeftPlayer.setText(sText);
		oTimerLeft.start();
		}
	/**
	 * The method displays a message to the right player and triggers a timer to hide the message after one second.
	 * 
	 * @param sText The content of the displayed message.
	 */
	public void setRightMessage(String sText)
		{
		oRightPlayer.setText(sText);
		oTimerRight.start();
		}
	/**
	 * Overloaded drawac method component.
	 */
	@Override public void paintComponent(Graphics g)
		{
		g.setColor(oColorBackground);
		g.fillRect(0, 0, getWidth(), getHeight());
		if (oBackgroundImg != null)
			{
			int iStartX = 0;
			while (iStartX < getWidth())
				{
				g.drawImage(oBackgroundImg, iStartX, 0, null);
				iStartX+= oBackgroundImg.getWidth(null);
				}
			}
		}
	}
