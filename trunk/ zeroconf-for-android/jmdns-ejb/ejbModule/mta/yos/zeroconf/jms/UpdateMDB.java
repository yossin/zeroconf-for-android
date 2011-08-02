package mta.yos.zeroconf.jms;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import mta.yos.zeroconf.bean.UpdateDeviceRequest;
import mta.yos.zeroconf.session.DeviceManager;

@MessageDriven( activationConfig = {
		@ActivationConfigProperty(
				propertyName = "destinationType",
				propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination",
				propertyValue = "jmdns.updateQ"),
				@ActivationConfigProperty(propertyName = "maxSessions",
				propertyValue = "1"),
		@ActivationConfigProperty(
			propertyName = "acknowledgeMode",
			propertyValue = "Auto-acknowledge")
})
public class UpdateMDB implements MessageListener{
	final static Logger logger = Logger.getLogger(UpdateMDB.class.getName());

	@EJB
	DeviceManager manager;
	
	@Override
	public void onMessage(Message message) {
		ObjectMessage oMessage = (ObjectMessage) message;
		try {
			UpdateDeviceRequest request = (UpdateDeviceRequest) oMessage.getObject();
			manager.updateDevice(request);
		} catch (Exception e) {
			logger.throwing(DeviceManager.class.getName(), "update", e);
			logger.severe("unable to update device "+ e.getMessage());
		}
		
		
	}

}
