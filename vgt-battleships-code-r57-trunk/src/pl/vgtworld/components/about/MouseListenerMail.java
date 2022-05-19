package pl.vgtworld.components.about;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import javax.swing.JComponent;

/**
 * Implementation of interface that listens for mouse events
 * implementing the functionality of the html link to the e-mail address.
 * 
 * @author VGT
 */
public class MouseListenerMail
	implements MouseListener
	{
	private JComponent oComponent;
	private String sEmailAddress;
	private String sName;
	private Color oHoverColor;
	private Color oDefaultColor;
	/**
	 * Constructor.
	 * 
	 * @param oComponent Reference to the component for which the listener will be used.
	 * @param sEmailAddress E-Mail address to which the message is to be created after clicking on the component.
	 */
	public MouseListenerMail(JComponent oComponent, String sEmailAddress)
		{
		this(oComponent, sEmailAddress, null, Color.RED);
		}
	/**
	 * Constructor.
	 * 
	 * @param oComponent Reference to the component for which the listener will be used.
	 * @param sEmailAddress E-Mail address to which the message is to be created after clicking on the component.
	 * @param sName Name / surname / name of the addressee.
	 */
	public MouseListenerMail(JComponent oComponent, String sEmailAddress, String sName)
		{
		this(oComponent, sEmailAddress, sName, Color.RED);
		}
	/**
	 * Constructor.
	 * 
	 * @param oComponent Reference to the component for which the listener will be used.
	 * @param sEmailAddress E-Mail address to which the message is to be created after clicking on the component.
	 * @param oHoverColor The foreground color for the component executing the hover effect.
	 */
	public MouseListenerMail(JComponent oComponent, String sEmailAddress, Color oHoverColor)
		{
		this(oComponent, sEmailAddress, null, oHoverColor);
		}
	/**
	 * Constructor.
	 * 
	 * @param oComponent Reference to the component for which the listener will be used.
	 * @param sEmailAddress E-Mail address to which the message is to be created after clicking on the component.
	 * @param sName Name / surname / name of the addressee.
	 * @param oHoverColor The foreground color for the component executing the hover effect.
	 */
	public MouseListenerMail(JComponent oComponent, String sEmailAddress, String sName, Color oHoverColor)
		{
		this.oComponent = oComponent;
		this.sEmailAddress = sEmailAddress;
		this.sName = sName;
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
	 * Changes the color of a component's foreground.
	 */
	public void mouseExited(MouseEvent arg0)
		{
		oComponent.setForeground(oDefaultColor);
		}
	/**
	 * Open the window for composing a new message in the default e-mail client.
	 */
	public void mousePressed(MouseEvent arg0)
		{
		try
			{
			if (sName == null)
				Desktop.getDesktop().mail(new URI("mailto:" + sEmailAddress));
			else
				Desktop.getDesktop().mail(new URI("mailto:" + sName + "<" + sEmailAddress + ">"));
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
