package microservices.activemq;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
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

import com.google.gson.Gson;

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

		// Destination Get queue
		Destination destination = session.createQueue("my-queue");

		// Consumer
		MessageConsumer consumer = session.createConsumer(destination);
		connection.start();

		while (true) {
			TextMessage message = (TextMessage) consumer.receive();

			System.out.println(getMicroserviceInfo(session));

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

	private static String getMicroserviceInfo(Session session)
			throws JMSException {
		Queue replyTo = session.createTemporaryQueue();
		MessageConsumer consumer = session.createConsumer(replyTo);

		String queueName = "ActiveMQ.Statistics.Broker";
		Queue testQueue = session.createQueue(queueName);
		MessageProducer producer = session.createProducer(testQueue);
		Message msg = session.createMessage();
		msg.setJMSReplyTo(replyTo);
		producer.send(msg);

		MapMessage reply = (MapMessage) consumer.receive();

		MicroservicesInfo msInfo = new MicroservicesInfo();
		for (@SuppressWarnings("rawtypes")
		Enumeration e = reply.getMapNames(); e.hasMoreElements();) {

			String name = e.nextElement().toString();

			if (name.equals("enqueueCount")) {
				msInfo.setMessagesEnqueued((Long) reply.getObject(name));
			}

			if (name.equals("dequeueCount")) {
				msInfo.setMessagesDequeued((Long) reply.getObject(name));
			}

			if (name.equals("producerCount")) {
				msInfo.setNumberOfConsumers((Long) reply.getObject(name));
			}

			if (name.equals("dispatchCount")) {
				msInfo.setNumberOfPendingMessage((Long) reply.getObject(name));
			}

		}

		Gson gson = new Gson();
		String json = gson.toJson(msInfo);
		return json;
	}

}
