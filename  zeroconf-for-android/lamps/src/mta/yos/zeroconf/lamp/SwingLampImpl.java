package mta.yos.zeroconf.lamp;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class SwingLampImpl implements Lamp {
	
	private class Listener implements WindowListener{

		@Override
		public void windowClosed(WindowEvent e) {
			listener.onShutdown();
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
		btn.setBackground(Color.darkGray);
		btn.setText("Off");
	}

	@Override
	public synchronized void turnOn() {
		status=1;
		btn.setBackground(Color.yellow);
		btn.setText("On");
	}
	final JFrame frame= new JFrame("Swing Lamp");
    final JButton btn = new JButton();

	@Override
	public void display() {
	    turnOff();
	    frame.addWindowListener(new Listener());
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBounds(80,80,100,100);
	    frame.setResizable(false);
	    frame.setVisible(true);
	    btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if(status()==0){
					turnOn();
				} else {
					turnOff();
				}
				
			}
		});
	    frame.add(btn);
	}

	LampListener listener;

	@Override
	public void setListener(LampListener listener) {
		this.listener=listener;
		listener.setLamp(this);
	}


}
