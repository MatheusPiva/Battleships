package pl.vgtworld.games.ship.components;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import pl.vgtworld.games.ship.ShipIterator;

/**
 * @author VGT
 * @version 1.0
 */
public class JComponentShipGameStatus
	extends JComponent
	{
	private static final int MARGINES = 5;
	private ShipIterator oShips;
	private Image oUndamagedShipImg;
	private Image oDamagedShipImg;
	public JComponentShipGameStatus(ShipIterator oContainer) throws IOException
		{
		oShips = oContainer;
		URL oImgUrlUndamagedShip = getClass().getResource("/pl/vgtworld/games/ship/img/ship-0.png");
		URL oImgUrlDamagedShip = getClass().getResource("/pl/vgtworld/games/ship/img/ship-1.png");
		if (oImgUrlUndamagedShip != null && oImgUrlDamagedShip != null)
			{
			oUndamagedShipImg = ImageIO.read(oImgUrlUndamagedShip);
			oDamagedShipImg = ImageIO.read(oImgUrlDamagedShip);
			}
		else
			{
			oUndamagedShipImg = null;
			oDamagedShipImg = null;
			}
		}
	public void setShips(ShipIterator oShips)
		{
		this.oShips = oShips;
		}
	@Override public void paintComponent(Graphics g)
		{
		if (oUndamagedShipImg != null && oDamagedShipImg != null && oShips != null)
			{
			int iUndamaged = oShips.getNumberOfUndamagedShips();
			int iDamaged = oShips.getNumberOfShipsHit();
			int iX = 0;
			for (int i = 1; i <= iUndamaged; ++i)
				{
				g.drawImage(oUndamagedShipImg, iX, 0, null);
				iX+= oUndamagedShipImg.getWidth(null) + MARGINES;
				}
			for (int i = 1; i <= iDamaged; ++i)
				{
				g.drawImage(oDamagedShipImg, iX, 0, null);
				iX+= oDamagedShipImg.getWidth(null) + MARGINES;
				}
			}
		}
	}
