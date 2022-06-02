package pl.vgtworld.games.statki.ai;

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
 * - dodanie metody metody {@link #setProsteLinie(boolean)}<br />
 * </p>
 * 
 * @author VGT
 * @version 1.1
 */
public abstract class AiGeneric
	{
	/**
	 * Kontener statkow nalezacych do gracza sterowanego przez komputer
	 * (uzyteczne w bardziej rozbudowanych wersjach AI do ustalania, jak daleko od potencjalnej przegranej jest komputer).
	 */
	protected ShipIterator oShips;
	/**
	 * Przechowuje wspolrzedne ostatniego celnego shotu.<br />
	 * 
	 * Wartosc tego pola nalezy uzupelniac we wszystkich metodach oddajacych shot na plansze przeciwnika.
	 */
	protected Position oOstatnieTrafienie;
	/**
	 * Kontener wykorzystywany do przechowywania wspolrzednych dla udanych trafien w poprzednich rundach.
	 * 
	 * Na jego podstawie mozliwe jest szukanie kolejnych pol trafionego statku w celu jego dalszego oshotu.
	 */
	protected ArrayList<Position> oUzyteczneHits;
	/**
	 * Wlasciwosc okresla, czy statki na planszy moga byc tylko pionowymi/poziomymi liniami (TRUE),
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
		oOstatnieTrafienie = new Position(2);
		oOstatnieTrafienie.setX(-1);
		oOstatnieTrafienie.setY(-1);
		oUzyteczneHits = new ArrayList<Position>();
		}
	/**
	 * Metoda pozwala ustawic wlasciwosc okreslajaca dozwolny ksztalt statkow.
	 * 
	 * @param bWartosc Okresla, czy statki moga byc tylko pionowymi/poziomymi liniami.
	 * @since 1.1
	 */
	public void setProsteLinie(boolean bWartosc)
		{
		bStraightLines = bWartosc;
		}
	/**
	 * Najprostrza mozliwa implementacja wyboru pola do ostrzelania. Metoda wyszukuje wszystkie pola, na ktore mozna oddac shot
	 * i losowo wybiera jedno z nich.<br />
	 * 
	 * Informacje na temat wspolrzednych oddanego shotu sa przekazywane do metody shot() kontenera
	 * statkow przekazanego w parametrze i tam jest zrealizowana pelna obsluga shotu.
	 * 
	 * @param oShipsPrzeciwnika Kontener statkow przeciwnika, ktory ma byc ostrzelany.
	 * @return Zwraca TRUE w przypadku Hits ktoregos ze statkow, lub FALSE, jesli shot byl niecelny.
	 */
	protected boolean shotLosowy(ShipIterator oShipsPrzeciwnika)
		{
		try
			{
			Position oWylosowanePole = losujPole(oShipsPrzeciwnika.getBoard());
			boolean bHit = oShipsPrzeciwnika.shot(oWylosowanePole.getX(), oWylosowanePole.getY());
			if (bHit == true)
				{
				//zapisanie celnego shotu w tablicy trafien
				Position oTrafienie = new Position(2);
				oTrafienie.setX(oWylosowanePole.getX());
				oTrafienie.setY(oWylosowanePole.getY());
				oUzyteczneHits.add(oTrafienie);
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
	 * i powtarza proces. Jesli wyczerpane zostana zapisane Hits, wywolywana jest metoda shotLosowy()
	 * 
	 * @param oShipsPrzeciwnika Kontener statkow przeciwnika.
	 * @return Zwraca TRUE, w przypadku Hits statku, lub FALSE, jesli shot byl niecelny.
	 */
	protected boolean shotSasiadujacy(ShipIterator oShipsPrzeciwnika)
		{
		//przygotowanie kontenera przechowujacego do 4 sasiednich pol, ktore nadaja sie do kolejnego shotu
		ArrayList<Position> oSasiedniePola = new ArrayList<Position>(4);
		//petla wyszukujaca we wczesniejszych Hitsch pola do oddania kolejnego shotu
		while (oUzyteczneHits.size() > 0)
			{
			//wylosowanie pola do przetestowania
			int iLosowanePole = oRand.nextInt(oUzyteczneHits.size());
			Position oWybranePole = oUzyteczneHits.get(iLosowanePole);
			
			try
				{
				//wczytanie wspolrzednych 4 sasiadow i sprawdzenie, czy sa to pola puste, lub zawierajace statek
				for (int i = -1; i <= 1; ++i)
					for (int j = -1; j <= 1; ++j)
						if (
							oWybranePole.getX() + i >= 0 && oWybranePole.getX() + i < oShipsPrzeciwnika.getBoard().getWidth()
							&& oWybranePole.getY() + j >= 0 && oWybranePole.getY() + j < oShipsPrzeciwnika.getBoard().getHeight()
							&& (i + j == -1 || i + j == 1)
							)
							{
							if (oShipsPrzeciwnika.getBoard().getField(oWybranePole.getX() + i, oWybranePole.getY() + j) != FieldTypeBoard.BOARD_FIELD_EMPTY
								&& oShipsPrzeciwnika.getBoard().getField(oWybranePole.getX() + i, oWybranePole.getY() + j) != FieldTypeBoard.SHIP_BOARD
								)
								{
								} else {
                                                            Position oPrawidlowe = new Position(2);
                                                            oPrawidlowe.setX(oWybranePole.getX() + i);
                                                            oPrawidlowe.setY(oWybranePole.getY() + j);
                                                            oSasiedniePola.add(oPrawidlowe);
                                    }
							}
				
				if (bStraightLines == true)
					{
					boolean bVerticalowy = false;
					boolean bLevely = false;
					for (int i = -1; i <= 1; ++i)
						for (int j = -1; j <= 1; ++j)
							if (
								oWybranePole.getX() + i >= 0 && oWybranePole.getX() + i < oShipsPrzeciwnika.getBoard().getWidth()
								&& oWybranePole.getY() + j >= 0 && oWybranePole.getY() + j < oShipsPrzeciwnika.getBoard().getHeight()
								&& (i + j == -1 || i + j == 1)
								)
								{
								if (oShipsPrzeciwnika.getBoard().getField(oWybranePole.getX() + i, oWybranePole.getY() + j) == FieldTypeBoard.CUSTOMS_SHOT_BOARD)
									{
									if (i == 0)
										bVerticalowy = true;
									if (j == 0)
										bLevely = true;
									}
								}
					if (bVerticalowy == true && bLevely == true)
						throw new DeveloperException();
					if (bVerticalowy == true)
						{
						for (int i = oSasiedniePola.size() - 1; i >= 0; --i)
							if (oSasiedniePola.get(i).getX() != oWybranePole.getX())
								oSasiedniePola.remove(i);
						}
					if (bLevely == true)
						{
						for (int i = oSasiedniePola.size() - 1; i >= 0; --i)
							if (oSasiedniePola.get(i).getY() != oWybranePole.getY())
								oSasiedniePola.remove(i);
						}
					}
				
				if (!oSasiedniePola.isEmpty())
					{
					//sa pola prawidlowe do oddania kolejnego shotu
					int iWylosowanySasiad = oRand.nextInt(oSasiedniePola.size());
					//oddanie shotu na wspolrzedne weybranego pola
					boolean bshot;
					bshot = oShipsPrzeciwnika.shot(oSasiedniePola.get(iWylosowanySasiad).getX(), oSasiedniePola.get(iWylosowanySasiad).getY());
					if (bshot == true)
						{
						//zapisanie celnego shotu w tablicy trafien
						Position oTrafienie = new Position(2);
						oTrafienie.setX( oSasiedniePola.get(iWylosowanySasiad).getX() );
						oTrafienie.setY( oSasiedniePola.get(iWylosowanySasiad).getY() );
						oUzyteczneHits.add(oTrafienie);
						}
					return bshot;
					}
				else
					{
					//brak prawidlowych pol. usuniecie Hits z listy i przejscie do kolejnej iteracji petli wyszukujacej
					oUzyteczneHits.remove(iLosowanePole);
					}
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
			
			}
		return shotLosowy(oShipsPrzeciwnika);
		}
	/**
	 * Metoda wyszukuje losowo na planszy pole do oddania shotu, jednak jesli wylosowane pole nie zawiera statku,
	 * nastepuje ponowne losowanie w celu znalezienia lepszego pola do shotu. Dozwolona ilosc powtorzen okresla
	 * drugi parametr.<br />
	 * 
	 * Jesli w ktorejkolwiek iteracji nastapi wylosowanie pola zawierajacego statek, shot uznaje sie za trafiony
	 * i nie sa wykonywane kolejne iteracje petli.<br />
	 * 
	 * Jesli w ostatniej iteracji takze zostanie wylosowane pole puste,
	 * wspolrzedne tego pola zostaje uznane za wykonany shot i jest on niecelny.
	 * 
	 * @param oShipsPrzeciwnika Kontener statkow przeciwnika.
	 * @param iQuantityPowtorzen Dozwolona ilosc powtorzen losowania pola do oshotu.
	 * @return Zwraca TRUE, w przypadku Hits statku, lub FALSE, jesli shot byl niecelny.
	 */
	protected boolean shotWielokrotny(ShipIterator oShipsPrzeciwnika, int iQuantityPowtorzen)
		{
		try
			{
			Position oWylosowanePole = null;
			Board oBoard = oShipsPrzeciwnika.getBoard();
			for (int i = 1; i <= iQuantityPowtorzen; ++i)
				{
				oWylosowanePole = losujPole(oBoard);
				if (oBoard.getField(oWylosowanePole.getX(), oWylosowanePole.getY()) == FieldTypeBoard.SHIP_BOARD || i == iQuantityPowtorzen)
					{
					boolean bshot;
					bshot = oShipsPrzeciwnika.shot(oWylosowanePole.getX(), oWylosowanePole.getY());
					if (bshot == true)
						{
						//zapisanie celnego shotu w tablicy trafien
						Position oTrafienie = new Position(2);
						oTrafienie.setX( oWylosowanePole.getX() );
						oTrafienie.setY( oWylosowanePole.getY() );
						oUzyteczneHits.add(oTrafienie);
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
	 * Metoda wybiera losowe pole dostepne do ostrzelania na planszy przeciwnika i zwraca obiekt typu Position zawierajacy te wspolrzedne.
	 * 
	 * @param oBoardPrzeciwnika Board przeciwnika, na ktora ma byc oddany shot.
	 * @return Wspolrzedne wylosowanego pola do ostrzelania.
	 */
	private Position losujPole(Board oBoardPrzeciwnika)
		{
		try
			{
			Position oWylosowanePole = new Position(2);
			int iRandomlyDrawnField = oRand.nextInt( oBoardPrzeciwnika.getUnknownQuantity() ) + 1;
			//obliczenie x i y dla wylosowanego pola
			int iX = 0;
			int iY = 0;
			while (iRandomlyDrawnField > 0)
				{
				if (oBoardPrzeciwnika.getField(iX, iY) == FieldTypeBoard.BOARD_FIELD_EMPTY || oBoardPrzeciwnika.getField(iX, iY) == FieldTypeBoard.SHIP_BOARD)
					--iRandomlyDrawnField;
				if (iRandomlyDrawnField > 0)
					{
					++iX;
					if (iX == oBoardPrzeciwnika.getWidth())
						{
						++iY;
						iX = 0;
						}
					}
				}
			oWylosowanePole.setX(iX);
			oWylosowanePole.setY(iY);
			return oWylosowanePole;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	}
