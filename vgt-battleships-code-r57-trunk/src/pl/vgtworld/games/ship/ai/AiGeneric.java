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
 * Abstract class containing a set of methods useful for building artificial intelligence classes. <br />
 *
 * <p>
 * updates: <br />
 * 1.1<br />
 * - adding a method method {@link #setStraightLines(boolean)}<br />
 * </p>
 * 
 * @author VGT
 * @version 1.1
 */
public abstract class AiGeneric
	{
	/**
	 * A ship container belonging to a computer-controlled player
	 * (useful in more complex versions of AI for determining how far the computer is from a potential failure).
	 */
	protected ShipIterator oShips;
	/**
	 * Keeps the co-ordinates of the last shot on target. <br />
	 * 
	 * The value of this position should be completed in all methods of transferring a shot to the opponent's board.
	 */
	protected Position oLastHit;
	/**
	 * Container used to store the coordinates for successful hits in previous rounds.
	 * 
	 * On its basis, it is possible to search for the next fields of the hit ship in order to further oscillate it.
	 */
	protected ArrayList<Position> oUsefulHits;
	/**
	 * The property determines whether the ships on board can only be vertical / horizontal lines (TRUE),
	 * or they can have any shape (FALSE, default).
	 */
	protected boolean bStraightLines;
	/**
	 * Random number generator.
	 */
	protected Random oRand;
	/**
	 * Constructor.
	 * 
	 * @param oShips A container of ships belonging to the player controlled by the given object Ai.
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
	 * The method allows you to set a property that determines the allowable shape of the ships.
	 * 
	 * @param bValue Determines if ships can only be vertical / horizontal lines.
	 * @since 1.1
	 */
	public void setStraightLines(boolean bValue)
		{
		bStraightLines = bValue;
		}
	/**
	 * The easiest possible implementation of selecting a position to fire. The method finds all positions on which the shot can be taken
	 * and randomly chooses one of them. <br />
	 * 
	 * Information about the coordinates of the returned shot is passed to the shot () method of the container
	 * provided in the parameter and there the full service of the shot is provided.
	 * 
	 * @param oOpponentShip The container of enemy ships to be fired upon.
	 * @return Returns TRUE for Hits from any of the ships, or FALSE if the shot was missed.
	 */
	protected boolean shotRandom(ShipIterator oOpponentShip)
		{
		try
			{
			Position oRandomlyDrawnField = drawField(oOpponentShip.getBoard());
			boolean bHit = oOpponentShip.shot(oRandomlyDrawnField.getX(), oRandomlyDrawnField.getY());
			if (bHit == true)
				{
				//save the shot on target in the hit board save the shot on target in the hit board
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
	 * The method randomly selects one of the saved previous hits and checks if it is possible to shoot at any of the adjacent poles. <br />
	 * 
	 * If so, he chooses one of the fields to fire. If not, removes the field from the list, randomly chooses the next saved hit
	 * and repeats the process. If Hits are exhausted, shotRandom () is called
	 * 
	 * @param oOpponentShip Enemy ships container.
	 * @return Returns TRUE if the ship hits, or FALSE if the shot was missed.
	 */
	protected boolean shotNeighbor(ShipIterator oOpponentShip)
		{
		//preparing a container for storing up to 4 adjacent fields that are suitable for the next shot
		ArrayList<Position> oNeighboringFields = new ArrayList<Position>(4);
		//search loop in earlier Hitsch position to return another shot
		while (oUsefulHits.size() > 0)
			{
			//drawing a position to test
			int iRandomField = oRand.nextInt(oUsefulHits.size());
			Position oSelectedField = oUsefulHits.get(iRandomField);
			
			try
				{
				//load the coordinates of 4 neighbors and check if they are empty or include a ship
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
					// sa position valid for the next shot to be taken
					int iRandomlyDrawnNeighbor = oRand.nextInt(oNeighboringFields.size());
					// putting a shot at the co-ordinates of the selected position
					boolean bshot;
					bshot = oOpponentShip.shot(oNeighboringFields.get(iRandomlyDrawnNeighbor).getX(), oNeighboringFields.get(iRandomlyDrawnNeighbor).getY());
					if (bshot == true)
						{
						// save an accurate shot in the hit table
						Position oHit = new Position(2);
						oHit.setX( oNeighboringFields.get(iRandomlyDrawnNeighbor).getX() );
						oHit.setY( oNeighboringFields.get(iRandomlyDrawnNeighbor).getY() );
						oUsefulHits.add(oHit);
						}
					return bshot;
					}
				else
					{
					// no valid poles. removing Hits from the list and proceeding to the next iteration of the search loop					
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
	 * The method searches randomly on the board for a field to take a shot, but if the drawn field does not contain a ship,
	 * re-draw takes place in order to find a better position for the shot. The allowed number of repetitions is specified
	 * second parameter. <br />
	 * 
	 * If in any iteration a position containing the ship is drawn, the shot is considered a hit
	 * and no subsequent loop iterations are performed. <br />
	 * 
	 * If in the last iteration an empty field is also drawn,
	 * the co-ordinates of that position is deemed to be the shot taken and the shot is missed.
	 * 
	 * @param oOpponentShip Enemy ships container.
	 * @param iRepeatQuantity The allowed number of repetitions of the draw position until oshotu.
	 * @return Returns TRUE if the ship hits, or FALSE if the shot was missed.
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
						//save an accurate shot in the hit table
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
		//the loop must return a shot. since it got here - throw the exception
		throw new DeveloperException();
		}
	/**
	 * The method selects a random field available for firing on the opponent's board and returns a Position object containing these co-ordinates.
	 * 
	 * @param oOpponentBoard The opponent's board at which the shot is to be taken.
	 * @return co-ordinates the drawn position to fire.
	 */
	private Position drawField(Board oOpponentBoard)
		{
		try
			{
			Position oRandomlyDrawnField = new Position(2);
			int iRandomlyDrawnField = oRand.nextInt( oOpponentBoard.getUnknownQuantity() ) + 1;
			// computing x and y for the drawn position
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
