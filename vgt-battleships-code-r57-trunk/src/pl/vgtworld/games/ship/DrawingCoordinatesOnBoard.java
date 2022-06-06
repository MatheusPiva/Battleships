package pl.vgtworld.games.ship;

import pl.vgtworld.tools.Position;

/**
 * Abstract class containing static methods to convert between coordinates 
 * on a drawn board in pixels, and the coordinates in the fields.
 * 
 * @author VGT
 * @version 1.0
 */
public abstract class DrawingCoordinatesOnBoard
	{
	/**
	 * The value of the margin by which the drawn board will be moved away 
         * from the edge of the panel on which it is drawn.<br />
	 * 
	 * Value expressed as a percentage of the panel width.
	 */
	static final int iMargins = 5;
	/**
	 * A method that converts the coordinates of a click on a component 
         * to the coordinates of the field on the board that you clicked.<br />
	 * 
	 * Returns an object containing the coordinates of the
         * clicked field, or the coordinates (-1, -1) if the click did not 
         * hit any field.
	 * 
	 * @param iPanelWidth The width of the panel drawing the board in pixels.
	 * @param iPanelHeight The height of the panel drawing the board in pixels.
	 * @param iBoardWidth Board width (number of fields).
	 * @param iBoardHeight The height of the board (number of fields).
	 * @param iX X coordinate of the clicked pixel.
	 * @param iY The Y coordinate of the pixel you clicked.
	 * @return Returns a position object with the coordinates of the field you clicked.
	 */
	public static Position pixToField(int iPanelWidth, int iPanelHeight, int iBoardWidth, int iBoardHeight, int iX, int iY)
		{
		Position oPosition = new Position(2);
		oPosition.setX(-1);
		oPosition.setY(-1);
		Position oCoOrdinatesTopLeft;
		Position oCoOrdinatesBottomRight;
		//finding X
		for (int i = 0; i < iBoardWidth; ++i)
			{
			oCoOrdinatesTopLeft = DrawingCoordinatesOnBoard.fieldToPixTopLeft(iPanelWidth, iPanelHeight, iBoardWidth, iBoardHeight, i, 0);
			oCoOrdinatesBottomRight = DrawingCoordinatesOnBoard.fieldToPixBottomRight(iPanelWidth, iPanelHeight, iBoardWidth, iBoardHeight, i, 0);
			if (oCoOrdinatesTopLeft.getX() < iX && oCoOrdinatesBottomRight.getX() > iX)
				{
				oPosition.setX(i);
				break;
				}
			}
		//finding Y
		for (int i = 0; i < iBoardHeight; ++i)
			{
			oCoOrdinatesTopLeft = DrawingCoordinatesOnBoard.fieldToPixTopLeft(iPanelWidth, iPanelHeight, iBoardWidth, iBoardHeight, 0, i);
			oCoOrdinatesBottomRight = DrawingCoordinatesOnBoard.fieldToPixBottomRight(iPanelWidth, iPanelHeight, iBoardWidth, iBoardHeight, 0, i);
			if (oCoOrdinatesTopLeft.getY() < iY && oCoOrdinatesBottomRight.getY() > iY)
				{
				oPosition.setY(i);
				break;
				}
			}
		
		return oPosition;
		}
	/**
	 * The method converts the coordinates of the fields on the board 
         * to the coordinates of the top left pixel of the given field in the board drawn on the panel.<br />
	 * 
	 * WARNING! These coordinates are already outside the field (the fields
         * are separated by a single row of pixels in order to draw the mesh)
         * and contain the point where the mesh crosses between the fields.
	 * 
	 * @param iPanelWidth Width panelu w pixelach.
	 * @param iPanelHeight Height panelu w pixelach.
	 * @param iBoardWidth Width planszy w polach.
	 * @param iBoardHeight Height planszy w polach.
	 * @param iXField Wspolrzedna X konwertowanego pola (liczone od 0).
	 * @param iYField Wspolrzedna Y konwertowanego pola (liczone od 0).
	 * @return Wspolrzedne pixela znajdujacego sie przy lewym gornym rogu rysowanego pola.
	 */
	public static Position fieldToPixTopLeft(int iPanelWidth, int iPanelHeight, int iBoardWidth, int iBoardHeight, int iXField, int iYField)
		{
		Position oPosition = new Position(2);
		oPosition.setX(-1);
		oPosition.setY(-1);
		//calculation of the margin width in pixels
		int iHorizontalMarginsFx = (int)((float)iPanelWidth * (float)((float)DrawingCoordinatesOnBoard.iMargins / 100)); 
		int iVerticalMarginsFx = (int)((float)iPanelHeight * (float)((float)DrawingCoordinatesOnBoard.iMargins / 100));
		//System.out.println(""+iHorizontalMarginsFx+","+iVerticalMarginsFx);
		//calculating the width and height of the drawn board after subtracting the margins
		int iBoardWidthPx = iPanelWidth - (2 * iHorizontalMarginsFx);
		int iBoardHeightPx = iPanelHeight - (2 * iVerticalMarginsFx);
		//System.out.println(""+iBoardWidthPx+","+iBoardHeightPx);
		//calculation of the x and y coordinates for the upper left corner of the field
		float fX = ((float)(iBoardWidthPx - 1) / iBoardWidth) * iXField;
		float fY = ((float)(iBoardHeightPx - 1) / iBoardHeight) * iYField;
		//System.out.println(""+fX+","+fY);
		//correction of the coordinates of the margin field
		fX+= (float)iHorizontalMarginsFx;
		fY+= (float)iVerticalMarginsFx;
		//entering the coordinates to the position object and returning the object
		oPosition.setX((int)fX);
		oPosition.setY((int)fY);
		return oPosition;
		}
	/**
	 * The method in operation is similar to {@link #fieldToPixTopLeft
         * (int, int, int, int, int, int)} except that it returns the 
         * coordinates of the pixel at the bottom right corner of the field.
	 * 
	 * @param iPanelWidth The width of the board in pixels.
	 * @param iPanelHeight The Height of the board in pixels.
	 * @param iBoardWidth The width of the board in boxes.
	 * @param iBoardHeight The height of the board in boxes.
	 * @param iXField X coordinate of the field to be converted (counted from 0).
	 * @param iYField Y coordinate of the field to be converted (counted from 0).
	 * @return Coordinates of the pixel at the bottom right corner of the drawn field.
	 */
	public static Position fieldToPixBottomRight(int iPanelWidth, int iPanelHeight, int iBoardWidth, int iBoardHeight, int iXField, int iYField)
		{
		return DrawingCoordinatesOnBoard.fieldToPixTopLeft(iPanelWidth, iPanelHeight, iBoardWidth, iBoardHeight, iXField + 1, iYField + 1);
		}
	}
