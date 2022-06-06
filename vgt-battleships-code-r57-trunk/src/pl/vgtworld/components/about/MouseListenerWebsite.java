package pl.vgtworld.components.about;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import javax.swing.JComponent;

/**
 * Implementation of an interface that listens for events related to the mouse
 * implementing the functionality of an html link that opens a web address.
 * 
 * @author VGT
 */
public class MouseListenerWebsite
	implements MouseListener
	{
	private JComponent oComponent;
	private String sWebsiteAddress;
	private Color oHoverColor;
	private Color oDefaultColor;
	/**
	 * Constructor.
	 * 
	 * @param oComponent Reference to the component for which the listener will be used.
	 * @param sWebsiteAddress The web address that is to be opened after clicking on the component..
	 */
	public MouseListenerWebsite(JComponent oComponent, String sWebsiteAddress)
		{
		this(oComponent, sWebsiteAddress, Color.RED);
		}
	/**
	 * Constructor.
	 * 
	 * @param oComponent Reference to the component for which the listener will be used.
	 * @param sWebsiteAddress The web address that is to be opened after clicking on the component.
	 * @param oHoverColor Foreground color for the component executing the hover effect.
	 */
	public MouseListenerWebsite(JComponent oComponent, String sWebsiteAddress, Color oHoverColor)
		{
		this.oComponent = oComponent;
		this.sWebsiteAddress = sWebsiteAddress;
		this.oHoverColor = oHoverColor;
		this.oDefaultColor = oComponent.getForeground();
		}
	/**
	 * Empty.
	 */
	public void mouseClicked(MouseEvent oEvent)
		{
		}
	/**
	 * Changes the color of a component's foreground.
	 */
	public void mouseEntered(MouseEvent arg0)
		{
		oComponent.setForeground(oHoverColor);
		}
	/**
	 * Changes the color of the foreground components.
	 */
	public void mouseExited(MouseEvent arg0)
		{
		oComponent.setForeground(oDefaultColor);
		}
	/**
	 * The web address that is to be opened after clicking on the component..
	 */
	public void mousePressed(MouseEvent arg0)
		{
		try
			{
			Desktop.getDesktop().browse(new URI(sWebsiteAddress));
			}
		catch (Exception oException)
			{
			
			}
		}
	/**
	 * Empty.
	 */
	public void mouseReleased(MouseEvent arg0)
		{
		}
	}
