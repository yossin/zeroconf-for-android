package mta.yos.zeroconf.jms;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import mta.yos.zeroconf.session.DeviceManager;

@MessageDriven( activationConfig = {
		@ActivationConfigProperty(
				propertyName = "destinationType",
				propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination",
				propertyValue = "jmdns.deleteQ"),
				@ActivationConfigProperty(propertyName = "maxSessions",
				propertyValue = "1"),
		@ActivationConfigProperty(
			propertyName = "acknowledgeMode",
			propertyValue = "Auto-acknowledge")
})
public class DeleteMDB implements MessageListener{
	final static Logger logger = Logger.getLogger(DeleteMDB.class.getName());

	@EJB
	DeviceManager manager;
	
	@Override
	public void onMessage(Message message) {
		TextMessage tMessage = (TextMessage) message;
		try {
			String serviceName = tMessage.getText();
			manager.deleteServiceByName(serviceName);
		} catch (Exception e) {
			logger.throwing(DeviceManager.class.getName(), "delete", e);
			logger.severe("unable to update service "+ e.getMessage());
		}
		
		
	}

}
