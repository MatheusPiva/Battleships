package pl.vgtworld.games.ship.ai;

import pl.vgtworld.games.ship.ShipIterator;

/**
 * Factory of AI objects.
 * 
 * @author VGT
 * @version 1.1
 */
abstract public class AiFactory
	{
	/**
	 * A method that returns various AI objects based on the expected difficulty level passed in the parameter.
	 * 
	 * @param iDifficultyLevel A number from 1-100 informing about the expected level of difficulty of a computer player.
	 * @param bStraightLines Determines if ships can only be vertical / horizontal lines.
	 * The information is saved in the created Ai object as it is necessary for later searching for fields for oshotu.
	 * @param oShips A ship container belonging to a generated computer player.
	 * @return Returns an Ai object containing the artificial intelligence of the computer player.
	 */
	public static Ai getAi(int iDifficultyLevel, boolean bStraightLines, ShipIterator oShips)
		{
		Ai oAi;
		if (iDifficultyLevel > 66)
			oAi = new AiCheater(oShips);
		else if (iDifficultyLevel > 33)
			oAi = new AiExtended(oShips);
		else
			oAi = new AiBasic(oShips);
		AiGeneric oAi2 = (AiGeneric)oAi;
		oAi2.setStraightLines(bStraightLines);
		return oAi;
		}
	}
