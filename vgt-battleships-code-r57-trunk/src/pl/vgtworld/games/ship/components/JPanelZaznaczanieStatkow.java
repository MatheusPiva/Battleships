package pl.vgtworld.games.ship.components;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.games.ship.Board;
import pl.vgtworld.games.ship.FieldTypeBoard;
import pl.vgtworld.games.ship.DrawingCoordinatesOnBoard;
import pl.vgtworld.games.ship.ShipIterator;
import pl.vgtworld.games.ship.ShipGenerator;
import pl.vgtworld.games.ship.ShipPositioner;
import pl.vgtworld.games.ship.Settings;
import pl.vgtworld.tools.Position;

/**
 * Panel wykorzystywany do obslugi rozmieszczenia statkow na planszy przez gracza.
 * 
 * @author VGT
 * @version 1.0
 */
public class JPanelZaznaczanieStatkow
	extends JPanel
	{
	/**
	 * Obiekt ustawien rozgrywki.
	 */
	private Settings oUstawienia;
	/**
	 * Board, na ktorej gracz zaznacza statki.
	 */
	private Board oBoard;
	/**
	 * Kontener statkow tworzony dla gracza po zakonczeniu rozmieszczania statkow.
	 */
	private ShipIterator oShips;
	/**
	 * Referencja do glownego okna gry.
	 */
	private JFrameGameWindowSettings oOkno;
	/**
	 * Komponent, na ktorym wyswietlana jest plansza.
	 */
	private JComponentPlansza oComponentPlansza;
	/**
	 * Panel wyswietlajacy informacje na temat ilosci statkow poszczegolnych Sizeow,
	 * ktore nalezy umiescic na planszy.
	 */
	private JPanelZaznaczanieStatkowLista oListaStatkowInfo;
	/**
	 * Panel zawierajacy informacje o wymaganych statkach oraz przyciski akcji.
	 */
	private JPanel oPanelPrawy;
	/**
	 * Obiekt obslugi akcji klikniecia myszki na planszy.
	 */
	private ZaznaczanieStatkowMouseListener oMouseListener;
	/**
	 * Klasa prywatna zawierajaca obsluge akcji wcisniecia przycisku zatwierdzajacego rozmieszczenie statkow na planszy.
	 */
	private class ActionZatwierdzStatki
		extends AbstractAction
		{
		public ActionZatwierdzStatki()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.accept"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.accept.desc"));
			}
		@Override public void actionPerformed(ActionEvent event)
			{
			ShipGenerator oGenerator = new ShipGenerator(oBoard);
			oShips = oGenerator.generateShips();
			boolean bOK = true;
			//sprawdzenie, kolejnych warunkow rozmieszczenia statkow
			if (oShips.getNumberOfShips() != oUstawienia.getNumbeOfShips())
				bOK = false;
			for (int i = oShips.getMaxShipSize(); i >= 1; --i)
				if (oShips.getNumberOfShips(i) != oUstawienia.getNumberOfShips(i))
					bOK = false;
			if (oShips.verifyApplication(oUstawienia.getStraightLines()) == false)
				bOK = false;
			//commit
			if (bOK == false)
				{
				JOptionPane.showMessageDialog(JPanelZaznaczanieStatkow.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.shipPlacement.invalidShipPlacement"));
				oShips = null;
				}
			else
				oOkno.rozpocznijRozgrywke();
			}
		}
	/**
	 * Klasa prywatna zawierajaca obsluge akcji wcisniecia przycisku usuwajacego wszystkie statki z planszy.
	 */
	private class ActionWyczysc
		extends AbstractAction
		{
		public ActionWyczysc()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.clear"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.clear.desc"));
			}
		@Override public void actionPerformed(ActionEvent event)
			{
			try
				{
				for (int i = 0; i < oBoard.getWidth(); ++i)
					for (int j = 0; j < oBoard.getHeight(); ++j)
						if (oBoard.getField(i, j) == FieldTypeBoard.SHIP_BOARD)
							oBoard.setField(i, j, FieldTypeBoard.BOARD_FIELD_EMPTY);
				repaint();
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
			}
		}
	/**
	 * Klasa prywatna zawierajaca obsluge wcisniecia przycisku rozmieszczajacego statki gracza losowo na planszy.
	 */
	private class ActionRozmiescLosowoShipsGracza
		extends AbstractAction
		{
		public ActionRozmiescLosowoShipsGracza()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.random"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.shipPlacement.random.desc"));
			}
		@Override public void actionPerformed(ActionEvent event)
			{
			try
				{
				for (int i = 0; i < oBoard.getWidth(); ++i)
					for (int j = 0; j < oBoard.getHeight(); ++j)
						if (oBoard.getField(i, j) == FieldTypeBoard.SHIP_BOARD)
							oBoard.setField(i, j, FieldTypeBoard.BOARD_FIELD_EMPTY);
				ShipIterator oKontener = new ShipIterator(oBoard);
				int[] aShips = oUstawienia.getShips();
				for (int iSize: aShips)
					oKontener.addAShip(iSize);
				ShipPositioner oPozycjoner = new ShipPositioner();
				if (oPozycjoner.shipSpaces(oKontener, oUstawienia.getStraightLines()) == false)
					JOptionPane.showMessageDialog(JPanelZaznaczanieStatkow.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.shipPlacement.randomShipPlacementFail"));
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
			repaint();
			}
		}
	/**
	 * Klasa prywatna zawierajaca obsluge klikniecia gracza na planszy (zaznaczanie/odznaczanie pol statkow).
	 */
	private class ZaznaczanieStatkowMouseListener
		extends MouseAdapter
		{
		public ZaznaczanieStatkowMouseListener()
			{
			}
		@Override public void mousePressed(MouseEvent event)
			{
			int iPlanszaSzerokosc = oBoard.getWidth();
			int iPlanszaWysokosc = oBoard.getHeight();
			int iKomponentSzerokosc = oComponentPlansza.getWidth();
			int iKomponentWysokosc = oComponentPlansza.getHeight();
			int iClickX = event.getX();
			int iClickY = event.getY();
			Position oKliknietePole;
			oKliknietePole = DrawingCoordinatesOnBoard.pixToField(iKomponentSzerokosc, iKomponentWysokosc, iPlanszaSzerokosc, iPlanszaWysokosc, iClickX, iClickY);
			try
				{
				if (oKliknietePole.getX() >= 0 && oKliknietePole.getX() < iPlanszaSzerokosc
					&& oKliknietePole.getY() >= 0 && oKliknietePole.getY() < iPlanszaWysokosc
					)
					{
					if (oBoard.getField(oKliknietePole.getX(), oKliknietePole.getY()) == FieldTypeBoard.BOARD_FIELD_EMPTY)
						{
						oBoard.setField(oKliknietePole.getX(), oKliknietePole.getY(), FieldTypeBoard.SHIP_BOARD);
//						oComponentPlansza.aktywujWyroznienie(oKliknietePole.getX(), oKliknietePole.getY());
						}
					else if (oBoard.getField(oKliknietePole.getX(), oKliknietePole.getY()) == FieldTypeBoard.SHIP_BOARD)
						{
						oBoard.setField(oKliknietePole.getX(), oKliknietePole.getY(), FieldTypeBoard.BOARD_FIELD_EMPTY);
//						oComponentPlansza.aktywujWyroznienie(oKliknietePole.getX(), oKliknietePole.getY());
						}
					Position oWspTopLeft;
					Position oWspBottomRight;
					oWspTopLeft = DrawingCoordinatesOnBoard.fieldToPixTopLeft(iKomponentSzerokosc, iKomponentWysokosc, iPlanszaSzerokosc, iPlanszaWysokosc, oKliknietePole.getX(), oKliknietePole.getY());
					oWspBottomRight = DrawingCoordinatesOnBoard.fieldToPixBottomRight(iKomponentSzerokosc, iKomponentWysokosc, iPlanszaSzerokosc, iPlanszaWysokosc, oKliknietePole.getX(), oKliknietePole.getY());
					oComponentPlansza.repaint(oWspTopLeft.getX(), oWspTopLeft.getY(), oWspBottomRight.getX()-oWspTopLeft.getX(), oWspBottomRight.getY() - oWspTopLeft.getY());
					}
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
			}
		}
	/**
	 * Konstruktor.
	 * 
	 * @param oUstawienia Glowne ustawienia gry.
	 * @param oOkno Glowne okno gry.
	 */
	public JPanelZaznaczanieStatkow(Settings oUstawienia, JFrameGameWindowSettings oOkno)
		{
		setLayout(new GridLayout(1, 2));
		this.oUstawienia = oUstawienia;
		this.oOkno = oOkno;
		oBoard = new Board(oUstawienia.getBoardWidth(), oUstawienia.getBoardHeight());
		oShips = null;
		oComponentPlansza = new JComponentPlansza(oBoard);
		oMouseListener = new ZaznaczanieStatkowMouseListener();
		
		addMouseListener(oMouseListener);
		
		//lewa polowka
		oComponentPlansza = new JComponentPlansza(oBoard);
		add(oComponentPlansza);
		
		//prawa polowka
		oPanelPrawy = new JPanel();
		oPanelPrawy.setLayout(new BorderLayout());
		oListaStatkowInfo = new JPanelZaznaczanieStatkowLista(oUstawienia);
		JScrollPane oListaStatkowInfoScroll = new JScrollPane(oListaStatkowInfo);
		oListaStatkowInfoScroll.setBorder(null);
		
		JPanel oButtonContainer = new JPanel();
		oButtonContainer.setLayout(new GridLayout(1,3));
		JButton oButtonZatwierdz = new JButton(new ActionZatwierdzStatki());
		JButton oButtonWyczysc = new JButton(new ActionWyczysc());
		JButton oButtonLosuj = new JButton(new ActionRozmiescLosowoShipsGracza());
		oButtonContainer.add(oButtonZatwierdz);
		oButtonContainer.add(oButtonLosuj);
		oButtonContainer.add(oButtonWyczysc);
		oPanelPrawy.add(oButtonContainer, BorderLayout.PAGE_END);
		oPanelPrawy.add(oListaStatkowInfoScroll, BorderLayout.CENTER);
		add(oPanelPrawy);
		}
	/**
	 * Metoda zwraca plansze, na ktorej zaznaczane sa statki.
	 * 
	 * @return Board z zaznaczonymi statkami.
	 */
	public Board getBoard()
		{
		return oBoard;
		}
	/**
	 * Metoda zwraca obiekt kontenera statkow stworznych przez gracza.<br />
	 * 
	 * Jesli statki nie zostaly rozmieszczone, lub zostaly rozmieszczone nieprawidlowo, metoda zwroci pusta referencje.
	 * 
	 * @return Kontener statkow gracza.
	 */
	public ShipIterator getStatki()
		{
		return oShips;
		}
	/**
	 * Metoda usuwa wszystkie statki umieszczone na planszy.
	 */
	public void wyczyscPlansze()
		{
		oBoard.clean();
		}
	/**
	 * Metoda zmienia Size planszy na podstawie aktualnego stanu obiektu ustawien.<br />
	 * 
	 * Wywolywana po zmianie ustawien rozgrywki.
	 */
	public void resetujPlansze()
		{
		//oBoard = new Board(oUstawienia.getBoardWidth(), oUstawienia.getBoardHeight());
		try
			{
			if (oUstawienia.getBoardWidth() != oBoard.getWidth() || oUstawienia.getBoardHeight() != oBoard.getHeight())
				oBoard.zmienSize(oUstawienia.getBoardWidth(), oUstawienia.getBoardHeight());
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Metoda aktualizuje panel z informacjami na temat ilosci wymaganych do umieszczenia statkow na planszy. 
	 */
	public void resetujOpis()
		{
		oListaStatkowInfo.odswiez();
		}
	}
