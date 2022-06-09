package pl.vgtworld.games.ship;

import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.tools.Position;

/**
 * Klasa zajmujaca sie utworzeniem obiektu kontenera statkow na podstawie dostarczonej board z zaznaczonymi positionmi statkow.
 * 
 * @author VGT
 * @version 1.0
 */
public class ShipGenerator
	{
	/**
	 * Board z naniesionym rozmieszczeniem statkow.<br />
	 * 
	 * Docelowo zostanie ona wykorzystana, jako plansza obiektu kontenera statkow.
	 */
	private Board oBoard;
	/**
	 * Kontener statkow, ktory zostanie utworzony na podstawie dostarczonej board.
	 */
	private ShipIterator oShips;
	/**
	 * Tablica pomocnicza wczytujaca pozycje wszystkich zaznaczonych pol na board.
	 */
	private Position[] aShipSpaces;
	/**
	 * Aktualna ilosc pol przechowywana w tablicy aShipSpaces.
	 */
	private int iNumberOfShipsField;
	/**
	 * Konstruktor domyslny.
	 * 
	 * @param oBoard Board z rozmieszczonymi statkami.
	 */
	public ShipGenerator(Board oBoard)
		{
		this.oBoard = oBoard;
		oShips = null;
		aShipSpaces = new Position[0];
		iNumberOfShipsField = 0;
		}
	/**
	 * Glowna metoda rozpoczynajaca proces tworzenia kontenera statkow na podstawie board dostarczonej w konstruktorze.
	 * 
	 * @return Zwraca stworzony kontener statkow.
	 */
	public ShipIterator generateShips()
		{
		try
			{
			//wyszukanie na board oznaczonych pol i wyczyszczenie board
			findField();
			//utworzenie kontenera statkow
			oShips = new ShipIterator(oBoard);
			int iNumberOfShips = 0;
			//wypelnienie kontenera statkami
			while (iNumberOfShipsField > 0)
				{
				Position[] aShip = generateShip();
				oShips.addAShip( aShip.length );
				++iNumberOfShips;
				for (int i = 0; i < aShip.length; ++i)
					oShips.getShip(iNumberOfShips).setField(i+1, aShip[i].getX(), aShip[i].getY());
				}
			//zwrocenie kontenera
			return this.oShips;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Metoda wyszukuje na board position statkow.<br />
	 * 
	 * Liste ich pozycji zapisuje do tablicy aShipSpaces, a ilosc pol do iNumberOfShipsField.
	 * Na koniec czysci takze plansze z oznaczonych pol, aby przygotowac ja do dzialania w ramach tworzonego obiektu kontenera statkow.
	 */
	private void findField()
		{
		try
			{
			aShipSpaces = new Position[0];
			iNumberOfShipsField = 0;
			for (int i = 0; i < oBoard.getWidth(); ++i)
				for (int j = 0; j < oBoard.getHeight(); ++j)
					if (oBoard.getField(i, j) == FieldTypeBoard.SHIP_BOARD)
						{
						Position[] aNewList = new Position[ iNumberOfShipsField + 1 ];
						//przepisanie dotychczasowej listy
						for (int k = 0; k < iNumberOfShipsField; k++)
							aNewList[k] = aShipSpaces[k];
						//dopisanie nowego elementu na koncu
						Position oObj = new Position(2);
						oObj.setX(i);
						oObj.setY(j);
						aNewList[iNumberOfShipsField] = oObj;
						++iNumberOfShipsField;
						aShipSpaces = aNewList;
						}
			//zamazanie pol na board
			for (int i = 0; i < aShipSpaces.length; ++i)
				oBoard.setField(aShipSpaces[i].getX(), aShipSpaces[i].getY(), FieldTypeBoard.BOARD_FIELD_EMPTY);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Metoda pobiera i usuwa position z tablicy aShipSpaces starajac sie wygenerowac liste pol pojedynczego statku.<br />
	 * 
	 * Po pobraniu pierwszego position skanuje liste pozostalych tak dlugo, az uzyska tablice zawierajaca wszystkie positionczone ze soba position.
	 * 
	 * @return Zwraca tablice zawierajaca liste pol dla jednego statku.
	 */
	private Position[] generateShip()
		{
		if (aShipSpaces.length == 0)
			throw new DeveloperException("No fields on the list");
		try
			{
			//utworzenie tablicy mogacej przechowac liste wszystkich pol aktualnie znajdujacych sie na board
			//(w przyszlosci mozna przerobic na kontener)
			int iSize = 0;
			Position[] aField = new Position[ aShipSpaces.length ];
			//pobranie pierwszego position z board
			aField[ iSize++ ] =  getField(0);
			//petla pobierajaca kolejne position dopoki jakies sasiadujace sa znajdowane
			boolean bNewNeighbor = true;
			while (bNewNeighbor == true)
				{
				bNewNeighbor = false;
				for (int i = 0; i < iSize; ++i)
					{
					int iNumberOfSearchedNeighbor = findNeighbor(aField[i]);
					if (iNumberOfSearchedNeighbor != -1)
						{
						aField[ iSize++ ] = getField(iNumberOfSearchedNeighbor);
						bNewNeighbor = true;
						}
					}
				}
			//utworzenie nowej tablicy o Sizeach przycietych do znalezionego statku, przepisanie do niej pol i return
			Position[] aReturn = new Position[iSize];
			for (int i = 0; i < aReturn.length; ++i)
				aReturn[i] = aField[i];
			return aReturn;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Metoda sprawdza, czy na liscie pol jest pole sasiadujace z przekazanym w parametrze.<br />
	 * 
	 * Jesli tak, zwraca jego index w tablicy aShipSpaces.
	 * Jesli wystepuje wiecej takich pol, zwrocony zostanie index pierwszego znalezionego position.
	 * Jesli podane position nie ma sasiadow, zostanie zwrocona wartosc -1.
	 * 
	 * @param oPosition co-ordinates position, dla ktorego nalezy szukac sasiadow.
	 * @return Zwraca index position sasiadujacego, lub -1, jesli nie znaleziono zadnego.
	 */
	private int findNeighbor(Position oPosition)
		{
		for (int i = 0; i < aShipSpaces.length; ++i)
			{
			if (
				(aShipSpaces[i].getX() == oPosition.getX() - 1 && aShipSpaces[i].getY() == oPosition.getY()) ||
				(aShipSpaces[i].getX() == oPosition.getX() + 1 && aShipSpaces[i].getY() == oPosition.getY()) ||
				(aShipSpaces[i].getX() == oPosition.getX() && aShipSpaces[i].getY() == oPosition.getY() - 1) ||
				(aShipSpaces[i].getX() == oPosition.getX() && aShipSpaces[i].getY() == oPosition.getY() + 1)
				)
				return i;
			}
		return -1;
		}
	/**
	 * Metoda usuwa z listy pol w aShipSpaces element o podanym indexie i zwraca go.
	 * 
	 * @param iIndex Index position do usuniecia.
	 * @return Zwraca usuniete pole.
	 * @throws ParameterException Wyrzuca wyjatek, jesli index znajduje sie poza dostepnym zakresem pol.
	 */
	private Position getField(int iIndex) throws ParameterException
		{
		if (iIndex >= iNumberOfShipsField || iIndex < 0)
			throw new ParameterException("iIndex = " + iIndex);
		Position[] aNewList = new Position[iNumberOfShipsField - 1];
		//przepisanie elementow do nowej tablicy z pominieciem usuwanego
		int iCounter = 0;
		for (int i = 0; i < iNumberOfShipsField; ++i)
			if (i != iIndex)
				{
				aNewList[iCounter] = aShipSpaces[i];
				++iCounter;
				}
		//zapisanie elementu do usuniecia i podmiana tablicy pol w obiekcie
		Position oReturn = aShipSpaces[iIndex];
		aShipSpaces = aNewList;
		--iNumberOfShipsField;
		
		return oReturn;
		}
	}
