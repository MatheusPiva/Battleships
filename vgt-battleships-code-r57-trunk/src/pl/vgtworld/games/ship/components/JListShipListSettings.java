package pl.vgtworld.games.ship.components;

import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import pl.vgtworld.exceptions.ParameterException;

/**
 * Obiekt listy wykorzystany w oknie ustawien do prezentacji listy zdefiniowanych statkow.
 * 
 * @author VGT
 * @version 1.0
 */
public class JListShipListSettings
	extends JList
	{
	/**
	 * Tablica przechowujaca Sizey statkow.
	 */
	private ArrayList<Integer> oListInt;
	/**
	 * Model listy przechowujacy Sizey statkow.
	 */
	private DefaultListModel oJListList;
	/**
	 * Konstruktor domyslny.
	 */
	public JListShipListSettings()
		{
		oListInt = new ArrayList<Integer>();
		oJListList = new DefaultListModel();
		setModel(oJListList);
		}
	/**
	 * Zwraca ilosc statkow przechowywana na liscie.
	 * 
	 * @return Ilosc statkow na liscie.
	 */
	public int getNumberOfShips()
		{
		return oListInt.size();
		}
	/**
	 * Zwraca tablice int zawierajaca Size wszystkich statkow przechowywanych w liscie.
	 * 
	 * @return Tablica z Sizeami statkow przechowywanych w liscie.
	 */
	public int[] getShipList()
		{
		int[] aLista = new int[ oListInt.size() ];
		for (int i = 0; i < oListInt.size(); ++i)
			aLista[i] = oListInt.get(i);
		return aLista;
		}
	/**
	 * Dodaje do listy statek o podanym Sizeze.
	 * 
	 * @param iSize Size dodawanego statku.
	 * @throws ParameterException Wyrzuca wyjatek, jesli podany Size jest mniejszy from 1.
	 */
	public void addList(int iSize) throws ParameterException
		{
		if (iSize < 1)
			throw new ParameterException("iSize = " + iSize);
		oListInt.add(iSize);
		oJListList.addElement(JListShipListSettings.shipName(iSize));
		}
	/**
	 * Zmienia Size statku o podanym indexie.
	 * 
	 * @param iIndex Index statku na liscie (liczony from 0).
	 * @param iSize Nowy Size statku.
	 * @throws ParameterException Wyrzuca wyjatek, jesli index jest poza zakresem istniejacej listy statkow, lub Size jest mniejszy from 1.
	 */
	public void changeList(int iIndex, int iSize) throws ParameterException
		{
		if (iIndex < 0 || iIndex >= oListInt.size())
			throw new ParameterException("iIndex = " + iIndex);
		if (iSize < 1)
			throw new ParameterException("iSize = " + iSize);
		oListInt.set(iIndex, iSize);
		oJListList.set(iIndex, JListShipListSettings.shipName(iSize));
		}
	/**
	 * Powieksza o 1 Size statku o podanym indexie.
	 * 
	 * @param iIndex Index statku na liscie (counted from 0).
	 * @throws ParameterException Wyrzuca wyjatek, jesli index jest poza zakresem istniejacej listy statkow.
	 */
	public void zoomList(int iIndex) throws ParameterException
		{
		if (iIndex < 0 || iIndex >= oListInt.size())
			throw new ParameterException("iIndex = " + iIndex);
		oListInt.set(iIndex, oListInt.get(iIndex) + 1);
		oJListList.set(iIndex, JListShipListSettings.shipName(oListInt.get(iIndex)));
		}
	/**
	 * Pomniejsza o 1 Size statku o podanym indexie.
	 * 
	 * @param iIndex Index statku na liscie (counted from 0).
	 * @throws ParameterException Wyrzuca wyjatek, jesli index jest poza zakresem istniejacej listy statkow.
	 */
	public void zoomOutList(int iIndex) throws ParameterException
		{
		if (iIndex < 0 || iIndex >= oListInt.size())
			throw new ParameterException("iIndex = " + iIndex);
		if (oListInt.get(iIndex) > 1)
			{
			oListInt.set(iIndex, oListInt.get(iIndex) - 1);
			oJListList.set(iIndex, JListShipListSettings.shipName(oListInt.get(iIndex)));
			}
		}
	/**
	 * Usuwa z listy statek o podanym indexie.
	 * 
	 * @param iIndex Index statku na liscie (counted from 0).
	 * @throws ParameterException Wyrzuca wyjatek, jesli index jest poza zakresem istniejacej listy statkow.
	 */
	public void deleteLista(int iIndex) throws ParameterException
		{
		if (iIndex < 0 || iIndex >= oListInt.size())
			throw new ParameterException("iIndex = " + iIndex);
		oListInt.remove(iIndex);
		oJListList.remove(iIndex);
		}
	/**
	 * Usuwa z listy wszystkie statki.
	 */
	public void clearList()
		{
		oListInt.clear();
		oJListList.clear();
		}
	/**
	 * Metoda generujaca nazwe statku do wyswietlania na liscie na podstawie podanego Sizeu.
	 * 
	 * @param iSize Size statku.
	 * @return Nazwa statku.
	 */
	private static String shipName(int iSize) throws ParameterException
		{
		if (iSize < 1)
			throw new ParameterException("iSize = " + iSize);
		int iShipClass = iSize > 5 ? 5 : iSize;
		return JFrameGameWindowSettings.LANG.getProperty("shipName.size" + iShipClass) + " ( " + iSize + " )";
		}
	}
