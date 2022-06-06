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
 * Komponent wyswietlajacy informacje o Hitsch i zatopieniach statkow poszczegolnych graczy.
 * 
 * @author VGT
 * @version 1.0
 */
public class JComponentEvents
	extends JComponent
	{
	/**
	 * Minimalna i preferowana Width komponentu.
	 */
	private static final int Width = 40;
	/**
	 * Minimalna i preferowana Height komponentu.
	 */
	private static final int Height = 40;
	/**
	 * Size czcionki wyswietlanych komunikatow.
	 */
	private static final int FONT_Size = 30;
	/**
	 * Kolor tla komponentu.
	 */
	private Color oColorBackground;
	/**
	 * Kolor czcionki komunikatow komponentu.
	 */
	private Color oColorFont;
	/**
	 * Obiekt czcionki komunikatow.
	 */
	private Font oFont;
	/**
	 * Obiekt komunikatow gracza lewego.
	 */
	private JLabel oLeftPlayer;
	/**
	 * Obiekt komunikatow gracza prawego.
	 */
	private JLabel oRightPlayer;
	/**
	 * Timer ukrywajacy komunikat gracza lewego.
	 */
	private Timer oTimerLeft;
	/**
	 * Timer ukrywajacy komunikat gracza prawego.
	 */
	private Timer oTimerRight;
	/**
	 * Tlo graficzne komponentu.
	 */
	private Image oBackgroundImg;
	/**
	 * Klasa prywatna obslugujaca akcje ukrywania komunikatu dla lewego gracza.
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
	 * Klasa prywatna obslugujaca akcje ukrywania komunikatu dla prawego gracza.
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
	 * Konstruktor domyslny.
	 */
	public JComponentEvents()
		{
		//tlo img
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
	 * Metoda wyswietla komunikat dla lewego gracza i wywoluje timer ukrywajacy komunikat po jednej sekundzie.
	 * 
	 * @param sText Tresc wyswietlanego komunikatu.
	 */
	public void setLeftMessage(String sText)
		{
		oLeftPlayer.setText(sText);
		oTimerLeft.start();
		}
	/**
	 * Metoda wyswietla komunikat dla prawego gracza i wywoluje timer ukrywajacy komunikat po jednej sekundzie.
	 * 
	 * @param sText Tresc wyswietlanego komunikatu.
	 */
	public void setRightMessage(String sText)
		{
		oRightPlayer.setText(sText);
		oTimerRight.start();
		}
	/**
	 * Przeciazona metoda drawaca komponent.
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
