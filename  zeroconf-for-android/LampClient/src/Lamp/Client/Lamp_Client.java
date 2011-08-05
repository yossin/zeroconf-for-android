package Lamp.Client;

import mta.yos.zeroconf.lamp.BaseLampApp;
import mta.yos.zeroconf.lamp.ConsoleLampImpl;
import mta.yos.zeroconf.lamp.JmdnsLampApp;
import mta.yos.zeroconf.lamp.Lamp;
import mta.yos.zeroconf.lamp.LampInfo;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Lamp_Client extends Activity implements Lamp 
{
    final int stat_connected_off 	= 0; 
    final int stat_connected_on 	= 1; 
    
    final String name="AndroidLamp";
    final int port=9007;
    final String provider="mta.yos.zeroconf.providers.JavaLampProvider";
    final String serialNumber="123327";
    
    int m_nStat;
    
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        m_nStat = stat_connected_off;
        UpdateUI();
        Button LampBtn = (Button)findViewById(R.id.LampBtn);
        LampBtn.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v) 
        	{
            	m_nStat += 1;
            	m_nStat %= 2;
            	UpdateUI();
            	/* update is pulled... */
            }
        });
        BaseLampApp app = new JmdnsLampApp (new LampInfo(name, port, provider, serialNumber));
        app.setLamp(this);
        setListener(app);
        
    	try {
			listener.onStartup();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    protected void onDestroy()
    {
    	listener.onShutdown();
    	super.onDestroy();
    }
    private void UpdateUI()
    {
    	Button LampBtn = (Button)findViewById(R.id.LampBtn);
    	switch (m_nStat)
    	{
    	case stat_connected_off:
            LampBtn.setText("Turned OFF");
            LampBtn.setBackgroundColor(Color.GRAY);
            break;
    	case stat_connected_on:
            LampBtn.setText("Turned ON");
            LampBtn.setBackgroundColor(Color.YELLOW);
            break;
    	}
    }

	public int status() 
	{
		return m_nStat;
	}

	public void turnOff() 
	{
    	m_nStat = stat_connected_off;
    	UpdateUI();		
	}

	public void turnOn() 
	{
    	m_nStat = stat_connected_on;
    	UpdateUI();		
	}

	public void display() 
	{
		turnOff();
	}

	LampListener listener;
	@Override
	public void setListener(LampListener listener) {
		listener.setLamp(this);
		this.listener = listener;
	}
}