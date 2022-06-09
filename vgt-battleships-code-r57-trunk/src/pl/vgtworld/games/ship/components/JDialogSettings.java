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
 * Window ustawien gry.
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
	private JFrameGameWindowSettings oMainWindow;
	/**
	 * Obiekt przechowujacy ustawienia rozgrywki.
	 */
	private Settings oSettings;
	/**
	 * Slider pozwalajacy ustawic Width board.
	 */
	private JSlider oBoardWidthSlider;
	/**
	 * Slider pozwalajacy ustawic Height board.
	 */
	private JSlider oBoardHeightSlider;
	/**
	 * Slider pozwalajacy ustawic poziom trudnosci komputera.
	 */
	private JSlider oDifficultyLevel;
	/**
	 * Pole Textowe przechowujace Width board.
	 */
	private JTextField oBoardWidth;
	/**
	 * Pole Textowe przechowujace Height board.
	 */
	private JTextField oBoardHeight;
	/**
	 * Checkbox zawierajacy informacje, czy statki moga byc tylko prostymi liniami.
	 */
	private JCheckBox oShipsStraightLines;
	/**
	 * Przycisk zapisujacy zmiany w ustawieniach.
	 */
	private JButton oButtonSave;
	/**
	 * Przycisk anulujacy zmiany w ustawieniach.
	 */
	private JButton oButtonCancel;
	/**
	 * Panel do obslugi tworzenia listy statkow.
	 */
	private JPanelShipListSettings oShipList;
	/**
	 * Checkbox okreslajacy, czy zapisac aktualne ustawienia, jako domyslne.
	 */
	private JCheckBox oSaveSettings;
	/**
	 * Klasa prywatna zawierajaca obsluge akcji przesuniecia slidera okreslajacego Width board.
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
	 * Klasa prywatna zawierajaca obsluge akcji przesuniecia slidera okreslajacego Height board.
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
	private class ActionCancel
		extends AbstractAction
		{
		public ActionCancel()
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
	private class ActionSave
		extends AbstractAction
		{
		public ActionSave()
			{
			putValue(Action.NAME, JFrameGameWindowSettings.LANG.getProperty("action.settings.save"));
			putValue(Action.SHORT_DESCRIPTION, JFrameGameWindowSettings.LANG.getProperty("action.settings.save.desc"));
			}
		public void actionPerformed(ActionEvent oEvent)
			{
			try
				{
				//sprawdzenie blednych danych w Valuesach ustawien
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
				if (oShipList.getShipList().getNumberOfShips() == 0)
					{
					JOptionPane.showMessageDialog(JDialogSettings.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.settings.noShip"));
					return;
					}
				//zapisanie ustawien
				oSettings.setBoardWidth(iBoardWidth);
				oSettings.setBoardHeight(iBoardHeight);
				oSettings.setDifficultyLevel(oDifficultyLevel.getValue());
				if (oShipsStraightLines.isSelected() == true)
					oSettings.setStraightLines(true);
				else
					oSettings.setStraightLines(false);
				oSettings.removeAllShips();
				int[] aLista = oShipList.getShipList().getShipList();
				for (int iSize: aLista)
					oSettings.addShip(iSize);
				
				oMainWindow.changeSettings();
				
				if (oSaveSettings.isSelected())
					oSettings.saveDefaultSettings();
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
	public JDialogSettings(JFrameGameWindowSettings oMainWindow, Settings oSettings)
		{
		super(oMainWindow, true);
		this.oMainWindow = oMainWindow;
		this.oSettings = oSettings;
		//Width board
		JLabel oBoardWidthLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("settings.boardWidth"), JLabel.CENTER);
		oBoardWidthSlider = new JSlider(5, 25, oSettings.getBoardWidth());
		oBoardWidthSlider.addChangeListener(new ActionWidthSlider());
		oBoardWidth = new JTextField(5);
		oBoardWidth.setHorizontalAlignment(JTextField.RIGHT);
		oBoardWidth.setText(String.valueOf(oBoardWidthSlider.getValue()));
		//Height board
		JLabel oBoardHeightLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("settings.boardHeight"), JLabel.CENTER);
		oBoardHeightSlider = new JSlider(5, 25, oSettings.getBoardHeight());
		oBoardHeightSlider.addChangeListener(new ActionHeightSlider());
		oBoardHeight = new JTextField(5);
		oBoardHeight.setHorizontalAlignment(JTextField.RIGHT);
		oBoardHeight.setText(String.valueOf(oBoardHeightSlider.getValue()));
		//poziom trudnosci
		JLabel oDifficultyLevelLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("settings.difficulty"), JLabel.CENTER);
		oDifficultyLevel = new JSlider(1, 100, oSettings.getDifficultyLevel());
		//ksztalkt statkow
		JLabel oShipsStraightLinesLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("settings.shipsShape"), JLabel.CENTER);
		oShipsStraightLines = new JCheckBox(JFrameGameWindowSettings.LANG.getProperty("settings.shipsShapeCheckbox"));
		if (oSettings.getStraightLines() == true)
			oShipsStraightLines.setSelected(true);
		//lista statkow
		oShipList = new JPanelShipListSettings();
		int[] aShips = oSettings.getShips();
		try
			{
			for (int iSize: aShips)
				oShipList.getShipList().addList(iSize);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		
		//wstawienie elementow do frame'a
		setLayout(new BorderLayout());
		JPanel oPanelLeft = new JPanel();
		oPanelLeft.setLayout(new GridLayout(4, 1));
		
		JPanel oBoardWidthContainer2 = new JPanel();
		JPanel oBoardWidthContainer = new JPanel();
		oBoardWidthContainer.setLayout(new GridLayout(3, 1));
		oBoardWidthContainer.add(oBoardWidthLabel);
		JPanel oBoardWidthTextfieldContainer = new JPanel();
		oBoardWidthTextfieldContainer.add(oBoardWidth);
		oBoardWidthContainer.add(oBoardWidthSlider);
		oBoardWidthContainer.add(oBoardWidthTextfieldContainer);
		oBoardWidthContainer2.add(oBoardWidthContainer);
		oPanelLeft.add(oBoardWidthContainer2);
		
		JPanel oBoardHeightContainer2 = new JPanel();
		JPanel oBoardHeightContainer = new JPanel();
		oBoardHeightContainer.setLayout(new GridLayout(3, 1));
		oBoardHeightContainer.add(oBoardHeightLabel);
		JPanel oBoardHeightTextfieldContainer = new JPanel();
		oBoardHeightTextfieldContainer.add(oBoardHeight);
		oBoardHeightContainer.add(oBoardHeightSlider);
		oBoardHeightContainer.add(oBoardHeightTextfieldContainer);
		oBoardHeightContainer2.add(oBoardHeightContainer);
		oPanelLeft.add(oBoardHeightContainer2);
		
		JPanel oDifficultyLevelContainer2 = new JPanel();
		JPanel oDifficultyLevelContainer = new JPanel();
		oDifficultyLevelContainer.setLayout(new GridLayout(2,1));
		oDifficultyLevelContainer.add(oDifficultyLevelLabel);
		JPanel oDifficultyLevelSliderContainer = new JPanel();
		oDifficultyLevelSliderContainer.add(oDifficultyLevel);
		oDifficultyLevelContainer.add(oDifficultyLevelSliderContainer);
		oDifficultyLevelContainer2.add(oDifficultyLevelContainer);
		oPanelLeft.add(oDifficultyLevelContainer2);
		
		JPanel oShipsShapeContainer2 = new JPanel();
		JPanel oShipsShapeContainer = new JPanel();
		oShipsShapeContainer.setLayout(new GridLayout(2, 1));
		
                // REMOVIDA OPÇÃO QUE PERMITE O POSICIONAMENTO DOS NAVIOS DE FORMA QUE NÃO SEJA VERTICAL OU HORIZONTAL
                //oShipsShapeContainer.add(oShipsStraightLinesLabel);
		//oShipsShapeContainer.add(oShipsStraightLines);
                
		oShipsShapeContainer2.add(oShipsShapeContainer);
		oPanelLeft.add(oShipsShapeContainer2);
		
		JPanel oPanelRight = new JPanel();
		oPanelRight.setLayout(new BorderLayout());
		oPanelRight.add(oShipList, BorderLayout.CENTER);
		
		JPanel oPanels = new JPanel();
		oPanels.setLayout(new GridLayout(1, 2));
		oPanels.add(oPanelLeft);
		oPanels.add(oPanelRight);
		add(oPanels, BorderLayout.CENTER);
		JPanel oButtons = new JPanel();
		oButtonCancel = new JButton(new ActionCancel());
		oButtonSave = new JButton(new ActionSave());
		oButtons.add(oButtonSave);
		oButtons.add(oButtonCancel);
		
		JPanel oSaveSettingsPanel = new JPanel();
		oSaveSettings = new JCheckBox(JFrameGameWindowSettings.LANG.getProperty("settings.saveAsDefault"));
		oSaveSettingsPanel.add(oSaveSettings);
		
		JPanel oBottomOptionsPanel = new JPanel();
		oBottomOptionsPanel.setLayout(new GridLayout(2, 1));
		oBottomOptionsPanel.add(oSaveSettingsPanel);
		oBottomOptionsPanel.add(oButtons);
		
		add(oBottomOptionsPanel, BorderLayout.PAGE_END);
		
		
		//pozostale ustawienia
		setMinimumSize(new Dimension(Width, Height));
		setTitle(JFrameGameWindowSettings.LANG.getProperty("settingsWindow.title"));
		setLocationRelativeTo(null);
		setResizable(true);
		}
	/**
	 * Metoda przywraca ustawienia wszystkich sliderow i pol Textowych na Values z obiektu ustawien.
	 */
	public void reset()
		{
		//rest pozycji okna
		int iPositionX = oMainWindow.getX() + (oMainWindow.getWidth() - Width) / 2;
		int iPositionY = oMainWindow.getY() + (oMainWindow.getHeight() - Height) / 2;
		setBounds(iPositionX, iPositionY, Width, Height);
		//reset ustawien
		oBoardWidth.setText(String.valueOf(oSettings.getBoardWidth()));
		oBoardWidthSlider.setValue(oSettings.getBoardWidth());
		oBoardHeight.setText(String.valueOf(oSettings.getBoardHeight()));
		oBoardHeightSlider.setValue(oSettings.getBoardHeight());
		oDifficultyLevel.setValue(oSettings.getDifficultyLevel());
		if (oSettings.getStraightLines() == true)
			oShipsStraightLines.setSelected(true);
		else
			oShipsStraightLines.setSelected(false);
		oShipList.getShipList().clearList();
		int[] aShips = oSettings.getShips();
		try
			{
			for (int iSize: aShips)
				oShipList.getShipList().addList(iSize);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	}
