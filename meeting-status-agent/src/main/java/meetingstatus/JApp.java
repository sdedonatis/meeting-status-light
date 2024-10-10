package meetingstatus;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.fazecast.jSerialComm.SerialPort;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class JApp implements XYPosObserver {
	protected final static String APP_TITLE = "Meeting Status Indicator";
	protected JFrame frmStatusIndicator;
	private JComboBox<String> cmbPort;
	private JTextField txtXPos;
	private JTextField txtYPos;
	private JTextField txtDelay;
	private JPanel panelColor;
	private JButton btnFindPixel;
	//Configuration
	private MeetingStatusProperties meetingStatusProperties = new MeetingStatusProperties();
	//Timer to schedule the updater Task
	private Timer timer = new Timer("Timer");
	// Listener reference used for getting an x, y position
	private XYPosListener xyPosListener;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					Logger.getLogger(JApp.class.getName()).log(Level.SEVERE, "Error setting look and feel", e);
				}
				JApp window = new JApp();
				window.frmStatusIndicator.setVisible(true);
			}
		});
	}

	/**
	 * Create the application.
	 */
	public JApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmStatusIndicator = new JFrame();
		frmStatusIndicator.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				saveFormValues();
			}
		});
		frmStatusIndicator.setIconImage(Toolkit.getDefaultToolkit().getImage(JApp.class.getResource("/images/siren_t.png")));
		frmStatusIndicator.setTitle(APP_TITLE);
		frmStatusIndicator.setBounds(100, 100, 421, 310);
		frmStatusIndicator.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmStatusIndicator.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("5dlu"),
				ColumnSpec.decode("default:grow"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				ColumnSpec.decode("5dlu"),},
			new RowSpec[] {
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblSerial = new JLabel("Serial");
		lblSerial.setVerticalAlignment(SwingConstants.TOP);
		lblSerial.setFont(new Font("Tahoma", Font.BOLD, 11));
		frmStatusIndicator.getContentPane().add(lblSerial, "2, 2, left, center");
		
		JLabel lblPort = new JLabel("Port");
		frmStatusIndicator.getContentPane().add(lblPort, "2, 4, right, center");
		
		cmbPort = new JComboBox<>();
		cmbPort.setModel(new DefaultComboBoxModel<String>(new String[] {"COM3", "COM4", "COM5"}));
		frmStatusIndicator.getContentPane().add(cmbPort, "4, 4, left, center");
		
		JButton btnRefreshPort = new JButton("\u27F3");
		btnRefreshPort.setToolTipText("Reload ports");
		btnRefreshPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initializeComboPorts(cmbPort);
			}
		});
		frmStatusIndicator.getContentPane().add(btnRefreshPort, "6, 4");
		
		JLabel lblInterval = new JLabel("Interval (ms)");
		frmStatusIndicator.getContentPane().add(lblInterval, "2, 6, right, center");
		
		txtDelay = new JTextField();
		txtDelay.setText("1000");
		frmStatusIndicator.getContentPane().add(txtDelay, "4, 6, left, center");
		txtDelay.setColumns(10);
		
		JLabel lblPixelColor = new JLabel("Pixel Color");
		lblPixelColor.setFont(new Font("Tahoma", Font.BOLD, 11));
		frmStatusIndicator.getContentPane().add(lblPixelColor, "2, 8, left, center");
		
		JLabel lblXPos = new JLabel("X pos");
		frmStatusIndicator.getContentPane().add(lblXPos, "2, 10, right, center");
		
		txtXPos = new JTextField();
		txtXPos.setText("100");
		frmStatusIndicator.getContentPane().add(txtXPos, "4, 10, left, center");
		txtXPos.setColumns(10);
		
		JLabel lblYPos = new JLabel("Y Pos");
		frmStatusIndicator.getContentPane().add(lblYPos, "6, 10, right, center");
		
		txtYPos = new JTextField();
		txtYPos.setText("100");
		frmStatusIndicator.getContentPane().add(txtYPos, "8, 10, left, center");
		txtYPos.setColumns(10);
		
		btnFindPixel = new JButton("Find Pixel");
		btnFindPixel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findPixel(txtXPos, txtYPos);
			}
		});
		frmStatusIndicator.getContentPane().add(btnFindPixel, "10, 10, left, top");
		
		JLabel lblColor = new JLabel("Color");
		frmStatusIndicator.getContentPane().add(lblColor, "2, 12, right, default");
		
		panelColor = new JPanel();
		panelColor.setBackground(new Color(192, 192, 192));
		frmStatusIndicator.getContentPane().add(panelColor, "4, 12, left, center");
						
		JPanel panel = new JPanel();
		frmStatusIndicator.getContentPane().add(panel, "2, 16, 9, 1, fill, fill");
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateTask();
			}
		});
		panel.add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmStatusIndicator.setVisible(false);
			}
		});
		panel.add(btnCancel);
		
		intializeForm(this);
	}

	/** Initializes the Form with the .properties file values */
	private void intializeForm(JApp jApp) {
		initializeComboPorts(cmbPort);

		try {
			//read .properties file
			meetingStatusProperties.load();
		
			//set form values 
			cmbPort.setSelectedItem(meetingStatusProperties.getPortName());			
			txtDelay.setText(String.valueOf(meetingStatusProperties.getInterval()));			
			txtXPos.setText(String.valueOf(meetingStatusProperties.getXPos()));
			txtYPos.setText(String.valueOf(meetingStatusProperties.getYPos()));
		
			//tries to connect using old values
			updateTask();
			
		} catch(IOException ioe) {
			String msg = "Error reading configuration " + ioe.getMessage();
			JOptionPane.showMessageDialog(frmStatusIndicator, msg, APP_TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}

	/** Inicializa el combo con los puertos seriales disponibles */
	private void initializeComboPorts(JComboBox<String> jComboBox) {
		jComboBox.removeAllItems();
		for (SerialPort commPort: SerialPort.getCommPorts()) {
			jComboBox.addItem(commPort.getSystemPortName());
		}
	}
	
	protected void findPixel(JTextField txtXpos2, JTextField txtYPos2) {
		// cursor para seleccioanr
		frmStatusIndicator.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		//xyPosListener = new XYPosListener(txtXPos, txtYPos);
		xyPosListener = new XYPosListener(this);
		
		//btnFindPixel.setEnabled(false);

		//Listener for getting clic position
		Toolkit.getDefaultToolkit().addAWTEventListener(
				xyPosListener, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
				
		frmStatusIndicator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStatusIndicator.setVisible(true);
		frmStatusIndicator.setAlwaysOnTop(true);
		frmStatusIndicator.setLocation(1, 1);		
	}

	@Override
	public void callBack(Integer x, Integer y) {
		Toolkit.getDefaultToolkit().removeAWTEventListener(xyPosListener);

		frmStatusIndicator.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		txtXPos.setText(String.valueOf(x));
		txtYPos.setText(String.valueOf(y));
		//btnFindPixel.setEnabled(true);
		
		updateColor(panelColor);
		
		String msg = "Posicion " + x + ", " + y + " seleccionada";
		JOptionPane.showMessageDialog(frmStatusIndicator, msg, APP_TITLE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void updateColor(JComponent jComponent) {
		//Get Pixel at X Y
		try {
			Robot robot = new Robot();
			Color color = robot.getPixelColor(Integer.valueOf(txtXPos.getText()), Integer.valueOf(txtYPos.getText()));
			//Set color
			jComponent.setBackground(color);
			
		} catch (AWTException e) {
			String msg = "Error getting pixel color" + e.getMessage();
			JOptionPane.showMessageDialog(frmStatusIndicator, msg, APP_TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/** Saves form values in .properties */
	private void saveFormValues() {
		try {
			//set MeetingStatusProperties values
			if (cmbPort.getSelectedItem() != null) {
				meetingStatusProperties.setPortName((String) cmbPort.getSelectedItem());
			}
			meetingStatusProperties.setInterval(Integer.valueOf(txtDelay.getText()));
			meetingStatusProperties.setXPos(Integer.valueOf(txtXPos.getText()));
			meetingStatusProperties.setYPos(Integer.valueOf(txtYPos.getText()));
		
			//save the file
			meetingStatusProperties.save();
			
		} catch(IOException ioe) {
			String msg = "Error saving configuration " + ioe.getMessage();
			JOptionPane.showMessageDialog(frmStatusIndicator, msg, APP_TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/** Removes the old Task and create a new one with the new parameters */
	private void updateTask() {
		saveFormValues();

		try {
			// Cancell old task
			timer.cancel();
			timer = new Timer();

			// Create new task
			StatusLightTask slu = new StatusLightTask(meetingStatusProperties.getXPos(),
					meetingStatusProperties.getYPos(), meetingStatusProperties.getPortName(), panelColor);
			timer.schedule(slu, 0, meetingStatusProperties.getInterval());
		
			// hides form if no error
			frmStatusIndicator.setVisible(false);
			
		} catch(Exception e) {
			String msg = "Error creating task " + e.getMessage();
			JOptionPane.showMessageDialog(frmStatusIndicator, msg, APP_TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}
}
