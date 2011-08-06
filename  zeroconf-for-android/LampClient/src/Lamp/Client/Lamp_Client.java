package Lamp.Client;

import java.util.Timer;
import java.util.TimerTask;

import mta.yos.zeroconf.lamp.BaseLampApp;
import mta.yos.zeroconf.lamp.ConsoleLampImpl;
import mta.yos.zeroconf.lamp.JmdnsLampApp;
import mta.yos.zeroconf.lamp.Lamp;
import mta.yos.zeroconf.lamp.LampInfo;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.style.UpdateLayout;
import android.view.View;
import android.widget.Button;

public class Lamp_Client extends Activity implements Lamp 
{
    final int stat_connected_off 	= 0; 
    final int stat_connected_on 	= 1; 
    final int stat_connected_error 	= 2; 
    
    final String name="AndroidLamp";
    final int port=9007;
    final String provider="mta.yos.zeroconf.providers.JavaLampProvider";
    final String serialNumber="123329";
    
    int m_nStat;
    
    
    
    
    public void onCreate(Bundle savedInstanceState) 
    {
    	handler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        m_nStat = stat_connected_off;
        UpdateUI(null);
        Button LampBtn = (Button)findViewById(R.id.LampBtn);
        LampBtn.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v) 
        	{
            	m_nStat += 1;
            	m_nStat %= 2;
            	UpdateUI(null);
            	/* update is pulled... */
            }
        });
        
        RegisterLamp(true);
        
//		startTimer();
    	
    }

    private boolean bCanRegistered = false;
    private boolean bRegistered = false;
    
    private void RegisterLamp(boolean bOn)
    {
    	try {
    		if (/*bCanRegistered == false ||*/ bRegistered == bOn)
    			return;
    		bRegistered = bOn;
    		if (bOn)
    	    {
    			BaseLampApp app = new JmdnsLampApp (new LampInfo(name, port, provider, serialNumber));
    			app.setLamp(this);
        		setListener(app);
    			listener.onStartup();
    	    }
    		else
    		{
    			listener.onShutdown();
    		}
    	} catch (Exception e) {
    		m_nStat = stat_connected_error;
    		UpdateUI("RegisterLamp(" + Boolean.toString(bOn) + ") \n[" + e.getStackTrace().toString() +"]");
    		e.printStackTrace();
    	}
    }
    
    Handler handler;
    private  void startTimer1(){
    	handler.postAtTime(new Runnable() {
			
			@Override
			public void run() {
		    	UpdateUI(null);		
			}
		},50);
    }
    private  void startTimer(){
    	Timer t = new Timer();
    	t.schedule(new TimerTask() {
			
			@Override
			public void run() 
			{
				startTimer1();
			}
		}, 5000, 5000);
    }
    
    public void startActivity(Intent intent)
    {
        RegisterLamp(true);
    	super.startActivity(intent);
    }
    
    protected void onStart()
    {
        RegisterLamp(true);
    	super.onStart();
    }

    protected void onRestart()
    {
        RegisterLamp(true);
    	super.onRestart();
    }

    protected void onResume()
    {
        RegisterLamp(true);
    	super.onResume();
    }

    protected void onPostResume()
    {
        RegisterLamp(true);
    	super.onPostResume();
    }

    
    protected void onStop()
    {
        RegisterLamp(false);
    	super.onStop();
    }
    protected void onPause()
    {
        RegisterLamp(false);
    	super.onPause();    	
    }

    protected void onUserLeaveHint()
    {
        RegisterLamp(false);
    	super.onUserLeaveHint();
    	
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {	
        RegisterLamp(false);
    	super.onActivityResult(requestCode, resultCode, data);
    } 
    
    protected void onDestroy()
    {
        RegisterLamp(false);
    	super.onDestroy();
    	
    	
    }
    
    private void UpdateUI(String sMsg)
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
    	case stat_connected_error:
            LampBtn.setText("Error, " + sMsg);
            LampBtn.setBackgroundColor(Color.RED);
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
    	startTimer1();
	}

	public void turnOn() 
	{
    	m_nStat = stat_connected_on;
    	startTimer1();
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