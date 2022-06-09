package pl.vgtworld.games.ship.ai;

import java.util.ArrayList;
import java.util.Random;
import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.games.ship.Board;
import pl.vgtworld.games.ship.FieldTypeBoard;
import pl.vgtworld.games.ship.ShipIterator;
import pl.vgtworld.tools.Position;

/**
 * Klasa abstrakcyjna zawierajaca zestaw metod uzytecznych do budowania klas sztucznej inteligencji.<br />
 *
 * <p>
 * aktualizacje:<br />
 * 1.1<br />
 * - dodanie metody metody {@link #setStraightLines(boolean)}<br />
 * </p>
 * 
 * @author VGT
 * @version 1.1
 */
public abstract class AiGeneric
	{
	/**
	 * Kontener statkow nalezacych do gracza sterowanego przez komputer
	 * (uzyteczne w bardziej rozbudowanych wersjach AI do ustalania, jak daleko from potencjalnej przegranej jest komputer).
	 */
	protected ShipIterator oShips;
	/**
	 * Przechowuje co-ordinates ostatniego celnego shotu.<br />
	 * 
	 * Wartosc tego position nalezy uzupelniac we wszystkich metodach oddajacych shot na plansze przeciwnika.
	 */
	protected Position oLastHit;
	/**
	 * Kontener wykorzystywany do przechowywania wspolrzednych dla udanych trafien w poprzednich rundach.
	 * 
	 * Na jego podstawie mozliwe jest szukanie kolejnych pol trafionego statku w celu jego dalszego oshotu.
	 */
	protected ArrayList<Position> oUsefulHits;
	/**
	 * Wlasciwosc okresla, czy statki na board moga byc tylko pionowymi/poziomymi liniami (TRUE),
	 * czy tez moga miec dowolne ksztalty (FALSE, domyslnie).
	 */
	protected boolean bStraightLines;
	/**
	 * Generator liczb losowych.
	 */
	protected Random oRand;
	/**
	 * Konstruktor.
	 * 
	 * @param oShips Kontener statkow nalezacych do gracza sterowanego przez dany obiekt Ai.
	 */
	public AiGeneric(ShipIterator oShips)
		{
		this.oShips = oShips;
		bStraightLines = false;
		oRand = new Random();
		oLastHit = new Position(2);
		oLastHit.setX(-1);
		oLastHit.setY(-1);
		oUsefulHits = new ArrayList<Position>();
		}
	/**
	 * Metoda pozwala ustawic wlasciwosc okreslajaca dozwolny ksztalt statkow.
	 * 
	 * @param bValue Okresla, czy statki moga byc tylko pionowymi/poziomymi liniami.
	 * @since 1.1
	 */
	public void setStraightLines(boolean bValue)
		{
		bStraightLines = bValue;
		}
	/**
	 * Najprostrza mozliwa implementacja wyboru position do ostrzelania. Metoda wyszukuje wszystkie position, na ktore mozna oddac shot
	 * i losowo wybiera jedno z nich.<br />
	 * 
	 * Informacje na temat wspolrzednych oddanego shotu sa przekazywane do metody shot() kontenera
	 * statkow przekazanego w parametrze i tam jest zrealizowana pelna obsluga shotu.
	 * 
	 * @param oOpponentShip Kontener statkow przeciwnika, ktory ma byc ostrzelany.
	 * @return Zwraca TRUE w przypadku Hits ktoregos ze statkow, lub FALSE, jesli shot byl niecelny.
	 */
	protected boolean shotRandom(ShipIterator oOpponentShip)
		{
		try
			{
			Position oRandomlyDrawnField = drawField(oOpponentShip.getBoard());
			boolean bHit = oOpponentShip.shot(oRandomlyDrawnField.getX(), oRandomlyDrawnField.getY());
			if (bHit == true)
				{
				//zapisanie celnego shotu w tablicy trafien
				Position oHit = new Position(2);
				oHit.setX(oRandomlyDrawnField.getX());
				oHit.setY(oRandomlyDrawnField.getY());
				oUsefulHits.add(oHit);
				}
			return bHit;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Metoda wybiera losowo jedno z zapisanych wczesniejszych trafien i sprawdza, czy mozna ostrzelac ktores z sasiadujacych pol.<br />
	 * 
	 * Jesli tak, wybiera jedno z pol do ostrzelania. Jesli nie, usuwa pole z listy, wybiera losowo kolejne zapisane trafienie
	 * i powtarza proces. Jesli wyczerpane zostana zapisane Hits, wywolywana jest metoda shotRandom()
	 * 
	 * @param oOpponentShip Kontener statkow przeciwnika.
	 * @return Zwraca TRUE, w przypadku Hits statku, lub FALSE, jesli shot byl niecelny.
	 */
	protected boolean shotNeighbor(ShipIterator oOpponentShip)
		{
		//przygotowanie kontenera przechowujacego do 4 sasiednich pol, ktore nadaja sie do kolejnego shotu
		ArrayList<Position> oNeighboringFields = new ArrayList<Position>(4);
		//petla wyszukujaca we wczesniejszych Hitsch position do oddania kolejnego shotu
		while (oUsefulHits.size() > 0)
			{
			//wylosowanie position do przetestowania
			int iRandomField = oRand.nextInt(oUsefulHits.size());
			Position oSelectedField = oUsefulHits.get(iRandomField);
			
			try
				{
				//wczytanie wspolrzednych 4 sasiadow i sprawdzenie, czy sa to position puste, lub zawierajace statek
				for (int i = -1; i <= 1; ++i)
					for (int j = -1; j <= 1; ++j)
						if (
							oSelectedField.getX() + i >= 0 && oSelectedField.getX() + i < oOpponentShip.getBoard().getWidth()
							&& oSelectedField.getY() + j >= 0 && oSelectedField.getY() + j < oOpponentShip.getBoard().getHeight()
							&& (i + j == -1 || i + j == 1)
							)
							{
							if (oOpponentShip.getBoard().getField(oSelectedField.getX() + i, oSelectedField.getY() + j) != FieldTypeBoard.BOARD_FIELD_EMPTY
								&& oOpponentShip.getBoard().getField(oSelectedField.getX() + i, oSelectedField.getY() + j) != FieldTypeBoard.SHIP_BOARD
								)
								{
								} else {
                                                            Position oCorrect = new Position(2);
                                                            oCorrect.setX(oSelectedField.getX() + i);
                                                            oCorrect.setY(oSelectedField.getY() + j);
                                                            oNeighboringFields.add(oCorrect);
                                    }
							}
				
				if (bStraightLines == true)
					{
					boolean bVertical = false;
					boolean bLevel = false;
					for (int i = -1; i <= 1; ++i)
						for (int j = -1; j <= 1; ++j)
							if (
								oSelectedField.getX() + i >= 0 && oSelectedField.getX() + i < oOpponentShip.getBoard().getWidth()
								&& oSelectedField.getY() + j >= 0 && oSelectedField.getY() + j < oOpponentShip.getBoard().getHeight()
								&& (i + j == -1 || i + j == 1)
								)
								{
								if (oOpponentShip.getBoard().getField(oSelectedField.getX() + i, oSelectedField.getY() + j) == FieldTypeBoard.CUSTOMS_SHOT_BOARD)
									{
									if (i == 0)
										bVertical = true;
									if (j == 0)
										bLevel = true;
									}
								}
					if (bVertical == true && bLevel == true)
						throw new DeveloperException();
					if (bVertical == true)
						{
						for (int i = oNeighboringFields.size() - 1; i >= 0; --i)
							if (oNeighboringFields.get(i).getX() != oSelectedField.getX())
								oNeighboringFields.remove(i);
						}
					if (bLevel == true)
						{
						for (int i = oNeighboringFields.size() - 1; i >= 0; --i)
							if (oNeighboringFields.get(i).getY() != oSelectedField.getY())
								oNeighboringFields.remove(i);
						}
					}
				
				if (!oNeighboringFields.isEmpty())
					{
					//sa position prawidlowe do oddania kolejnego shotu
					int iRandomlyDrawnNeighbor = oRand.nextInt(oNeighboringFields.size());
					//oddanie shotu na co-ordinates weybranego position
					boolean bshot;
					bshot = oOpponentShip.shot(oNeighboringFields.get(iRandomlyDrawnNeighbor).getX(), oNeighboringFields.get(iRandomlyDrawnNeighbor).getY());
					if (bshot == true)
						{
						//zapisanie celnego shotu w tablicy trafien
						Position oHit = new Position(2);
						oHit.setX( oNeighboringFields.get(iRandomlyDrawnNeighbor).getX() );
						oHit.setY( oNeighboringFields.get(iRandomlyDrawnNeighbor).getY() );
						oUsefulHits.add(oHit);
						}
					return bshot;
					}
				else
					{
					//brak prawidlowych pol. usuniecie Hits z listy i przejscie do kolejnej iteracji petli wyszukujacej
					oUsefulHits.remove(iRandomField);
					}
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
			
			}
		return shotRandom(oOpponentShip);
		}
	/**
	 * Metoda wyszukuje losowo na board pole do oddania shotu, jednak jesli wylosowane pole nie zawiera statku,
	 * nastepuje ponowne losowanie w celu znalezienia lepszego position do shotu. Dozwolona ilosc powtorzen okresla
	 * drugi parametr.<br />
	 * 
	 * Jesli w ktorejkolwiek iteracji nastapi wylosowanie position zawierajacego statek, shot uznaje sie za trafiony
	 * i nie sa wykonywane kolejne iteracje petli.<br />
	 * 
	 * Jesli w ostatniej iteracji takze zostanie wylosowane pole puste,
	 * co-ordinates tego position zostaje uznane za wykonany shot i jest on niecelny.
	 * 
	 * @param oOpponentShip Kontener statkow przeciwnika.
	 * @param iRepeatQuantity Dozwolona ilosc powtorzen losowania position do oshotu.
	 * @return Zwraca TRUE, w przypadku Hits statku, lub FALSE, jesli shot byl niecelny.
	 */
	protected boolean multipleShot(ShipIterator oOpponentShip, int iRepeatQuantity)
		{
		try
			{
			Position oRandomlyDrawnField = null;
			Board oBoard = oOpponentShip.getBoard();
			for (int i = 1; i <= iRepeatQuantity; ++i)
				{
				oRandomlyDrawnField = drawField(oBoard);
				if (oBoard.getField(oRandomlyDrawnField.getX(), oRandomlyDrawnField.getY()) == FieldTypeBoard.SHIP_BOARD || i == iRepeatQuantity)
					{
					boolean bshot;
					bshot = oOpponentShip.shot(oRandomlyDrawnField.getX(), oRandomlyDrawnField.getY());
					if (bshot == true)
						{
						//zapisanie celnego shotu w tablicy trafien
						Position oHit = new Position(2);
						oHit.setX( oRandomlyDrawnField.getX() );
						oHit.setY( oRandomlyDrawnField.getY() );
						oUsefulHits.add(oHit);
						}
					return bshot;
					}
				}
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		//petla musi zwrocic shot. skoro doszlo tutaj - wywal wyjatek
		throw new DeveloperException();
		}
	/**
	 * Metoda wybiera losowe pole dostepne do ostrzelania na board przeciwnika i zwraca obiekt typu Position zawierajacy te co-ordinates.
	 * 
	 * @param oOpponentBoard Board przeciwnika, na ktora ma byc oddany shot.
	 * @return co-ordinates wylosowanego position do ostrzelania.
	 */
	private Position drawField(Board oOpponentBoard)
		{
		try
			{
			Position oRandomlyDrawnField = new Position(2);
			int iRandomlyDrawnField = oRand.nextInt( oOpponentBoard.getUnknownQuantity() ) + 1;
			//obliczenie x i y dla wylosowanego position
			int iX = 0;
			int iY = 0;
			while (iRandomlyDrawnField > 0)
				{
				if (oOpponentBoard.getField(iX, iY) == FieldTypeBoard.BOARD_FIELD_EMPTY || oOpponentBoard.getField(iX, iY) == FieldTypeBoard.SHIP_BOARD)
					--iRandomlyDrawnField;
				if (iRandomlyDrawnField > 0)
					{
					++iX;
					if (iX == oOpponentBoard.getWidth())
						{
						++iY;
						iX = 0;
						}
					}
				}
			oRandomlyDrawnField.setX(iX);
			oRandomlyDrawnField.setY(iY);
			return oRandomlyDrawnField;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	}
