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
 * Glowne Window gry.
 * 
 * @author VGT
 * @version 1.0
 */
public class JFrameGameWindowSettings
	extends JFrame
	{
	/**
	 * Zmienna przechowujaca wersje programu odczytana z Fileu.
	 */
	public static String sVersion;
	/**
	 * Stala przechowujaca minimalna Width okna glownego gry.
	 */
	public static final int MIN_WIDTH = 640;
	/**
	 * Stala przechowujaca minimalna Height okna glownego gry.
	 */
	public static final int MIN_Height = 480;
	/**
	 * File jezykowy.
	 */
	public static Properties LANG;
	/**
	 * Obiekt statusu gry.
	 */
	private GameStatus oGameStatus;
	/**
	 * Obiekt ustawien rozgrywki.
	 */
	private Settings oSettings;
	/**
	 * Window ustawien rozgrywki.
	 */
	private JDialogSettings oWindowSettings;
	/**
	 * Window informacji o autorze.
	 */
	private JDialogAbout oWindowAbout;
	/**
	 * Menu glownego okna gry.
	 */
	private JMenuBar oMenu;
	/**
	 * Panel przechowujacy plansze wyswietlany w trakcie gry.
	 */
	private JPanel oBoardPanelContainer;
	/**
	 * Panel wyswietlany po rozpoczeciu nowej gry. Realizuje obsluge rozmieszczenia statkow przez gracza.
	 */
	private JPanelMarkingShips oShipSelectionPanel;
	/**
	 * Panel startowy wyswietlany po uruchomieniu programu zawierajacy przyciski do uruchomienia gry,
	 * zmiany ustawien i zakonczenia rozgrywki.
	 */
	private JPanel oButtonsPanel;
	/**
	 * Komponent wyswietlany w gornej czesci okna prezentujacy komunikaty na temat wydarzen na board poszczegolnych graczy.
	 */
	private JComponentEvents oEventsComponent;
	/**
	 * Komponent wyswietlany w dolnej czesci okna prezentujacy informacje na temat stanu aktualnej rozgrywki.
	 */
	private JComponentGameStatus oGameStatusComponent;
	/**
	 * Kontener statkow gracza.
	 */
	private ShipIterator oPlayerShips;
	/**
	 * Kontener statkow komputera.
	 */
	private ShipIterator oComputerShips;
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
	private boolean bPlayerTurn;
	/**
	 * Klasa prywatna zawierajaca obsluge akcji rozpoczecia nowej rozgrywki.
	 */
	private class ActionNewGame
		extends AbstractAction
		{
		public ActionNewGame()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.newGame"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.newGame.desc"));
			}
		public void actionPerformed(ActionEvent event)
			{
			oGameStatus.startNewGame();
			oButtonsPanel.setVisible(false);
			oBoardPanelContainer.setVisible(false);
			oShipSelectionPanel.setVisible(true);
			add(oShipSelectionPanel, BorderLayout.CENTER);
			oShipSelectionPanel.ClearBoard();
			repaint();
			}
		}
	/**
	 * Klasa prywatna zawierajaca obsluge akcji wywolania okna ustawien rozgrywki.
	 */
	private class ActionSettings
		extends AbstractAction
		{
		public ActionSettings()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.settings"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.settings.desc"));
			}
		public void actionPerformed(ActionEvent oEvent)
			{
			oWindowSettings.reset();
			oWindowSettings.setVisible(true);
			}
		}
	/**
	 * Klasa prywatna zawierajaca obsluge akcji zakonczenia programu.
	 */
	private class ActionFinish
		extends AbstractAction
		{
		public ActionFinish()
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
			oWindowAbout.centerPosition();
			oWindowAbout.setVisible(true);
			}
		}
	/**
	 * Klasa prywatna obslugujaca przebieg pojedynczego cyklu rozgrywki.<br />
	 * 
	 * - oddanie shotu przez gracza poprzez klikniecie na plansze komputera realizowane przez metode mousePressed().<br />
	 * - oddanie shotu przez komputer na plansze gracza wywolywane z metody actionPerformed() za pomoca timera.
	 */
	private class GameplayMouseListener
		extends MouseAdapter
		implements ActionListener
		{
		private Board oBoard;
		private JComponentBoard oBoardComponent;
		private Timer oTimer;
		public GameplayMouseListener(Board oBoard, JComponentBoard oBoardComponent)
			{
			this.oBoard = oBoard;
			this.oBoardComponent = oBoardComponent;
			oTimer = new Timer(1000, this);
			oTimer.setRepeats(false);
			}
		public void setComponent(JComponentBoard oBoardComponent)
			{
			this.oBoardComponent = oBoardComponent;
			}
		public boolean isSetComponent()
			{
			if (oBoardComponent == null)
				return false;
			else
				return true;
			}
		@Override public void mousePressed(MouseEvent event)
			{
			int iBoardWidth = oBoard.getWidth();
			int iBoardHeight = oBoard.getHeight();
			int iComponentWidth = oBoardComponent.getWidth();
			int iComponentHeight = oBoardComponent.getHeight();
			int iClickX = event.getX();
			int iClickY = event.getY();
			Position oClickedField;
			oClickedField = DrawingCoordinatesOnBoard.pixToField(iComponentWidth, iComponentHeight, iBoardWidth, iBoardHeight, iClickX, iClickY);
			try
				{
				if (bPlayerTurn == true
					&& oClickedField.getX() >= 0 && oClickedField.getX() < iBoardWidth
					&& oClickedField.getY() >= 0 && oClickedField.getY() < iBoardHeight
					&& (oBoard.getField(oClickedField.getX(), oClickedField.getY()) == FieldTypeBoard.BOARD_FIELD_EMPTY
						|| oBoard.getField(oClickedField.getX(), oClickedField.getY()) == FieldTypeBoard.SHIP_BOARD
						)
					)
					{
					bPlayerTurn = false;
					int iQuantitySunkenBeforeShot = oComputerShips.getNumberOfUndamagedShips();
					
					//shot na plansze komputera
					boolean bHit;
					bHit = oComputerShips.shot(oClickedField.getX(), oClickedField.getY());
					JComponentBoard oBoardComponent = (JComponentBoard)oBoardPanelContainer.getComponent(1);
					oBoardComponent.activateHighlight(oClickedField);
					//obsluga sprawdzania, czy koniec gry
					if (bHit == true && oComputerShips.getNumberOfShips() == oComputerShips.getNumberOfUndamagedShips())
						{
						oGameStatus.playerVictory();
						oGameStatusComponent.updateData();
						JOptionPane.showMessageDialog(JFrameGameWindowSettings.this, LANG.getProperty("message.win"));
						return;
						}
					else if (bHit == true)
						{
                                                    
                                                //COLOCAR AUDIO DE EXPLOSÃO
                                                
                                                InputStream in;
                                                
                                                    try {
                                                        
                                                        in = new FileInputStream(new File ("src/pl/vgtworld/games/shpi/components/explosion.mp3"));
//                                                        AudioStream ad = new AudioStream(in);
//                                                        AudioPlayer.player.start(ad);
                                                  
                                                    } catch (Exception e) {
                                                    }
                                                    
						oGameStatusComponent.updateData();
						if (iQuantitySunkenBeforeShot != oComputerShips.getNumberOfUndamagedShips())
							oEventsComponent.setRightMessage(LANG.getProperty("message.hit2"));
						else
							oEventsComponent.setRightMessage(LANG.getProperty("message.hit1"));
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
			int iQuantitySunkenBeforeShot = oPlayerShips.getNumberOfUndamagedShips();
			//shot na plansze gracza
			boolean bHit = oAi.shot(oPlayerShips);
			JComponentBoard oBoardComponent = (JComponentBoard)oBoardPanelContainer.getComponent(0);
			oBoardComponent.activateHighlight(oPlayerShips.getLastShot());
			//obsluga sprawdzania, czy koniec gry
			if (bHit == true && oPlayerShips.getNumberOfShips() == oPlayerShips.getNumberOfUndamagedShips())
				{
				oGameStatus.computerVictory();
				oGameStatusComponent.updateData();
				int iComponentsQuantity = oBoardPanelContainer.getComponentCount();
				JComponentBoard oBoardComponent;
				for (int i = 0; i < iComponentsQuantity; ++i)
					{
					oBoardComponent = (JComponentBoard)oBoardPanelContainer.getComponent(i);
					if (oBoardComponent != null)
						oBoardComponent.setViewShips(true);
					}
				oBoardPanelContainer.repaint();
				JOptionPane.showMessageDialog(JFrameGameWindowSettings.this, LANG.getProperty("message.lose"));
				return;
				}
			else if (bHit == true)
				{
				oGameStatusComponent.updateData();
				if (iQuantitySunkenBeforeShot != oPlayerShips.getNumberOfUndamagedShips())
					oEventsComponent.setLeftMessage(LANG.getProperty("message.hit2"));
				else
					oEventsComponent.setLeftMessage(LANG.getProperty("message.hit1"));
				}
			
			bPlayerTurn = true;
			
			}
		}
	/**
	 * Konstruktor.
	 * 
	 * @param oGameStatus Obiekt przechowujacy informacje na temat aktualnego statusu gry.
	 * @param oSettings Obiekt przechowujacy ustawienia dotyczace rozgrywki.
	 */
	public JFrameGameWindowSettings(GameStatus oGameStatus, Settings oSettings)
		{
		this(oGameStatus, oSettings, MIN_WIDTH, MIN_Height);
		}
	/**
	 * konstruktor przeciazaony pozwalajacy zdefiniowac Size okna gry.
	 * 
	 * @param oGameStatus Obiekt przechowujacy informacje na temat aktualnego statusu gry.
	 * @param oSettings Obiekt przechowujacy ustawienia dotyczace rozgrywki.
	 * @param iWidth Width Window gry w pixel.
	 * @param iHeight Height okna gry w pixel.
	 */
	public JFrameGameWindowSettings(GameStatus oGameStatus, Settings oSettings, int iWidth, int iHeight)
		{
		InputStream oFile = getClass().getResourceAsStream("/wersja.txt");
		if (oFile != null)
			{
			Scanner oVer = new Scanner(oFile);
			sVersion = oVer.nextLine();
			}
		else
			sVersion = null;
		
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

		//setMinimumSize(new Dimension(MIN_WIDTH, MIN_Height));
		
		setTitle(JFrameGameWindowSettings.LANG.getProperty("mainWindow.title"));
		//setSize(iWidth, iHeight);
                setExtendedState(JFrameGameWindowSettings.MAXIMIZED_BOTH);
                setUndecorated(true);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		this.oGameStatus = oGameStatus;
		this.oSettings = oSettings;
		oWindowSettings = new JDialogSettings(this, oSettings);
		oWindowSettings.setLocationRelativeTo(this);
		oWindowAbout = new JDialogAbout(this, JFrameGameWindowSettings.LANG.getProperty("mainWindow.title"));
		if (sVersion != null)
			{
			oWindowAbout.setVersion(sVersion);
			oWindowAbout.rebuild();
			}
		
		//panel z planszami graczy
		oBoardPanelContainer = new JPanel();
		oBoardPanelContainer.setLayout(new GridLayout());
		if (oGameStatus.getGameLaunched() == true && oGameStatus.getShipsArranged() == true)
			add(oBoardPanelContainer, BorderLayout.CENTER);
		
		//panel z plansza do zaznaczania statkow po rozpoczeciu gry
		oShipSelectionPanel = new JPanelMarkingShips(oSettings, this);
		if (oGameStatus.getGameLaunched() == true && oGameStatus.getShipsArranged() == false)
			add(oShipSelectionPanel, BorderLayout.CENTER);
		
		//obiekty akcji
		ActionNewGame oActionNewGame = new ActionNewGame();
		ActionFinish oActionFinish = new ActionFinish();
		ActionSettings oActionSettings = new ActionSettings();
		
		//panel zastepujacy plansze przed rozpoczeciem rozgrywki
		oButtonsPanel = new JPanel();
		oButtonsPanel.setBackground(Color.BLACK);
		//oButtonsPanel.setLayout(new GridLayout());
		JButton oButtonNewGame = new JButton(oActionNewGame);
		JButton oButtonSettings = new JButton(oActionSettings);
		JButton oButtonFinish = new JButton(oActionFinish);
		oButtonsPanel.add(oButtonNewGame);
		oButtonsPanel.add(oButtonSettings);
		oButtonsPanel.add(oButtonFinish);
		if (oGameStatus.getGameLaunched() == false)
			add(oButtonsPanel, BorderLayout.CENTER);
		
		//obszar rysowania wydarzen
		oEventsComponent = new JComponentEvents();
		add(oEventsComponent, BorderLayout.PAGE_START);
		
		//obszar rysowania statusu gry
		oGameStatusComponent = new JComponentGameStatus(this.oGameStatus);
		add(oGameStatusComponent, BorderLayout.PAGE_END);
		
		//menu
		oMenu = new JMenuBar();
		setJMenuBar(oMenu);
		
		//menu "statki"
		JMenu oShipsMenu = new JMenu(JFrameGameWindowSettings.LANG.getProperty("menu.game"));
		JMenuItem oShipsMenuNew = new JMenuItem(oActionNewGame);
		JMenuItem oShipsMenuClose = new JMenuItem(oActionFinish);
		oShipsMenu.add(oShipsMenuNew);
		oShipsMenu.add(oShipsMenuClose);
		oMenu.add(oShipsMenu);

		//menu "opcje"
		JMenu oOptionsMenu = new JMenu(JFrameGameWindowSettings.LANG.getProperty("menu.options"));
		JMenuItem oOptionsMenuConf = new JMenuItem(oActionSettings);
		oOptionsMenu.add(oOptionsMenuConf);
		oMenu.add(oOptionsMenu);
		
		//menu "help"
		JMenu oHelpMenu = new JMenu(JFrameGameWindowSettings.LANG.getProperty("menu.help"));
		JMenuItem oHelpMenuAbout = new JMenuItem(new ActionAbout());
		oHelpMenu.add(oHelpMenuAbout);
		oMenu.add(oHelpMenu);
		
		//mapa wejsc
		InputMap oIMap = oButtonsPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		oIMap.put(KeyStroke.getKeyStroke("F2"), "action.NewGame");
		
		//mapa akcji
		ActionMap oAMap = oButtonsPanel.getActionMap();
		oAMap.put("action.NewGame", oActionNewGame);
		}
	/**
	 * Metoda dodaje do kontenera plansz przekazana w parametrze plansze.
	 * 
	 * @param oBoard Board, ktora ma byc wyswietlana w kontenerze plansz.
	 * @param bViewShips Zmienna okreslajaca, czy na board maja byc wyswietlane takze nietrafione position statkow.
	 */
	public void addBoards(Board oBoard, boolean bViewShips)
		{
		addBoards(oBoard, bViewShips, null);
		}
	/**
	 * Metoda dodaje do kontenera plansz przekazana w parametrze plansze.<br />
	 * 
	 * Wersja przeciazona, ktora dodatkowo pozwala przekazac listener klikniec na plansze.
	 * 
	 * @param oBoard Board, ktora ma byc wyswietlana w kontenerze plansz.
	 * @param bViewShips Zmienna okreslajaca, czy na board maja byc wyswietlane takze nietrafione position statkow.
	 * @param oMouseListener Obiekt obslugi zdarzen klikniec dla dodawanej board.
	 */
	public void addBoards(Board oBoard, boolean bViewShips, GameplayMouseListener oMouseListener)
		{
		JComponentBoard oBoardComponent = new JComponentBoard(oBoard);
		if (oMouseListener != null)
			{
			if (oMouseListener.isSetComponent() == false)
				oMouseListener.setComponent(oBoardComponent);
			oBoardComponent.addMouseListener(oMouseListener);
			}
		oBoardComponent.setViewShips(bViewShips);
		oBoardPanelContainer.add(oBoardComponent);
		}
	/**
	 * Metoda wywolywana przez panel rozmieszczania statkow, po tym jak zostanie zatwierdzone prawidlowe rozmieszczenie statkow gracza.
	 */
	public void startGameplay()
		{
		oPlayerShips = oShipSelectionPanel.getShips();
		oComputerShips = JFrameGameWindowSettings.generatePlayer(oSettings);
		oGameStatusComponent.setPlayerShips(oPlayerShips);
		oGameStatusComponent.setComputerShips(oComputerShips);
		oGameStatusComponent.updateData();
		oAi = AiFactory.getAi(oSettings.getDifficultyLevel(), oSettings.getStraightLines(), oComputerShips);
		ShipPositioner oPositioner = new ShipPositioner();
		boolean bSuccessfulDeployment = false;
		boolean bContinue = true;
		try
			{
			while (bSuccessfulDeployment == false && bContinue == true)
				{
				bSuccessfulDeployment = oPositioner.shipSpaces(oComputerShips, oSettings.getStraightLines());
				if (bSuccessfulDeployment == false)
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
		if (bSuccessfulDeployment == false)
			{
			JOptionPane.showMessageDialog(this, LANG.getProperty("errorMsg.shipPlacement.computerShipPlacementFail"));
			}
		else
			{
			bPlayerTurn = true;
			oBoardPanelContainer.removeAll();
			if (oBoardPanelContainer.getComponentCount() == 0)
				{
				addBoards(oPlayerShips.getBoard(), true);
				addBoards(oComputerShips.getBoard(), false, new GameplayMouseListener(oComputerShips.getBoard(), null));
				}
			oButtonsPanel.setVisible(false);
			oShipSelectionPanel.setVisible(false);
			oBoardPanelContainer.setVisible(true);
			add(oBoardPanelContainer, BorderLayout.CENTER);
			repaint();
			}
		}
	/**
	 * Metoda wywolywana przez Window ustawien w przypadku zmian w ustawieniach rozgrywki
	 * (zmiana Sizei board, ilosci i/lub Sizei statkow, poziomu trudnosci).<br />
	 * 
	 * Koryguje wymagane obiekty, aby dopasowac je do nowych ustawien i jesli byla rozpoczeta gra, anuluje ja i rozpoczyna nowa.
	 */
	public void changeSettings()
		{
		oShipSelectionPanel.resetBoard();
		oShipSelectionPanel.resetDescription();
		//oShipSelectionPanel.repaint();
		if (oGameStatus.getGameLaunched() == true)
			{
			Timer oTimer = new Timer(10, new ActionNewGame());
			oTimer.setRepeats(false);
			oTimer.start();
			}
		}
	/**
	 * Metoda tworzy nowa plansze i nowy kontener zawierajacy statki dla gracza i zwraca obiekt kontenera.<br />
	 * 
	 * Size board, ilosc i Size statkow sa ustalane na podstawie ustawien gry przekazanych w parametrze.
	 * 
	 * @param oSettings Settings glowne gry.
	 * @return Zwraca kontener statkow gracza.
	 */
	public static ShipIterator generatePlayer(Settings oSettings)
		{
		Board oBoard = new Board(oSettings.getBoardWidth(), oSettings.getBoardHeight());
		ShipIterator oShips = new ShipIterator(oBoard);
		int[] aShipsList = oSettings.getShips();
		for (int iSize: aShipsList)
			oShips.addAShip(iSize);
		return oShips;
		}
	}
