package microservices.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Receiver message
 * 
 * @author lij
 * 
 */

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsReceiver {

	public static void main(String[] args) throws JMSException {
		// ConnectionFactory
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
		// JMS Provider connection
		Connection connection = connectionFactory.createConnection();
		// Session
		Session session = connection.createSession(Boolean.FALSE,
				Session.AUTO_ACKNOWLEDGE);
		// Destination
		// Get queue
		Destination destination = session.createQueue("my-queue");

		// Consumer
		MessageConsumer consumer = session.createConsumer(destination);
		connection.start();
		while (true) {
			TextMessage message = (TextMessage) consumer.receive();
			if (null != message) {
				System.out.println("Message：" + message.getText());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				break;
			}
		}
		session.close();
		connection.close();

	}

}
