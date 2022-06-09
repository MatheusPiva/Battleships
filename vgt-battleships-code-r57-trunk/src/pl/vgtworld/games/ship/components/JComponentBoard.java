package pl.vgtworld.games.ship.components;

import static pl.vgtworld.games.ship.DrawingCoordinatesOnBoard.*;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.games.ship.Board;
import pl.vgtworld.games.ship.FieldTypeBoard;
import pl.vgtworld.tools.Position;

/**
 * Komponent obslugujacy wyswietlanie board.
 * 
 * @author VGT
 * @version 1.0
 */
public class JComponentBoard
	extends JComponent
	implements ActionListener
	{
	/**
	 * Board, ktora ma wyswietlic panel.
	 */
	private Board oBoard;
	/**
	 * Wlasciwosc przechowujaca informacje, czy na board maja byc takze wyswietlane nietrafione position statkow.
	 */
	private boolean bViewShips;
	/**
	 * Kolor position statku.
	 */
	private Color oShipColor;
	/**
	 * Kolor position po shote niecelnym.
	 */
	private Color oCustomShotColor;
	/**
	 * The color of the field after the shots on target.
	 */
	private Color oOutOfTargetColor;
	/**
	 * Kolor linii siatki rozdzielajacej position.
	 */
	private Color oGridColor;
	/**
	 * Kolor tla (uzywany w przyadku niepowodzenia zaladowania tla graficznego).
	 */
	private Color oBackgroundColor;
	/**
	 * Tablica przechowujaca kolejne kolory animacji shotu na pole ze statkiem.
	 */
	private Color[] aHighlightColorOfShips;
	/**
	 * Tablica przechowujaca kolejne kolory animacji shotu na pole puste.
	 */
	private Color[] aHighlightColorEmptyField;
	/**
	 * Index aktualnie wyswietlanego koloru wyroznienia.
	 */
	private int iHighlightColorNumber;
	/**
	 * co-ordinates position wyswietlanego, jako wyroznione.
	 */
	private Position oHighlightedField;
	/**
	 * Obrazek tla board.
	 */
	private static Image oBackgroundImg;
	/**
	 * Timer do obslugi animacji wyroznienia position.
	 */
	private Timer oTimer;
	static
		{
		JComponentBoard.oBackgroundImg = null;
		}
	/**
	 * Konstruktor.
	 * 
	 * @param oBoard Board, ktora ma byc wyswietlona na panel.
	 */
	public JComponentBoard(Board oBoard)
		{
		this.oBoard = oBoard;
		bViewShips = true;
		oShipColor = new Color(0, 0, 255, 127);
		oCustomShotColor = new Color(0, 0, 0);
		oOutOfTargetColor = new Color(150, 150, 50, 192);
		oGridColor = new Color(0, 0, 0, 96);
		aHighlightColorOfShips = new Color[100];
		aHighlightColorEmptyField = new Color[100];
		for (int i = 0; i < 100; ++i)
			{
			aHighlightColorOfShips[i] = new Color(255, 0, 0, (int)(((double)i / 100) * 255));
			aHighlightColorEmptyField[i] = new Color(0, 255, 0, (int)(((double)i / 100) * 255));
			}
		iHighlightColorNumber = 0;
		oHighlightedField = new Position(2);
		oHighlightedField.setX(-1);
		oHighlightedField.setY(-1);
		if (JComponentBoard.oBackgroundImg == null)
			{
			URL oImgUrl = getClass().getResource("/pl/vgtworld/games/ship/img/map-bg.jpg");
			
			if (oImgUrl != null)
				{
				try
					{
					JComponentBoard.oBackgroundImg = ImageIO.read(oImgUrl);
					}
				catch (IOException e)
					{
					JComponentBoard.oBackgroundImg = null;
					}
				}
			if (oImgUrl == null || JComponentBoard.oBackgroundImg == null)
				{
				JComponentBoard.oBackgroundImg = null;
				oOutOfTargetColor = new Color(255, 255, 255, 127);
				oBackgroundColor = new Color(190, 160, 110);
				}
			}
		oTimer = new Timer(1000, this);
		oTimer.setRepeats(false);
		//setMinimumSize(new Dimension(iWidth, iHeight));
		//setPreferredSize(new Dimension(iWidth, iHeight));
		}
	/**
	 * Metoda umozliwia okreslenie, czy panel ma wyswietlac takze nietrafione position statkow.
	 * 
	 * @param bStan Jesli TRUE, panel wyswietli nietrafione position statkow.
	 */
	public void setViewShips(boolean bStan)
		{
		bViewShips = bStan;
		}
	public void activateHighlight(Position oPosition)
		{
		activateHighlight(oPosition.getX(), oPosition.getY());
		}
	public void activateHighlight(int iX, int iY)
		{
		if (oTimer.isRunning())
			oTimer.stop();
		oHighlightedField.setX(iX);
		oHighlightedField.setY(iY);
		iHighlightColorNumber = 99;
		oTimer.start();
		repaint();
		}
	public void actionPerformed(ActionEvent oEvent)
		{
		iHighlightColorNumber-= 100;
		if (iHighlightColorNumber < 0)
			{
			iHighlightColorNumber = 0;
			//oTimer.stop();
			}
		repaint();
		}
	/**
	 * Przeciazona metoda drawaca zawartosc panel.
	 */
	@Override public void paintComponent(Graphics g)
		{
		super.paintComponent(g);
		int iWidth = getWidth();
		int iHeight = getHeight();
		//background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, iWidth - 1, iHeight - 1);
		int iBoardXStart = fieldToPixTopLeft(iWidth, iHeight, oBoard.getWidth(), oBoard.getHeight(), 0, 0).getX();
		int iBoardYStart = fieldToPixTopLeft(iWidth, iHeight, oBoard.getWidth(), oBoard.getHeight(), 0, 0).getY();
		int iBoardXWidth = fieldToPixTopLeft(iWidth, iHeight, oBoard.getWidth(), oBoard.getHeight(), oBoard.getWidth(), oBoard.getHeight()).getX() - iBoardXStart + 1;
		int iBoardYWidth = fieldToPixTopLeft(iWidth, iHeight, oBoard.getWidth(), oBoard.getHeight(), oBoard.getWidth(), oBoard.getHeight()).getY() - iBoardYStart + 1;
		if (JComponentBoard.oBackgroundImg != null)
			{
			g.drawImage(JComponentBoard.oBackgroundImg, 0, 0, iWidth, iHeight, null);
			}
		else
			{
			g.setColor(oBackgroundColor);
			g.fillRect(iBoardXStart, iBoardYStart, iBoardXWidth, iBoardYWidth);
			}
		//przygotowanie zmiennych
		Position oCross;
		Position oCross2;
		int iPositionX = 0;
		int iPositionY = 0;
		int iPositionWidth = 0;
		int iPositionHeight = 0;
		int iCrossSize = 0;
		boolean bdraw = false;
		for (int i = 0; i < oBoard.getWidth(); ++i)
			for (int j = 0; j < oBoard.getHeight(); ++j)
				{
				//obliczenie niezbednych danych
				oCross = fieldToPixTopLeft(iWidth, iHeight, oBoard.getWidth(), oBoard.getHeight(), i, j);
				oCross2 = fieldToPixBottomRight(iWidth, iHeight, oBoard.getWidth(), oBoard.getHeight(), i, j);
				iPositionX = oCross.getX() + 1;
				iPositionY = oCross.getY() + 1;
				iPositionWidth = oCross2.getX() - iPositionX;
				iPositionHeight = oCross2.getY() - iPositionY;
				//skrzyzowania pomiedzy polami
				if (iCrossSize == 0)
					{
					iCrossSize = (int)((iPositionWidth + iPositionHeight) * 0.03);
					if (iCrossSize < 2)
						iCrossSize = 2;
					}
				g.setColor(oGridColor);
				g.drawLine(oCross.getX() - iCrossSize, oCross.getY(), oCross.getX() + iCrossSize, oCross.getY());
				g.drawLine(oCross.getX(), oCross.getY() - iCrossSize, oCross.getX(), oCross.getY() + iCrossSize);
				if (i + 1 == oBoard.getWidth())
					{
					g.drawLine(oCross2.getX() - iCrossSize, oCross.getY(), oCross2.getX() + iCrossSize, oCross.getY());
					g.drawLine(oCross2.getX(), oCross.getY() - iCrossSize, oCross2.getX(), oCross.getY() + iCrossSize);
					}
				if (j + 1 == oBoard.getHeight())
					{
					g.drawLine(oCross.getX() - iCrossSize, oCross2.getY(), oCross.getX() + iCrossSize, oCross2.getY());
					g.drawLine(oCross.getX(), oCross2.getY() - iCrossSize, oCross.getX(), oCross2.getY() + iCrossSize);
					}
				if (i + 1 == oBoard.getWidth() && j + 1 == oBoard.getHeight())
					{
					g.drawLine(oCross2.getX() - iCrossSize, oCross2.getY(), oCross2.getX() + iCrossSize, oCross2.getY());
					g.drawLine(oCross2.getX(), oCross2.getY() - iCrossSize, oCross2.getX(), oCross2.getY() + iCrossSize);
					}
				//zawartosc position
				try
					{
					FieldTypeBoard eTyp = oBoard.getField(i, j);
					bdraw = false;
					switch (eTyp)
						{
						case SHIP_BOARD:
							g.setColor(oShipColor);
							if (bViewShips == true)
								bdraw = true;
							break;
						case CUSTOMS_SHOT_BOARD:
							g.setColor(oCustomShotColor);
							bdraw = true;
							break;
						case BOARD_FIELD_UNAVAILABLE:
						case BOARD_SHOT_FALSE:
							g.setColor(oOutOfTargetColor);
							bdraw = true;
							break;
						}
					if (bdraw == true)
						{
						g.fillRect(iPositionX, iPositionY, iPositionWidth, iPositionHeight);
						}
					//wyroznienie position
					if (iHighlightColorNumber > 0 && oHighlightedField.getX() == i && oHighlightedField.getY() == j)
						{
						if (oBoard.getField(i, j) == FieldTypeBoard.SHIP_BOARD || oBoard.getField(i, j) == FieldTypeBoard.CUSTOMS_SHOT_BOARD)
							g.setColor(aHighlightColorOfShips[iHighlightColorNumber]);
						else
							g.setColor(aHighlightColorEmptyField[iHighlightColorNumber]);
						g.fillRect(iPositionX, iPositionY, iPositionWidth, iPositionHeight);
						}
					}
				catch (ParameterException e)
					{
					throw new DeveloperException(e);
					}
				}
		}
	}
