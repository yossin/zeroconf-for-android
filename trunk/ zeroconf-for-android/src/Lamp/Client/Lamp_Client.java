package Lamp.Client;

import android.R.bool;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Lamp_Client extends Activity 
{
    final int stat_disconnected 	= 0; 
    final int stat_connected_off 	= 1; 
    final int stat_connected_on 	= 2; 
    
    int m_nStat;
    
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        m_nStat = stat_disconnected;
        UpdateUI();
        Button LampBtn = (Button)findViewById(R.id.LampBtn);
        LampBtn.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v) 
        	{
            	int nStat = m_nStat + 1;
            	nStat %= 3;
            	m_nStat = SendStatusUpdate(nStat);
            	UpdateUI();
            }
        });
    }
    
    private void UpdateUI()
    {
    	Button LampBtn = (Button)findViewById(R.id.LampBtn);
    	switch (m_nStat)
    	{
    	case stat_disconnected:
            LampBtn.setText("Disconnected");
            LampBtn.setBackgroundColor(Color.RED);
            break;
    	case stat_connected_off:
            LampBtn.setText("Connected OFF");
            LampBtn.setBackgroundColor(Color.GRAY);
            break;
    	case stat_connected_on:
            LampBtn.setText("Connected ON");
            LampBtn.setBackgroundColor(Color.YELLOW);
            break;
    	}
        
    }
    
    /* return updated status, */
    /* if for example wanted to connect and connection is not established -> updated status = disconnected  */
    int SendStatusUpdate(int nStat)
    {
    	//...
    	
    	return nStat;
    }
}