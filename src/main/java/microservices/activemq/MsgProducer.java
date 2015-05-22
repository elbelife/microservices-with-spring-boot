package microservices.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.gson.Gson;

/**
 * ActiveMQ send message
 * 
 * @author lij
 * 
 */
public class MsgProducer {
	
	private final static String LOCAL_HOST_TIP = "tcp://localhost:61616";

	public static void main(String[] args) throws JMSException, InterruptedException {
		// ConnectionFactory
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER, 
				ActiveMQConnection.DEFAULT_PASSWORD,
				                   LOCAL_HOST_TIP );
		// JMS Provider
		Connection connection = connectionFactory.createConnection();
		connection.start();
		// Session
		Session session = connection.createSession(Boolean.TRUE,
				Session.AUTO_ACKNOWLEDGE);
		// Destination
		// queue name
		Destination destination = session.createQueue("my-queue");
		// MessageProducer
		MessageProducer producer = session.createProducer(destination);
		//persistence
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		// send message
		sendMessage(session, producer);
		session.close();
		connection.stop();
		connection.close();

	}
	

	/**
	 * On the specified session, a message is issued by the specified message producer。
	 * 
	 * @param session
	 * @param producer
	 * @throws InterruptedException 
	 */
	public static void sendMessage(Session session, MessageProducer producer)
			throws JMSException, InterruptedException {
		int start = 0;
		while (start < 1000){
			start++;
			// create Text message
			TextMessage message = session.createTextMessage(getJsonMatchItem());
			Thread.sleep((int) Math.random()*100);
			// Send message with producer
			producer.send(message);
			session.commit();
			System.out.println("Sent message: " + message.getText());
		}
	}
	
	private static String getJsonMatchItem(){
		MatchItem matchItem = new MatchItem();
		Gson gson = new Gson();
		return gson.toJson(matchItem);
	}
	
}