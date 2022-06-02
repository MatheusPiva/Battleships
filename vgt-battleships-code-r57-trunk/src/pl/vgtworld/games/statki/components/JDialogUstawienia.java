package pl.vgtworld.games.statki.components;

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
public class JDialogUstawienia
	extends JDialog
	{
	/**
	 * Szerokosc okna ustawien.
	 */
	public static final int SZEROKOSC = 600;
	/**
	 * Wysokosc okna ustawien.
	 */
	public static final int WYSOKOSC = 450; 
	/**
	 * Referencja do glownego okna gry.
	 */
	private JFrameGameWindowSettings oOknoGlowne;
	/**
	 * Obiekt przechowujacy ustawienia rozgrywki.
	 */
	private Settings oUstawienia;
	/**
	 * Slider pozwalajacy ustawic szerokosc planszy.
	 */
	private JSlider oBoardSzerokoscSlider;
	/**
	 * Slider pozwalajacy ustawic wysokosc planszy.
	 */
	private JSlider oBoardWysokoscSlider;
	/**
	 * Slider pozwalajacy ustawic poziom trudnosci komputera.
	 */
	private JSlider oPoziomTrudnosci;
	/**
	 * Pole tekstowe przechowujace szerokosc planszy.
	 */
	private JTextField oBoardSzerokosc;
	/**
	 * Pole tekstowe przechowujace wysokosc planszy.
	 */
	private JTextField oBoardWysokosc;
	/**
	 * Checkbox zawierajacy informacje, czy statki moga byc tylko prostymi liniami.
	 */
	private JCheckBox oStatkiProsteLinie;
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
	private JPanelUstawieniaListaStatkow oListaStatkow;
	/**
	 * Checkbox okreslajacy, czy zapisac aktualne ustawienia, jako domyslne.
	 */
	private JCheckBox oZapiszUstawienia;
	/**
	 * Klasa prywatna zawierajaca obsluge akcji przesuniecia slidera okreslajacego szerokosc planszy.
	 */
	private class ActionSzerokoscSlider
		implements ChangeListener
		{
		public void stateChanged(ChangeEvent oEvent)
			{
			JSlider oSource = (JSlider)oEvent.getSource();
			oBoardSzerokosc.setText(String.valueOf(oSource.getValue()));
			}
		}
	/**
	 * Klasa prywatna zawierajaca obsluge akcji przesuniecia slidera okreslajacego wysokosc planszy.
	 */
	private class ActionWysokoscSlider
		implements ChangeListener
		{
		public void stateChanged(ChangeEvent oEvent)
			{
			JSlider oSource = (JSlider)oEvent.getSource();
			oBoardWysokosc.setText(String.valueOf(oSource.getValue()));
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
				int iPlanszaSzerokosc;
				int iPlanszaWysokosc;
				try
					{
					iPlanszaSzerokosc = Integer.parseInt(oBoardSzerokosc.getText());
					iPlanszaWysokosc = Integer.parseInt(oBoardWysokosc.getText());
					}
				catch (NumberFormatException e)
					{
					JOptionPane.showMessageDialog(JDialogUstawienia.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.settings.invalidBoardSize"));
					return;
					}
				if (iPlanszaSzerokosc < 1 || iPlanszaWysokosc < 1)
					{
					JOptionPane.showMessageDialog(JDialogUstawienia.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.settings.invalidBoardSize"));
					return;
					}
				if (oListaStatkow.getListaStatkow().getNumberOfShips() == 0)
					{
					JOptionPane.showMessageDialog(JDialogUstawienia.this, JFrameGameWindowSettings.LANG.getProperty("errorMsg.settings.noShip"));
					return;
					}
				//zapisanie ustawien
				oUstawienia.setBoardWidth(iPlanszaSzerokosc);
				oUstawienia.setBoardHeight(iPlanszaWysokosc);
				oUstawienia.setDifficultyLevel(oPoziomTrudnosci.getValue());
				if (oStatkiProsteLinie.isSelected() == true)
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
	public JDialogUstawienia(JFrameGameWindowSettings oOknoGlowne, Settings oUstawienia)
		{
		super(oOknoGlowne, true);
		this.oOknoGlowne = oOknoGlowne;
		this.oUstawienia = oUstawienia;
		//szerokosc planszy
		JLabel oBoardSzerokoscLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("settings.boardWidth"), JLabel.CENTER);
		oBoardSzerokoscSlider = new JSlider(5, 25, oUstawienia.getBoardWidth());
		oBoardSzerokoscSlider.addChangeListener(new ActionSzerokoscSlider());
		oBoardSzerokosc = new JTextField(5);
		oBoardSzerokosc.setHorizontalAlignment(JTextField.RIGHT);
		oBoardSzerokosc.setText(String.valueOf(oBoardSzerokoscSlider.getValue()));
		//wysokosc planszy
		JLabel oBoardWysokoscLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("settings.boardHeight"), JLabel.CENTER);
		oBoardWysokoscSlider = new JSlider(5, 25, oUstawienia.getBoardHeight());
		oBoardWysokoscSlider.addChangeListener(new ActionWysokoscSlider());
		oBoardWysokosc = new JTextField(5);
		oBoardWysokosc.setHorizontalAlignment(JTextField.RIGHT);
		oBoardWysokosc.setText(String.valueOf(oBoardWysokoscSlider.getValue()));
		//poziom trudnosci
		JLabel oPoziomTrudnosciLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("settings.difficulty"), JLabel.CENTER);
		oPoziomTrudnosci = new JSlider(1, 100, oUstawienia.getDifficultyLevel());
		//ksztalkt statkow
		JLabel oStatkiProsteLinieLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("settings.shipsShape"), JLabel.CENTER);
		oStatkiProsteLinie = new JCheckBox(JFrameGameWindowSettings.LANG.getProperty("settings.shipsShapeCheckbox"));
		if (oUstawienia.getStraightLines() == true)
			oStatkiProsteLinie.setSelected(true);
		//lista statkow
		oListaStatkow = new JPanelUstawieniaListaStatkow();
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
		
		JPanel oBoardSzerokoscContainer2 = new JPanel();
		JPanel oBoardSzerokoscContainer = new JPanel();
		oBoardSzerokoscContainer.setLayout(new GridLayout(3, 1));
		oBoardSzerokoscContainer.add(oBoardSzerokoscLabel);
		JPanel oBoardSzerokoscTextfieldContainer = new JPanel();
		oBoardSzerokoscTextfieldContainer.add(oBoardSzerokosc);
		oBoardSzerokoscContainer.add(oBoardSzerokoscSlider);
		oBoardSzerokoscContainer.add(oBoardSzerokoscTextfieldContainer);
		oBoardSzerokoscContainer2.add(oBoardSzerokoscContainer);
		oPanelLewy.add(oBoardSzerokoscContainer2);
		
		JPanel oBoardWysokoscContainer2 = new JPanel();
		JPanel oBoardWysokoscContainer = new JPanel();
		oBoardWysokoscContainer.setLayout(new GridLayout(3, 1));
		oBoardWysokoscContainer.add(oBoardWysokoscLabel);
		JPanel oBoardWysokoscTextfieldContainer = new JPanel();
		oBoardWysokoscTextfieldContainer.add(oBoardWysokosc);
		oBoardWysokoscContainer.add(oBoardWysokoscSlider);
		oBoardWysokoscContainer.add(oBoardWysokoscTextfieldContainer);
		oBoardWysokoscContainer2.add(oBoardWysokoscContainer);
		oPanelLewy.add(oBoardWysokoscContainer2);
		
		JPanel oPoziomTrudnosciContainer2 = new JPanel();
		JPanel oPoziomTrudnosciContainer = new JPanel();
		oPoziomTrudnosciContainer.setLayout(new GridLayout(2,1));
		oPoziomTrudnosciContainer.add(oPoziomTrudnosciLabel);
		JPanel oPoziomTrudnosciSliderContainer = new JPanel();
		oPoziomTrudnosciSliderContainer.add(oPoziomTrudnosci);
		oPoziomTrudnosciContainer.add(oPoziomTrudnosciSliderContainer);
		oPoziomTrudnosciContainer2.add(oPoziomTrudnosciContainer);
		oPanelLewy.add(oPoziomTrudnosciContainer2);
		
		JPanel oStatkiKsztaltContainer2 = new JPanel();
		JPanel oStatkiKsztaltContainer = new JPanel();
		oStatkiKsztaltContainer.setLayout(new GridLayout(2, 1));
		
                // REMOVIDA OPÇÃO QUE PERMITE O POSICIONAMENTO DOS NAVIOS DE FORMA QUE NÃO SEJA VERTICAL OU HORIZONTAL
                //oStatkiKsztaltContainer.add(oStatkiProsteLinieLabel);
		//oStatkiKsztaltContainer.add(oStatkiProsteLinie);
                
		oStatkiKsztaltContainer2.add(oStatkiKsztaltContainer);
		oPanelLewy.add(oStatkiKsztaltContainer2);
		
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
		setMinimumSize(new Dimension(SZEROKOSC, WYSOKOSC));
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
		int iPozycjaX = oOknoGlowne.getX() + (oOknoGlowne.getWidth() - SZEROKOSC) / 2;
		int iPozycjaY = oOknoGlowne.getY() + (oOknoGlowne.getHeight() - WYSOKOSC) / 2;
		setBounds(iPozycjaX, iPozycjaY, SZEROKOSC, WYSOKOSC);
		//reset ustawien
		oBoardSzerokosc.setText(String.valueOf(oUstawienia.getBoardWidth()));
		oBoardSzerokoscSlider.setValue(oUstawienia.getBoardWidth());
		oBoardWysokosc.setText(String.valueOf(oUstawienia.getBoardHeight()));
		oBoardWysokoscSlider.setValue(oUstawienia.getBoardHeight());
		oPoziomTrudnosci.setValue(oUstawienia.getDifficultyLevel());
		if (oUstawienia.getStraightLines() == true)
			oStatkiProsteLinie.setSelected(true);
		else
			oStatkiProsteLinie.setSelected(false);
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
