package meetingstatus;

import java.awt.AWTEvent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.AWTEventListener;
import java.util.logging.Logger;

/** Listener for getting an X, Y position on the screen */
public class XYPosListener implements AWTEventListener {
	private static final Logger LOGGER = Logger.getLogger(XYPosListener.class.getName());
	
	private XYPosObserver xyPosObserver;
	
	public XYPosListener(XYPosObserver xyPosObserver) {
		this.xyPosObserver = xyPosObserver;
	}
	
    public void eventDispatched(AWTEvent event) {
    	
        // We do not want the event to show twice,
        // as it shows for focusing and unfocusing
    	
        if(event.getID() == java.awt.Event.DOWN) {
            Point p = MouseInfo.getPointerInfo().getLocation();
            LOGGER.fine("Mouse Clicked at " + p.x + ", " + p.y);
            if (xyPosObserver != null) {
            	xyPosObserver.callBack(p.x, p.y);
            }
        }
    }
}
