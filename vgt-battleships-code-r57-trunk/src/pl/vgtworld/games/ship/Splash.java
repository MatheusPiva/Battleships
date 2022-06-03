/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.vgtworld.games.ship;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

/**
 *
 * @author danie
 */
public class Splash {
    
    private final int imgWidth = 1000;
    private final int imgHeight = 900;
    private final int splashTime = 6000;
    private final String imgPath = "/pl/vgtworld/games/ship/img/splash.png";
    
    public Splash(){
        
        JWindow windowSplash = new JWindow();
        
        windowSplash.getContentPane().add(
            new JLabel(
                    "", new ImageIcon(getClass().getResource(imgPath)),
                    SwingConstants.CENTER)
        );
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        
        windowSplash.setBounds(
                (dimension.width - imgWidth) / 2, 
                (dimension.height - imgHeight) / 2, 
                imgWidth,
                imgHeight);
        
        windowSplash.setVisible(true);
        
        try{
            Thread.sleep(splashTime);
        }catch(InterruptedException e){}
        
        windowSplash.dispose(); 
        
    }
        
}
