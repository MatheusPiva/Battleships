package pl.vgtworld.games.ship.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import pl.vgtworld.components.about.JDialogAbout;
import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.games.ship.Board;
import pl.vgtworld.games.ship.FieldTypeBoard;
import pl.vgtworld.games.ship.DrawingCoordinatesOnBoard;
import pl.vgtworld.games.ship.ShipIterator;
import pl.vgtworld.games.ship.ShipPositioner;
import pl.vgtworld.games.ship.GameStatus;
import pl.vgtworld.games.ship.Settings;
import pl.vgtworld.games.ship.ai.Ai;
import pl.vgtworld.games.ship.ai.AiFactory;
import pl.vgtworld.tools.Position;
//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;

/**
 * Glowne okno gry.
 * 
 * @author VGT
 * @version 1.0
 */
public class JFrameGameWindowSettings
	extends JFrame
	{
	/**
	 * Zmienna przechowujaca wersje programu odczytana z pliku.
	 */
	public static String sWersja;
	/**
	 * Stala przechowujaca minimalna szerokosc okna glownego gry.
	 */
	public static final int MIN_SZEROKOSC = 640;
	/**
	 * Stala przechowujaca minimalna wysokosc okna glownego gry.
	 */
	public static final int MIN_WYSOKOSC = 480;
	/**
	 * Plik jezykowy.
	 */
	public static Properties LANG;
	/**
	 * Obiekt statusu gry.
	 */
	private GameStatus oStatusGry;
	/**
	 * Obiekt ustawien rozgrywki.
	 */
	private Settings oUstawienia;
	/**
	 * Okno ustawien rozgrywki.
	 */
	private JDialogUstawienia oOknoUstawienia;
	/**
	 * Okno informacji o autorze.
	 */
	private JDialogAbout oOknoAbout;
	/**
	 * Menu glownego okna gry.
	 */
	private JMenuBar oMenu;
	/**
	 * Panel przechowujacy plansze wyswietlany w trakcie gry.
	 */
	private JPanel oPanelPlanszeKontener;
	/**
	 * Panel wyswietlany po rozpoczeciu nowej gry. Realizuje obsluge rozmieszczenia statkow przez gracza.
	 */
	private JPanelZaznaczanieStatkow oPanelZaznaczanieStatkow;
	/**
	 * Panel startowy wyswietlany po uruchomieniu programu zawierajacy przyciski do uruchomienia gry,
	 * zmiany ustawien i zakonczenia rozgrywki.
	 */
	private JPanel oPanelPrzyciski;
	/**
	 * Komponent wyswietlany w gornej czesci okna prezentujacy komunikaty na temat wydarzen na planszy poszczegolnych graczy.
	 */
	private JComponentWydarzenia oComponentWydarzenia;
	/**
	 * Komponent wyswietlany w dolnej czesci okna prezentujacy informacje na temat stanu aktualnej rozgrywki.
	 */
	private JComponentStatusGry oComponentStatusGry;
	/**
	 * Kontener statkow gracza.
	 */
	private ShipIterator oShipsGracz;
	/**
	 * Kontener statkow komputera.
	 */
	private ShipIterator oShipsKomputer;
	/**
	 * Sztuczna inteligencja komputera.
	 */
	private Ai oAi;
	/**
	 * Zmienna okreslajaca, czy jest kolej gracza na oddanie shotu.<br />
	 * 
	 * Wykorzystywana w celu zablokowania oddania shotu, gdy jest kolej komputera, lub gra zakonczyla sie
	 * zwyciestwem ktoregos gracza.
	 */
	private boolean bKolejGracza;
	/**
	 * Klasa prywatna zawierajaca obsluge akcji rozpoczecia nowej rozgrywki.
	 */
	private class ActionNowaGra
		extends AbstractAction
		{
		public ActionNowaGra()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.newGame"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.newGame.desc"));
			}
		public void actionPerformed(ActionEvent event)
			{
			oStatusGry.startNewGame();
			oPanelPrzyciski.setVisible(false);
			oPanelPlanszeKontener.setVisible(false);
			oPanelZaznaczanieStatkow.setVisible(true);
			add(oPanelZaznaczanieStatkow, BorderLayout.CENTER);
			oPanelZaznaczanieStatkow.wyczyscPlansze();
			repaint();
			}
		}
	/**
	 * Klasa prywatna zawierajaca obsluge akcji wywolania okna ustawien rozgrywki.
	 */
	private class ActionUstawienia
		extends AbstractAction
		{
		public ActionUstawienia()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.settings"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.settings.desc"));
			}
		public void actionPerformed(ActionEvent oEvent)
			{
			oOknoUstawienia.reset();
			oOknoUstawienia.setVisible(true);
			}
		}
	/**
	 * Klasa prywatna zawierajaca obsluge akcji zakonczenia programu.
	 */
	private class ActionZakoncz
		extends AbstractAction
		{
		public ActionZakoncz()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.exit"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.exit.desc"));
			}
		public void actionPerformed(ActionEvent event)
			{
			System.exit(0);
			}
		}
	/**
	 * Klasa prywatna zawierajaca obsluge akcji wyswietlenia okna informacji o autorze.
	 */
	private class ActionAbout
		extends AbstractAction
		{
		public ActionAbout()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.about"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.about.desc"));
			}
		public void actionPerformed(ActionEvent oEvent)
			{
			oOknoAbout.centerPosition();
			oOknoAbout.setVisible(true);
			}
		}
	/**
	 * Klasa prywatna obslugujaca przebieg pojedynczego cyklu rozgrywki.<br />
	 * 
	 * - oddanie shotu przez gracza poprzez klikniecie na plansze komputera realizowane przez metode mousePressed().<br />
	 * - oddanie shotu przez komputer na plansze gracza wywolywane z metody actionPerformed() za pomoca timera.
	 */
	private class RozgrywkaMouseListener
		extends MouseAdapter
		implements ActionListener
		{
		private Board oBoard;
		private JComponentPlansza oCompPlansza;
		private Timer oTimer;
		public RozgrywkaMouseListener(Board oBoard, JComponentPlansza oCompPlansza)
			{
			this.oBoard = oBoard;
			this.oCompPlansza = oCompPlansza;
			oTimer = new Timer(1000, this);
			oTimer.setRepeats(false);
			}
		public void setComponent(JComponentPlansza oCompPlansza)
			{
			this.oCompPlansza = oCompPlansza;
			}
		public boolean isSetComponent()
			{
			if (oCompPlansza == null)
				return false;
			else
				return true;
			}
		@Override public void mousePressed(MouseEvent event)
			{
			int iPlanszaSzerokosc = oBoard.getWidth();
			int iPlanszaWysokosc = oBoard.getHeight();
			int iKomponentSzerokosc = oCompPlansza.getWidth();
			int iKomponentWysokosc = oCompPlansza.getHeight();
			int iClickX = event.getX();
			int iClickY = event.getY();
			Position oKliknietePole;
			oKliknietePole = DrawingCoordinatesOnBoard.pixToField(iKomponentSzerokosc, iKomponentWysokosc, iPlanszaSzerokosc, iPlanszaWysokosc, iClickX, iClickY);
			try
				{
				if (bKolejGracza == true
					&& oKliknietePole.getX() >= 0 && oKliknietePole.getX() < iPlanszaSzerokosc
					&& oKliknietePole.getY() >= 0 && oKliknietePole.getY() < iPlanszaWysokosc
					&& (oBoard.getField(oKliknietePole.getX(), oKliknietePole.getY()) == FieldTypeBoard.BOARD_FIELD_EMPTY
						|| oBoard.getField(oKliknietePole.getX(), oKliknietePole.getY()) == FieldTypeBoard.SHIP_BOARD
						)
					)
					{
					bKolejGracza = false;
					int iQuantityZatopionychPrzedshotem = oShipsKomputer.getNumberOfUndamagedShips();
					
					//shot na plansze komputera
					boolean bHit;
					bHit = oShipsKomputer.shot(oKliknietePole.getX(), oKliknietePole.getY());
					JComponentPlansza oComponentPlansza = (JComponentPlansza)oPanelPlanszeKontener.getComponent(1);
					oComponentPlansza.aktywujWyroznienie(oKliknietePole);
					//obsluga sprawdzania, czy koniec gry
					if (bHit == true && oShipsKomputer.getNumberOfShips() == oShipsKomputer.getNumberOfUndamagedShips())
						{
						oStatusGry.playerVictory();
						oComponentStatusGry.aktualizujDane();
						JOptionPane.showMessageDialog(JFrameGameWindowSettings.this, LANG.getProperty("message.win"));
						return;
						}
					else if (bHit == true)
						{
                                                    
                                                //COLOCAR AUDIO DE EXPLOSÃO
                                                
                                                InputStream in;
                                                
                                                    try {
                                                        
                                                        in = new FileInputStream(new File ("src\\pl\\vgtworld\\games\\statki\\components\\explosao.mp3"));
//                                                        AudioStream ad = new AudioStream(in);
//                                                        AudioPlayer.player.start(ad);
                                                  
                                                    } catch (Exception e) {
                                                    }
                                                    
						oComponentStatusGry.aktualizujDane();
						if (iQuantityZatopionychPrzedshotem != oShipsKomputer.getNumberOfUndamagedShips())
							oComponentWydarzenia.ustawPrawyKomunikat(LANG.getProperty("message.hit2"));
						else
							oComponentWydarzenia.ustawPrawyKomunikat(LANG.getProperty("message.hit1"));
						}
					
					oTimer.start();
					}
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
			}
		public void actionPerformed(ActionEvent oEvent)
			{
			int iQuantityZatopionychPrzedshotem = oShipsGracz.getNumberOfUndamagedShips();
			//shot na plansze gracza
			boolean bHit = oAi.shot(oShipsGracz);
			JComponentPlansza oComponentPlansza = (JComponentPlansza)oPanelPlanszeKontener.getComponent(0);
			oComponentPlansza.aktywujWyroznienie(oShipsGracz.getLastShot());
			//obsluga sprawdzania, czy koniec gry
			if (bHit == true && oShipsGracz.getNumberOfShips() == oShipsGracz.getNumberOfUndamagedShips())
				{
				oStatusGry.computerVictory();
				oComponentStatusGry.aktualizujDane();
				int iQuantityKomponentow = oPanelPlanszeKontener.getComponentCount();
				JComponentPlansza oCompPlansza;
				for (int i = 0; i < iQuantityKomponentow; ++i)
					{
					oCompPlansza = (JComponentPlansza)oPanelPlanszeKontener.getComponent(i);
					if (oCompPlansza != null)
						oCompPlansza.setWyswietlStatki(true);
					}
				oPanelPlanszeKontener.repaint();
				JOptionPane.showMessageDialog(JFrameGameWindowSettings.this, LANG.getProperty("message.lose"));
				return;
				}
			else if (bHit == true)
				{
				oComponentStatusGry.aktualizujDane();
				if (iQuantityZatopionychPrzedshotem != oShipsGracz.getNumberOfUndamagedShips())
					oComponentWydarzenia.ustawLewyKomunikat(LANG.getProperty("message.hit2"));
				else
					oComponentWydarzenia.ustawLewyKomunikat(LANG.getProperty("message.hit1"));
				}
			
			bKolejGracza = true;
			
			}
		}
	/**
	 * Konstruktor.
	 * 
	 * @param oStatusGry Obiekt przechowujacy informacje na temat aktualnego statusu gry.
	 * @param oUstawienia Obiekt przechowujacy ustawienia dotyczace rozgrywki.
	 */
	public JFrameGameWindowSettings(GameStatus oStatusGry, Settings oUstawienia)
		{
		this(oStatusGry, oUstawienia, MIN_SZEROKOSC, MIN_WYSOKOSC);
		}
	/**
	 * konstruktor przeciazaony pozwalajacy zdefiniowac Size okna gry.
	 * 
	 * @param oStatusGry Obiekt przechowujacy informacje na temat aktualnego statusu gry.
	 * @param oUstawienia Obiekt przechowujacy ustawienia dotyczace rozgrywki.
	 * @param iSzerokosc Szerokosc okno gry w pixelach.
	 * @param iWysokosc Wysokosc okna gry w pixelach.
	 */
	public JFrameGameWindowSettings(GameStatus oStatusGry, Settings oUstawienia, int iSzerokosc, int iWysokosc)
		{
		InputStream oPlik = getClass().getResourceAsStream("/wersja.txt");
		if (oPlik != null)
			{
			Scanner oVer = new Scanner(oPlik);
			sWersja = oVer.nextLine();
			}
		else
			sWersja = null;
		
		Properties oDefaultLang = new Properties();
		try
			{
			InputStream oDefaultLangStream = getClass().getResourceAsStream("/pl/vgtworld/games/ship/lang/en_US.lang");
			if (oDefaultLangStream == null)
				{
				System.err.println("default language file not found");
				JOptionPane.showMessageDialog(null, "default language file not found", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
				}
			oDefaultLang.load(oDefaultLangStream);
			InputStream oLangStream = getClass().getResourceAsStream("/pl/vgtworld/games/ship/lang/" + Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry() + ".lang");
			LANG = new Properties(oDefaultLang);
			if (oLangStream != null)
				{
				BufferedReader oBuffereLangStream = new BufferedReader(new InputStreamReader(oLangStream, "UTF-8"));
				LANG.load(oBuffereLangStream);
				}
			}
		catch (IOException e)
			{
			System.err.println("error reading file");
			JOptionPane.showMessageDialog(null, "error reading file", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
			}

		//setMinimumSize(new Dimension(MIN_SZEROKOSC, MIN_WYSOKOSC));
		
		setTitle(JFrameGameWindowSettings.LANG.getProperty("mainWindow.title"));
		//setSize(iSzerokosc, iWysokosc);
                setExtendedState(JFrameGameWindowSettings.MAXIMIZED_BOTH);
                setUndecorated(true);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		this.oStatusGry = oStatusGry;
		this.oUstawienia = oUstawienia;
		oOknoUstawienia = new JDialogUstawienia(this, oUstawienia);
		oOknoUstawienia.setLocationRelativeTo(this);
		oOknoAbout = new JDialogAbout(this, JFrameGameWindowSettings.LANG.getProperty("mainWindow.title"));
		if (sWersja != null)
			{
			oOknoAbout.setVersion(sWersja);
			oOknoAbout.rebuild();
			}
		
		//panel z planszami graczy
		oPanelPlanszeKontener = new JPanel();
		oPanelPlanszeKontener.setLayout(new GridLayout());
		if (oStatusGry.getGameLaunched() == true && oStatusGry.getShipsArranged() == true)
			add(oPanelPlanszeKontener, BorderLayout.CENTER);
		
		//panel z plansza do zaznaczania statkow po rozpoczeciu gry
		oPanelZaznaczanieStatkow = new JPanelZaznaczanieStatkow(oUstawienia, this);
		if (oStatusGry.getGameLaunched() == true && oStatusGry.getShipsArranged() == false)
			add(oPanelZaznaczanieStatkow, BorderLayout.CENTER);
		
		//obiekty akcji
		ActionNowaGra oActionNowaGra = new ActionNowaGra();
		ActionZakoncz oActionZakoncz = new ActionZakoncz();
		ActionUstawienia oActionUstawienia = new ActionUstawienia();
		
		//panel zastepujacy plansze przed rozpoczeciem rozgrywki
		oPanelPrzyciski = new JPanel();
		oPanelPrzyciski.setBackground(Color.BLACK);
		//oPanelPrzyciski.setLayout(new GridLayout());
		JButton oButtonNowaGra = new JButton(oActionNowaGra);
		JButton oButtonUstawienia = new JButton(oActionUstawienia);
		JButton oButtonZakoncz = new JButton(oActionZakoncz);
		oPanelPrzyciski.add(oButtonNowaGra);
		oPanelPrzyciski.add(oButtonUstawienia);
		oPanelPrzyciski.add(oButtonZakoncz);
		if (oStatusGry.getGameLaunched() == false)
			add(oPanelPrzyciski, BorderLayout.CENTER);
		
		//obszar rysowania wydarzen
		oComponentWydarzenia = new JComponentWydarzenia();
		add(oComponentWydarzenia, BorderLayout.PAGE_START);
		
		//obszar rysowania statusu gry
		oComponentStatusGry = new JComponentStatusGry(this.oStatusGry);
		add(oComponentStatusGry, BorderLayout.PAGE_END);
		
		//menu
		oMenu = new JMenuBar();
		setJMenuBar(oMenu);
		
		//menu "statki"
		JMenu oMenuStatki = new JMenu(JFrameGameWindowSettings.LANG.getProperty("menu.game"));
		JMenuItem oMenuStatkiNew = new JMenuItem(oActionNowaGra);
		JMenuItem oMenuStatkiClose = new JMenuItem(oActionZakoncz);
		oMenuStatki.add(oMenuStatkiNew);
		oMenuStatki.add(oMenuStatkiClose);
		oMenu.add(oMenuStatki);

		//menu "opcje"
		JMenu oMenuOpcje = new JMenu(JFrameGameWindowSettings.LANG.getProperty("menu.options"));
		JMenuItem oMenuOpcjeConf = new JMenuItem(oActionUstawienia);
		oMenuOpcje.add(oMenuOpcjeConf);
		oMenu.add(oMenuOpcje);
		
		//menu "help"
		JMenu oMenuPomoc = new JMenu(JFrameGameWindowSettings.LANG.getProperty("menu.help"));
		JMenuItem oMenuPomocAbout = new JMenuItem(new ActionAbout());
		oMenuPomoc.add(oMenuPomocAbout);
		oMenu.add(oMenuPomoc);
		
		//mapa wejsc
		InputMap oIMap = oPanelPrzyciski.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		oIMap.put(KeyStroke.getKeyStroke("F2"), "action.nowaGra");
		
		//mapa akcji
		ActionMap oAMap = oPanelPrzyciski.getActionMap();
		oAMap.put("action.nowaGra", oActionNowaGra);
		}
	/**
	 * Metoda dodaje do kontenera plansz przekazana w parametrze plansze.
	 * 
	 * @param oBoard Board, ktora ma byc wyswietlana w kontenerze plansz.
	 * @param bWyswietlStatki Zmienna okreslajaca, czy na planszy maja byc wyswietlane takze nietrafione pola statkow.
	 */
	public void dodajPlansze(Board oBoard, boolean bWyswietlStatki)
		{
		dodajPlansze(oBoard, bWyswietlStatki, null);
		}
	/**
	 * Metoda dodaje do kontenera plansz przekazana w parametrze plansze.<br />
	 * 
	 * Wersja przeciazona, ktora dodatkowo pozwala przekazac listener klikniec na plansze.
	 * 
	 * @param oBoard Board, ktora ma byc wyswietlana w kontenerze plansz.
	 * @param bWyswietlStatki Zmienna okreslajaca, czy na planszy maja byc wyswietlane takze nietrafione pola statkow.
	 * @param oMouseListener Obiekt obslugi zdarzen klikniec dla dodawanej planszy.
	 */
	public void dodajPlansze(Board oBoard, boolean bWyswietlStatki, RozgrywkaMouseListener oMouseListener)
		{
		JComponentPlansza oCompPlansza = new JComponentPlansza(oBoard);
		if (oMouseListener != null)
			{
			if (oMouseListener.isSetComponent() == false)
				oMouseListener.setComponent(oCompPlansza);
			oCompPlansza.addMouseListener(oMouseListener);
			}
		oCompPlansza.setWyswietlStatki(bWyswietlStatki);
		oPanelPlanszeKontener.add(oCompPlansza);
		}
	/**
	 * Metoda wywolywana przez panel rozmieszczania statkow, po tym jak zostanie zatwierdzone prawidlowe rozmieszczenie statkow gracza.
	 */
	public void rozpocznijRozgrywke()
		{
		oShipsGracz = oPanelZaznaczanieStatkow.getStatki();
		oShipsKomputer = JFrameGameWindowSettings.generujGracza(oUstawienia);
		oComponentStatusGry.setStatkiGracz(oShipsGracz);
		oComponentStatusGry.setStatkiKomputer(oShipsKomputer);
		oComponentStatusGry.aktualizujDane();
		oAi = AiFactory.getAi(oUstawienia.getDifficultyLevel(), oUstawienia.getStraightLines(), oShipsKomputer);
		ShipPositioner oPozycjoner = new ShipPositioner();
		boolean bUdaneRozmieszczenie = false;
		boolean bContinue = true;
		try
			{
			while (bUdaneRozmieszczenie == false && bContinue == true)
				{
				bUdaneRozmieszczenie = oPozycjoner.shipSpaces(oShipsKomputer, oUstawienia.getStraightLines());
				if (bUdaneRozmieszczenie == false)
					{
					if (JOptionPane.showConfirmDialog(this, LANG.getProperty("errorMsg.shipPlacement.computerShipPlacementError"), LANG.getProperty("errorMsg.windowTitle"), JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
						bContinue = false;
					}
				}
			}
		catch(ParameterException e)
			{
			throw new DeveloperException(e);
			}
		if (bUdaneRozmieszczenie == false)
			{
			JOptionPane.showMessageDialog(this, LANG.getProperty("errorMsg.shipPlacement.computerShipPlacementFail"));
			}
		else
			{
			bKolejGracza = true;
			oPanelPlanszeKontener.removeAll();
			if (oPanelPlanszeKontener.getComponentCount() == 0)
				{
				dodajPlansze(oShipsGracz.getBoard(), true);
				dodajPlansze(oShipsKomputer.getBoard(), false, new RozgrywkaMouseListener(oShipsKomputer.getBoard(), null));
				}
			oPanelPrzyciski.setVisible(false);
			oPanelZaznaczanieStatkow.setVisible(false);
			oPanelPlanszeKontener.setVisible(true);
			add(oPanelPlanszeKontener, BorderLayout.CENTER);
			repaint();
			}
		}
	/**
	 * Metoda wywolywana przez okno ustawien w przypadku zmian w ustawieniach rozgrywki
	 * (zmiana wielkosci planszy, ilosci i/lub wielkosci statkow, poziomu trudnosci).<br />
	 * 
	 * Koryguje wymagane obiekty, aby dopasowac je do nowych ustawien i jesli byla rozpoczeta gra, anuluje ja i rozpoczyna nowa.
	 */
	public void zmianaUstawien()
		{
		oPanelZaznaczanieStatkow.resetujPlansze();
		oPanelZaznaczanieStatkow.resetujOpis();
		//oPanelZaznaczanieStatkow.repaint();
		if (oStatusGry.getGameLaunched() == true)
			{
			Timer oTimer = new Timer(10, new ActionNowaGra());
			oTimer.setRepeats(false);
			oTimer.start();
			}
		}
	/**
	 * Metoda tworzy nowa plansze i nowy kontener zawierajacy statki dla gracza i zwraca obiekt kontenera.<br />
	 * 
	 * Size planszy, ilosc i Size statkow sa ustalane na podstawie ustawien gry przekazanych w parametrze.
	 * 
	 * @param oUstawienia Settings glowne gry.
	 * @return Zwraca kontener statkow gracza.
	 */
	public static ShipIterator generujGracza(Settings oUstawienia)
		{
		Board oBoard = new Board(oUstawienia.getBoardWidth(), oUstawienia.getBoardHeight());
		ShipIterator oShips = new ShipIterator(oBoard);
		int[] aListaStatkow = oUstawienia.getShips();
		for (int iSize: aListaStatkow)
			oShips.addAShip(iSize);
		return oShips;
		}
	}
