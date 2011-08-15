package mta.yos.zeroconf.lamp;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class SwingLampImpl implements Lamp {
	
	private class Listener implements WindowListener{
		boolean shutdown=false;
		private synchronized void shutdown(){
			if (shutdown==false){
				listener.onShutdown();
				shutdown=true;
			}
			
		}
		@Override
		public void windowClosed(WindowEvent e) {
			shutdown();
		}
		@Override
		public void windowOpened(WindowEvent e) {
			try {
				listener.onStartup();
			} catch (Exception e1) {
				e1.printStackTrace();
			}		
		}


		@Override
		public void windowActivated(WindowEvent e) {		
		}
		@Override
		public void windowClosing(WindowEvent e) {
			shutdown();
		}
		@Override
		public void windowDeactivated(WindowEvent e) {
		}
		@Override
		public void windowDeiconified(WindowEvent e) {
		}
		@Override
		public void windowIconified(WindowEvent e) {
		}

	}	
	int status=0;
	@Override
	public int status() {
		return status;
	}

	@Override
	public synchronized void turnOff() {
		status=0;
		frame.getContentPane().setBackground(Color.darkGray);
		frame.setTitle("Off");
	}

	@Override
	public synchronized void turnOn() {
		status=1;
		frame.getContentPane().setBackground(Color.yellow);
		frame.setTitle("On");
	}
	final JFrame frame= new JFrame("Swing Lamp");

	@Override
	public void display() {
	    turnOff();
	    
	    frame.addWindowListener(new Listener());
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBounds(80,80,160,160);
	    frame.setResizable(false);
	    frame.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(status()==0){
					turnOn();
				} else {
					turnOff();
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
		});
	    frame.setVisible(true);

	}

	LampListener listener;

	@Override
	public void setListener(LampListener listener) {
		this.listener=listener;
		listener.setLamp(this);
	}


}
