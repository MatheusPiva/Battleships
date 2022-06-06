package pl.vgtworld.games.ship.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.games.ship.Settings;

/**
 * Okno ustawien gry.
 * 
 * @author VGT
 * @version 1.0
 */
public class JDialogSettings
	extends JDialog
	{
	/**
	 * Width okna ustawien.
	 */
	public static final int Width = 600;
	/**
	 * Height okna ustawien.
	 */
	public static final int Height = 450; 
	/**
	 * Referencja do glownego okna gry.
	 */
	private JFrameGameWindowSettings oOknoGlowne;
	/**
	 * Obiekt przechowujacy ustawienia rozgrywki.
	 */
	private Settings oUstawienia;
	/**
	 * Slider pozwalajacy ustawic Width planszy.
	 */
	private JSlider oBoardWidthSlider;
	/**
	 * Slider pozwalajacy ustawic Height planszy.
	 */
	private JSlider oBoardHeightSlider;
	/**
	 * Slider pozwalajacy ustawic poziom trudnosci komputera.
	 */
	private JSlider oPoziomTrudnosci;
	/**
	 * Pole tekstowe przechowujace Width planszy.
	 */
	private JTextField oBoardWidth;
	/**
	 * Pole tekstowe przechowujace Height planszy.
	 */
	private JTextField oBoardHeight;
	/**
	 * Checkbox zawierajacy informacje, czy statki moga byc tylko prostymi liniami.
	 */
	private JCheckBox oShipsProsteLinie;
	/**
	 * Przycisk zapisujacy zmiany w ustawieniach.
	 */
	private JButton oButtonZapisz;
	/**
	 * Przycisk anulujacy zmiany w ustawieniach.
	 */
	private JButton oButtonAnuluj;
	/**
	 * Panel do obslugi tworzenia listy statkow.
	 */
	private JPanelShipListSettings oListaStatkow;
	/**
	 * Checkbox okreslajacy, czy zapisac aktualne ustawienia, jako domyslne.
	 */
	private JCheckBox oZapiszUstawienia;
	/**
	 * Klasa prywatna zawierajaca obsluge akcji przesuniecia slidera okreslajacego Width planszy.
	 */
	private class ActionWidthSlider
		implements ChangeListener
		{
		public void stateChanged(ChangeEvent oEvent)
			{
			JSlider oSource = (JSlider)oEvent.getSource();
			oBoardWidth.setText(String.valueOf(oSource.getValue()));
			}
		}
	/**
	 * Klasa prywatna zawierajaca obsluge akcji przesuniecia slidera okreslajacego Height planszy.
	 */
	private class ActionHeightSlider
		implements ChangeListener
		{
		public void stateChanged(ChangeEvent oEvent)
			{
			JSlider oSource = (JSlider)oEvent.getSource();
			oBoardHeight.setText(String.valueOf(oSource.getValue()));
			}
		}
	/**
	 * Klasa prywatna zawierajaca obsluge akcji wcisniecia przycisku anulujacego zmiany w ustawieniach. 
	 */
	private class ActionAnuluj
		extends AbstractAction
		{
		public ActionAnuluj()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.settings.cancel"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.settings.cancel.desc"));
			}
		public void actionPerformed(ActionEvent oEvent)
			{
			setVisible(false);
			}
		}
	/**
	 * Klasa prywatna zawierajaca obsluge akcji wcisniecia przycisku zatwierdzajacego zmiany w ustawieniach.
	 */
	private class ActionZapisz
		extends AbstractAction
		{
		public ActionZapisz()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.settings.save"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.settings.save.desc"));
			}
		public void actionPerformed(ActionEvent oEvent)
			{
			try
				{
				//sprawdzenie blednych danych w wartosciach ustawien
				int iBoardWidth;
				int iBoardHeight;
				try
					{
					iBoardWidth = Integer.parseInt(oBoardWidth.getText());
					iBoardHeight = Integer.parseInt(oBoardHeight.getText());
					}
				catch (NumberFormatException e)
					{
					JOptionPane.showMessageDialog(JDialogSettings.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.settings.invalidBoardSize"));
					return;
					}
				if (iBoardWidth < 1 || iBoardHeight < 1)
					{
					JOptionPane.showMessageDialog(JDialogSettings.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.settings.invalidBoardSize"));
					return;
					}
				if (oListaStatkow.getListaStatkow().getNumberOfShips() == 0)
					{
					JOptionPane.showMessageDialog(JDialogSettings.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.settings.noShip"));
					return;
					}
				//zapisanie ustawien
				oUstawienia.setBoardWidth(iBoardWidth);
				oUstawienia.setBoardHeight(iBoardHeight);
				oUstawienia.setDifficultyLevel(oPoziomTrudnosci.getValue());
				if (oShipsProsteLinie.isSelected() == true)
					oUstawienia.setStraightLines(true);
				else
					oUstawienia.setStraightLines(false);
				oUstawienia.removeAllShips();
				int[] aLista = oListaStatkow.getListaStatkow().getListaStatkow();
				for (int iSize: aLista)
					oUstawienia.addShip(iSize);
				
				oOknoGlowne.zmianaUstawien();
				
				if (oZapiszUstawienia.isSelected())
					oUstawienia.saveDefaultSettings();
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
			setVisible(false);
			}
		}
	/**
	 * Konstruktor.
	 */
	public JDialogSettings(JFrameGameWindowSettings oOknoGlowne, Settings oUstawienia)
		{
		super(oOknoGlowne, true);
		this.oOknoGlowne = oOknoGlowne;
		this.oUstawienia = oUstawienia;
		//Width planszy
		JLabel oBoardWidthLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("settings.boardWidth"), JLabel.CENTER);
		oBoardWidthSlider = new JSlider(5, 25, oUstawienia.getBoardWidth());
		oBoardWidthSlider.addChangeListener(new ActionWidthSlider());
		oBoardWidth = new JTextField(5);
		oBoardWidth.setHorizontalAlignment(JTextField.RIGHT);
		oBoardWidth.setText(String.valueOf(oBoardWidthSlider.getValue()));
		//Height planszy
		JLabel oBoardHeightLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("settings.boardHeight"), JLabel.CENTER);
		oBoardHeightSlider = new JSlider(5, 25, oUstawienia.getBoardHeight());
		oBoardHeightSlider.addChangeListener(new ActionHeightSlider());
		oBoardHeight = new JTextField(5);
		oBoardHeight.setHorizontalAlignment(JTextField.RIGHT);
		oBoardHeight.setText(String.valueOf(oBoardHeightSlider.getValue()));
		//poziom trudnosci
		JLabel oPoziomTrudnosciLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("settings.difficulty"), JLabel.CENTER);
		oPoziomTrudnosci = new JSlider(1, 100, oUstawienia.getDifficultyLevel());
		//ksztalkt statkow
		JLabel oShipsProsteLinieLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("settings.shipsShape"), JLabel.CENTER);
		oShipsProsteLinie = new JCheckBox(JFrameGameWindowSettings.LANG.getProperty("settings.shipsShapeCheckbox"));
		if (oUstawienia.getStraightLines() == true)
			oShipsProsteLinie.setSelected(true);
		//lista statkow
		oListaStatkow = new JPanelShipListSettings();
		int[] aShips = oUstawienia.getShips();
		try
			{
			for (int iSize: aShips)
				oListaStatkow.getListaStatkow().listaDodaj(iSize);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		
		//wstawienie elementow do frame'a
		setLayout(new BorderLayout());
		JPanel oPanelLewy = new JPanel();
		oPanelLewy.setLayout(new GridLayout(4, 1));
		
		JPanel oBoardWidthContainer2 = new JPanel();
		JPanel oBoardWidthContainer = new JPanel();
		oBoardWidthContainer.setLayout(new GridLayout(3, 1));
		oBoardWidthContainer.add(oBoardWidthLabel);
		JPanel oBoardWidthTextfieldContainer = new JPanel();
		oBoardWidthTextfieldContainer.add(oBoardWidth);
		oBoardWidthContainer.add(oBoardWidthSlider);
		oBoardWidthContainer.add(oBoardWidthTextfieldContainer);
		oBoardWidthContainer2.add(oBoardWidthContainer);
		oPanelLewy.add(oBoardWidthContainer2);
		
		JPanel oBoardHeightContainer2 = new JPanel();
		JPanel oBoardHeightContainer = new JPanel();
		oBoardHeightContainer.setLayout(new GridLayout(3, 1));
		oBoardHeightContainer.add(oBoardHeightLabel);
		JPanel oBoardHeightTextfieldContainer = new JPanel();
		oBoardHeightTextfieldContainer.add(oBoardHeight);
		oBoardHeightContainer.add(oBoardHeightSlider);
		oBoardHeightContainer.add(oBoardHeightTextfieldContainer);
		oBoardHeightContainer2.add(oBoardHeightContainer);
		oPanelLewy.add(oBoardHeightContainer2);
		
		JPanel oPoziomTrudnosciContainer2 = new JPanel();
		JPanel oPoziomTrudnosciContainer = new JPanel();
		oPoziomTrudnosciContainer.setLayout(new GridLayout(2,1));
		oPoziomTrudnosciContainer.add(oPoziomTrudnosciLabel);
		JPanel oPoziomTrudnosciSliderContainer = new JPanel();
		oPoziomTrudnosciSliderContainer.add(oPoziomTrudnosci);
		oPoziomTrudnosciContainer.add(oPoziomTrudnosciSliderContainer);
		oPoziomTrudnosciContainer2.add(oPoziomTrudnosciContainer);
		oPanelLewy.add(oPoziomTrudnosciContainer2);
		
		JPanel oShipsKsztaltContainer2 = new JPanel();
		JPanel oShipsKsztaltContainer = new JPanel();
		oShipsKsztaltContainer.setLayout(new GridLayout(2, 1));
		
                // REMOVIDA OPÇÃO QUE PERMITE O POSICIONAMENTO DOS NAVIOS DE FORMA QUE NÃO SEJA VERTICAL OU HORIZONTAL
                //oShipsKsztaltContainer.add(oShipsProsteLinieLabel);
		//oShipsKsztaltContainer.add(oShipsProsteLinie);
                
		oShipsKsztaltContainer2.add(oShipsKsztaltContainer);
		oPanelLewy.add(oShipsKsztaltContainer2);
		
		JPanel oPanelPrawy = new JPanel();
		oPanelPrawy.setLayout(new BorderLayout());
		oPanelPrawy.add(oListaStatkow, BorderLayout.CENTER);
		
		JPanel oPanele = new JPanel();
		oPanele.setLayout(new GridLayout(1, 2));
		oPanele.add(oPanelLewy);
		oPanele.add(oPanelPrawy);
		add(oPanele, BorderLayout.CENTER);
		JPanel oButtony = new JPanel();
		oButtonAnuluj = new JButton(new ActionAnuluj());
		oButtonZapisz = new JButton(new ActionZapisz());
		oButtony.add(oButtonZapisz);
		oButtony.add(oButtonAnuluj);
		
		JPanel oPanelZapiszUstawienia = new JPanel();
		oZapiszUstawienia = new JCheckBox(JFrameGameWindowSettings.LANG.getProperty("settings.saveAsDefault"));
		oPanelZapiszUstawienia.add(oZapiszUstawienia);
		
		JPanel oPanelOpcjeDolne = new JPanel();
		oPanelOpcjeDolne.setLayout(new GridLayout(2, 1));
		oPanelOpcjeDolne.add(oPanelZapiszUstawienia);
		oPanelOpcjeDolne.add(oButtony);
		
		add(oPanelOpcjeDolne, BorderLayout.PAGE_END);
		
		
		//pozostale ustawienia
		setMinimumSize(new Dimension(Width, Height));
		setTitle(JFrameGameWindowSettings.LANG.getProperty("settingsWindow.title"));
		setLocationRelativeTo(null);
		setResizable(true);
		}
	/**
	 * Metoda przywraca ustawienia wszystkich sliderow i pol tekstowych na wartosci z obiektu ustawien.
	 */
	public void reset()
		{
		//rest pozycji okna
		int iPositionX = oOknoGlowne.getX() + (oOknoGlowne.getWidth() - Width) / 2;
		int iPositionY = oOknoGlowne.getY() + (oOknoGlowne.getHeight() - Height) / 2;
		setBounds(iPositionX, iPositionY, Width, Height);
		//reset ustawien
		oBoardWidth.setText(String.valueOf(oUstawienia.getBoardWidth()));
		oBoardWidthSlider.setValue(oUstawienia.getBoardWidth());
		oBoardHeight.setText(String.valueOf(oUstawienia.getBoardHeight()));
		oBoardHeightSlider.setValue(oUstawienia.getBoardHeight());
		oPoziomTrudnosci.setValue(oUstawienia.getDifficultyLevel());
		if (oUstawienia.getStraightLines() == true)
			oShipsProsteLinie.setSelected(true);
		else
			oShipsProsteLinie.setSelected(false);
		oListaStatkow.getListaStatkow().listaWyczysc();
		int[] aShips = oUstawienia.getShips();
		try
			{
			for (int iSize: aShips)
				oListaStatkow.getListaStatkow().listaDodaj(iSize);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	}
