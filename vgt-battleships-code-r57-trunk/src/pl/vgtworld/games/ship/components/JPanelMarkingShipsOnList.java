package pl.vgtworld.games.ship.components;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pl.vgtworld.games.ship.Settings;

/**
 * Panel wyswietlajacy informacje na temat ilosci wymaganych statkow w poszczegolnych Sizeach.
 * 
 * @author VGT
 * @version 1.0
 */
public class JPanelMarkingShipsOnList
	extends JPanel
	{
	/**
	 * Obiekt ustawien, z ktorego sa wczytywane informacje na temat wymaganych statkow.
	 */
	private Settings oSettings;
	/**
	 * Kontener etykiet.
	 */
	private ArrayList<JLabel> oLabels;
	/**
	 * Kolor czcionki wyswietlanych informacji.
	 */
	private Color oTextColor;
	/**
	 * Konstruktor.
	 * 
	 * @param oSettings Obiekt ustawien glownych gry.
	 */
	public JPanelMarkingShipsOnList(Settings oSettings)
		{
		setBackground(Color.BLACK);
		oTextColor = new Color(230, 230, 230);
		this.oSettings = oSettings;
		oLabels = new ArrayList<JLabel>();
		BoxLayout oLay = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(oLay);
		
		refresh();
		}
	/**
	 * Metoda refresha wyswietlane informacje wczytujac je na nowo z obiektu ustawien.
	 */
	public void refresh()
		{
		//utworzenie etykiet
		if (oLabels.size() == 0)
			{
			JLabel oLabel = new JLabel(JFrameGameWindowSettings.LANG.getProperty("shipPlacement.list.header"));
			oLabel.setForeground(oTextColor);
			oLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			oLabels.add(oLabel);
			}
		int iMaxSize = oSettings.getMaxShipSize();
		int iTextLines = 1;
		int iQuantity;
		String sText;
		for (int i = iMaxSize; i >= 1; --i)
			{
			iQuantity = oSettings.getNumberOfShips(i);
			if (iQuantity > 0)
				{
				int iShipClass = i > 5 ? 5 : i;
				if (i == 1)
					sText = JFrameGameWindowSettings.LANG.getProperty("shipPlacement.list.prefix") + " " + JFrameGameWindowSettings.LANG.getProperty("shipName.size1.plural") + " (" + JFrameGameWindowSettings.LANG.getProperty("shipPlacement.list.size") +" 1): ";
				else
					sText = JFrameGameWindowSettings.LANG.getProperty("shipPlacement.list.prefix") + " " + JFrameGameWindowSettings.LANG.getProperty("shipName.size" + iShipClass + ".plural") + " (" + JFrameGameWindowSettings.LANG.getProperty("shipPlacement.list.size") + " " + i + "): ";
				++iTextLines;
				if (oLabels.size() < iTextLines)
					{
					JLabel oLabel = new JLabel(sText + iQuantity);
					oLabel.setForeground(oTextColor);
					oLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
					oLabels.add(oLabel);
					}
				else
					oLabels.get(iTextLines - 1).setText(sText + iQuantity);
				}
			}
		
		//wrzucenie etykiet w panel
		removeAll();
		for (int i = 0; i < iTextLines; ++i)
			add(oLabels.get(i));
		
		validate();
		repaint();
		}
	}
