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
	private ArrayList<Integer> oListaInt;
	/**
	 * Model listy przechowujacy Sizey statkow.
	 */
	private DefaultListModel oJListLista;
	/**
	 * Konstruktor domyslny.
	 */
	public JListShipListSettings()
		{
		oListaInt = new ArrayList<Integer>();
		oJListLista = new DefaultListModel();
		setModel(oJListLista);
		}
	/**
	 * Zwraca ilosc statkow przechowywana na liscie.
	 * 
	 * @return Ilosc statkow na liscie.
	 */
	public int getNumberOfShips()
		{
		return oListaInt.size();
		}
	/**
	 * Zwraca tablice int zawierajaca Size wszystkich statkow przechowywanych w liscie.
	 * 
	 * @return Tablica z Sizeami statkow przechowywanych w liscie.
	 */
	public int[] getListaStatkow()
		{
		int[] aLista = new int[ oListaInt.size() ];
		for (int i = 0; i < oListaInt.size(); ++i)
			aLista[i] = oListaInt.get(i);
		return aLista;
		}
	/**
	 * Dodaje do listy statek o podanym Sizeze.
	 * 
	 * @param iSize Size dodawanego statku.
	 * @throws ParameterException Wyrzuca wyjatek, jesli podany Size jest mniejszy od 1.
	 */
	public void listaDodaj(int iSize) throws ParameterException
		{
		if (iSize < 1)
			throw new ParameterException("iSize = " + iSize);
		oListaInt.add(iSize);
		oJListLista.addElement(JListShipListSettings.statekNazwa(iSize));
		}
	/**
	 * Zmienia Size statku o podanym indexie.
	 * 
	 * @param iIndex Index statku na liscie (liczony od 0).
	 * @param iSize Nowy Size statku.
	 * @throws ParameterException Wyrzuca wyjatek, jesli index jest poza zakresem istniejacej listy statkow, lub Size jest mniejszy od 1.
	 */
	public void listaZmien(int iIndex, int iSize) throws ParameterException
		{
		if (iIndex < 0 || iIndex >= oListaInt.size())
			throw new ParameterException("iIndex = " + iIndex);
		if (iSize < 1)
			throw new ParameterException("iSize = " + iSize);
		oListaInt.set(iIndex, iSize);
		oJListLista.set(iIndex, JListShipListSettings.statekNazwa(iSize));
		}
	/**
	 * Powieksza o 1 Size statku o podanym indexie.
	 * 
	 * @param iIndex Index statku na liscie (liczone od 0).
	 * @throws ParameterException Wyrzuca wyjatek, jesli index jest poza zakresem istniejacej listy statkow.
	 */
	public void listaPowieksz(int iIndex) throws ParameterException
		{
		if (iIndex < 0 || iIndex >= oListaInt.size())
			throw new ParameterException("iIndex = " + iIndex);
		oListaInt.set(iIndex, oListaInt.get(iIndex) + 1);
		oJListLista.set(iIndex, JListShipListSettings.statekNazwa(oListaInt.get(iIndex)));
		}
	/**
	 * Pomniejsza o 1 Size statku o podanym indexie.
	 * 
	 * @param iIndex Index statku na liscie (liczone od 0).
	 * @throws ParameterException Wyrzuca wyjatek, jesli index jest poza zakresem istniejacej listy statkow.
	 */
	public void listaPomniejsz(int iIndex) throws ParameterException
		{
		if (iIndex < 0 || iIndex >= oListaInt.size())
			throw new ParameterException("iIndex = " + iIndex);
		if (oListaInt.get(iIndex) > 1)
			{
			oListaInt.set(iIndex, oListaInt.get(iIndex) - 1);
			oJListLista.set(iIndex, JListShipListSettings.statekNazwa(oListaInt.get(iIndex)));
			}
		}
	/**
	 * Usuwa z listy statek o podanym indexie.
	 * 
	 * @param iIndex Index statku na liscie (liczone od 0).
	 * @throws ParameterException Wyrzuca wyjatek, jesli index jest poza zakresem istniejacej listy statkow.
	 */
	public void listaUsun(int iIndex) throws ParameterException
		{
		if (iIndex < 0 || iIndex >= oListaInt.size())
			throw new ParameterException("iIndex = " + iIndex);
		oListaInt.remove(iIndex);
		oJListLista.remove(iIndex);
		}
	/**
	 * Usuwa z listy wszystkie statki.
	 */
	public void listaWyczysc()
		{
		oListaInt.clear();
		oJListLista.clear();
		}
	/**
	 * Metoda generujaca nazwe statku do wyswietlania na liscie na podstawie podanego Sizeu.
	 * 
	 * @param iSize Size statku.
	 * @return Nazwa statku.
	 */
	private static String statekNazwa(int iSize) throws ParameterException
		{
		if (iSize < 1)
			throw new ParameterException("iSize = " + iSize);
		int iKlasaStatku = iSize > 5 ? 5 : iSize;
		return JFrameGameWindowSettings.LANG.getProperty("shipName.size" + iKlasaStatku) + " ( " + iSize + " )";
		}
	}
