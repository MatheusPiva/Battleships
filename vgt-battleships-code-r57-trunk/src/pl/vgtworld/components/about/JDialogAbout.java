package pl.vgtworld.components.about;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * Class displaying a dialog box with contact information.
 * 
 * @author VGT
 */
public class JDialogAbout
	extends JDialog
	{
	private String sSoftwareName;
	private Frame oOwner;
	private ArrayList<JLabel> oAboutLabels;
	private ArrayList<JLabel> oValues;
	private Font oFontName;
	private JButton oCloseButton;
	private JLabel oVersion;
	private Image oLogo;
	/**
	 * Class implementing the action of pressing the key to close the window
	 * 
	 * @author VGT
	 */
	private class CloseAction
		extends AbstractAction
		{
		public CloseAction()
			{
			putValue(AbstractAction.NAME, "OK");
			}
		public void actionPerformed(ActionEvent oEvent)
			{
			JDialogAbout.this.setVisible(false);
			}
		}
	/**
	 * Constructor.
	 * 
         * @param oOwner Window for creating a dialog
	 * @param sSoftName Name of the program for which the dialog is created.
	 */
	public JDialogAbout(Frame oOwner, String sSoftName)
		{
		super(oOwner, true);
		this.sSoftwareName = sSoftName;
		this.oOwner = oOwner;
		oAboutLabels = new ArrayList<JLabel>();
		oValues = new ArrayList<JLabel>();
		oFontName = new Font("Serif", Font.BOLD, 18);
		oCloseButton = new JButton(new CloseAction());
		oVersion = null;
		
		URL oLogoUrl = getClass().getResource("logo.png");
		if (oLogoUrl != null)
			{
			try
				{
				oLogo = ImageIO.read(oLogoUrl);
				}
			catch (IOException oException)
				{
				oLogo = null;
				}
			}
		
		setLayout(new GridBagLayout());
		setTitle("About");
		setLocationRelativeTo(this.oOwner);
		//setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		
		//elements
		Cursor oCursorHand = new Cursor(Cursor.HAND_CURSOR);
		JLabel oMail = new JLabel("tomek@vgtworld.pl");
		MouseListenerMail oMailClick = new MouseListenerMail(oMail, "tomek@vgtworld.pl");
		JLabel oWebsite = new JLabel("www.vgtworld.pl");
		MouseListenerWebsite oWebsiteClick = new MouseListenerWebsite(oWebsite, "www.vgtworld.pl");
		oMail.setCursor(oCursorHand);
		oMail.addMouseListener(oMailClick);
		oWebsite.setCursor(oCursorHand);
		oWebsite.addMouseListener(oWebsiteClick);
		
		addElement("Author:", "VGT", false);
		addElement(new JLabel("E-Mail:"), oMail, false);
		addElement(new JLabel("Website:"), oWebsite, false);
		rebuild();
		
		pack();
		}
	/**
	 * It allows you to set the version of the program that will be displayed as information in the dialog box.
	 * 
	 * @param sVersion The version of the program.
	 */
	public void setVersion(String sVersion)
		{
		oVersion = new JLabel("ver. " + sVersion);
		}
	/**
	 * Performs all the tasks of arranging information in a dialog box.
	 * 
	 * The method is called automatically when elements are added using the {@link #addElement (String, String)} and {@link #addElement (JLabel, JLabel)} methods.
	 * If multiple items are added it is not recommended for performance reasons. Then you should use the methods
	 * {@link #addElement (String, String, boolean)} and {@link #addElement (JLabel, JLabel, boolean)} passing in the third parameter FALSE
	 * and only after adding all elements call the rebuild method.
	 */
	public void rebuild()
		{
		JLabel oNameLabel = new JLabel(sSoftwareName, JLabel.CENTER);
		GridBagConstraints oNameGBC = new GridBagConstraints();
		oNameGBC.gridx = 0;
		oNameGBC.gridy = 0;
		oNameGBC.gridwidth = 3;
		oNameGBC.gridheight = 1;
		oNameGBC.weightx = 100;
		oNameGBC.weighty = 100;
		oNameGBC.anchor = GridBagConstraints.CENTER;
		oNameGBC.insets = new Insets(10, 10, 10, 10);
		oNameLabel.setFont(oFontName);
		add(oNameLabel, oNameGBC);
		
		for (int i = 0; i < oAboutLabels.size(); ++i)
			{
			GridBagConstraints oItemLabelGBC = new GridBagConstraints();
			oItemLabelGBC.gridx = 1;
			oItemLabelGBC.gridy = i + 1;
			oItemLabelGBC.gridwidth = 1;
			oItemLabelGBC.gridheight = 1;
			oItemLabelGBC.weightx = 0;
			oItemLabelGBC.weighty = 100;
			oItemLabelGBC.anchor = GridBagConstraints.WEST;
			oItemLabelGBC.insets = new Insets(2, 15, 2, 15);
			
			
			GridBagConstraints oItemGBC = new GridBagConstraints();
			oItemGBC.gridx = 2;
			oItemGBC.gridy = i + 1;
			oItemGBC.gridwidth = 1;
			oItemGBC.gridheight = 1;
			oItemGBC.weightx = 100;
			oItemGBC.weighty = 100;
			oItemGBC.anchor = GridBagConstraints.WEST;
			oItemGBC.insets = new Insets(2, 0, 2, 15);
			
			add(oAboutLabels.get(i), oItemLabelGBC);
			add(oValues.get(i), oItemGBC);
			}
		
		GridBagConstraints oButtonCloseGBC = new GridBagConstraints();
		oButtonCloseGBC.gridx = 1;
		oButtonCloseGBC.gridy = oAboutLabels.size() + 1;
		oButtonCloseGBC.gridwidth = 2;
		oButtonCloseGBC.gridheight = 1;
		oButtonCloseGBC.weightx = 100;
		oButtonCloseGBC.weighty = 100;
		oButtonCloseGBC.anchor = GridBagConstraints.CENTER;
		oButtonCloseGBC.insets = new Insets(10, 0, 10, 0);
		add(oCloseButton, oButtonCloseGBC);
		
		if (oVersion != null)
			{
			GridBagConstraints oVersionGBC = new GridBagConstraints();
			oVersionGBC.gridx = 1;
			oVersionGBC.gridy = oAboutLabels.size() + 2;
			oVersionGBC.gridwidth = 2;
			oVersionGBC.gridheight = 1;
			oVersionGBC.weightx = 100;
			oVersionGBC.weighty = 100;
			oVersionGBC.anchor = GridBagConstraints.SOUTHEAST;
			oVersionGBC.insets = new Insets(0, 0, 5, 5);
			add(oVersion, oVersionGBC);
			}
		
		if (oLogo != null)
			{
			JLabel oLogo = new JLabel();
			oLogo.setIcon(new ImageIcon(this.oLogo));
			GridBagConstraints oLogoGBC = new GridBagConstraints();
			oLogoGBC.gridx = 0;
			oLogoGBC.gridy = 1;
			oLogoGBC.gridwidth = 1;
			if (oVersion != null)
				oLogoGBC.gridheight = oAboutLabels.size() + 2;
			else
				oLogoGBC.gridheight = oAboutLabels.size() + 1;
			oLogoGBC.weightx = 0;
			oLogoGBC.weighty = 100;
			oLogoGBC.anchor = GridBagConstraints.NORTH;
			oLogoGBC.insets = new Insets(0, 10, 0, 0);
			add(oLogo, oLogoGBC);
			}
		}
	/**
	 * Performs centering of the window with respect to the parent window.
	 * The method should be called in the action displaying the dialog box immediately before setting the visibility.
	 */
	public void centerPosition()
		{
		int iX = oOwner.getX() + (oOwner.getWidth() - getWidth()) / 2;
		int iY = oOwner.getY() + (oOwner.getHeight() - getHeight()) / 2;
		setBounds(iX, iY, getWidth(), getHeight());
		}
	/**
	 * Add a new item to the list of information presented..
	 * 
	 * After adding an item, the method automatically calls the {@link #rebuild ()} method to update the window view.
	 * If it is inappropriate, use the {@link #addElement (String, String, boolean)} method.
	 * 
	 * @param sLabel The name of the property to be added.
	 * @param sValue Value of the property to be added.
	 */
	public void addElement(String sLabel, String sValue)
		{
		addElement(new JLabel(sLabel), new JLabel(sValue), true);
		}
	/**
	 * Add a new item to the list of information presented.
	 * 
	 * @param sLabel The name of the property to be added.
	 * @param sValue Value of the property to be added.
	 * @param bRebuild Determines whether the rebuild method should be called after adding an item.
	 */
	public void addElement(String sLabel, String sValue, boolean bRebuild)
		{
		addElement(new JLabel(sLabel), new JLabel(sValue), bRebuild);
		}
	/**
	 * Add a new item to the list of information presented.
	 * 
	 * After adding an item, the method automatically calls the {@link #rebuild ()} method to update the window view.
	 * If it is inappropriate. use the method {@link #addElement (JLabel, JLabel, boolean)}.
	 * 
	 * @param oLabel Name of the Property being Added.
	 * @param oValue Value of the added property.
	 */
	public void addElement(JLabel oLabel, JLabel oValue)
		{
		addElement(oLabel, oValue, true);
		}
	/**
	 * Adds a new item to the list of information presented.
	 * 
	 * @param oLabel The name of the property being added.
	 * @param oValue Value of the added property.
	 * @param bRebuild Specifies whether to call the rebuild method after adding an item.
	 */
	public void addElement(JLabel oLabel, JLabel oValue, boolean bRebuild)
		{
		oAboutLabels.add(oLabel);
		oValues.add(oValue);
		if (bRebuild == true)
			rebuild();
		}
	}
