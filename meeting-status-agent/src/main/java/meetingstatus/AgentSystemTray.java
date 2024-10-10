package meetingstatus;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class AgentSystemTray {
	protected static final String VERSION = "1.0-20241008";
	private TrayIcon iconoSystemTray;
	private JApp frame = new JApp();
	
    public AgentSystemTray() {
        // icon declaration
        // verify that SystemTray is supported
        if (SystemTray.isSupported()) {
            //static reference to SystemTray
            SystemTray tray = SystemTray.getSystemTray();
            //get icon image
            Image iconImage = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("images/siren_inv_t.png"));
                        
            // Right clic Menu
            PopupMenu popup = new PopupMenu();
            MenuItem configItem = new MenuItem("Config");
            configItem.addActionListener(new ActionListener() {	
				@Override
				public void actionPerformed(ActionEvent e) {
					// shows the main frame
					frame.frmStatusIndicator.setVisible(true);
				}
			});
            popup.add(configItem);
            MenuItem aboutItem = new MenuItem("About");
            aboutItem.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					// About dialog
					String msg = "Version " + VERSION;
					JOptionPane.showMessageDialog(frame.frmStatusIndicator, msg, JApp.APP_TITLE, JOptionPane.ERROR_MESSAGE);
				}
			});
            popup.add(aboutItem);
            popup.addSeparator();
            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					// Exit App
                	SystemTray.getSystemTray().remove(iconoSystemTray);
                    System.exit(0);
				}
			});
            popup.add(exitItem);
            
            // TrayIcon object initialization
            iconoSystemTray = new TrayIcon(iconImage, JApp.APP_TITLE, popup);
            // double click capture
            ActionListener accionMostrarMensaje = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
					// shows the main frame
                	frame.frmStatusIndicator.setVisible(true);
                }
            };
 
            iconoSystemTray.setImageAutoSize(true);
            iconoSystemTray.addActionListener(accionMostrarMensaje);
 
            // handler in case system tray fails
            try {
                tray.add(iconoSystemTray);
                
            } catch (AWTException e) {
            	JOptionPane.showMessageDialog(null, "Couldn't add icon to System Tray", 
    					JApp.APP_TITLE, JOptionPane.ERROR_MESSAGE);
            }
        } else {
			JOptionPane.showMessageDialog(null, "System does not support System Tray", 
					JApp.APP_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Main class for system tray
     */
    public static void main(String[] args) {
    	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			//Error seting look and feel.
			Logger.getLogger(AgentSystemTray.class.getName()).log(Level.SEVERE, "Error setting look and feel", e);
		}
        new AgentSystemTray();
    }       
}