package microservices.springboot;

/**
 * Hello world!
 *
 */
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

import microservices.activemq.MatchItem;
import microservices.activemq.MicroservicesInfo;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import com.google.gson.Gson;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Controller
public class App {

	private final static String LOCAL_HOST_TIP = "tcp://localhost:61616";

	// ConnectionFactory
	static ConnectionFactory connectionFactory;

	// JMS Provider connection
	static Connection connection;

	@RequestMapping("/matchItem")
	@ResponseBody
	public MatchItem matchItem() {
		return getActiveMQQueue();
	}

	@RequestMapping("/microservicesInfo")
	@ResponseBody
	public MicroservicesInfo microservicesInfo() {
		return getMicroserviceInfo();
	}
	
	public void addViewControllers(ViewControllerRegistry registry) {
	    registry.addViewController("/").setViewName("forward:/index2.html");
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(App.class, args);

	}

	public static MatchItem getActiveMQQueue() {

		try {
			connectionFactory = new ActiveMQConnectionFactory(
					ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD, LOCAL_HOST_TIP);
			connection = connectionFactory.createConnection();
			connection.start();
			// Session
			Session session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE);

			// Destination Get queue
			Destination destination = session.createQueue("my-queue");

			// Consumer
			MessageConsumer consumer = session.createConsumer(destination);

			TextMessage message = (TextMessage) consumer.receive();
			if (null != message) {
				Gson gson = new Gson();
				MatchItem matchItem = gson.fromJson(message.getText(), MatchItem.class);
				System.out.println("MatchItem Info：" + message.getText());
				session.close();
				connection.close();
				return matchItem;
			} else {
				return null;
			}
			
			
		} catch (JMSException e) {
			//Nothing to doing
		}
		return null;

	}

	private static MicroservicesInfo getMicroserviceInfo() {

		MicroservicesInfo msInfo = new MicroservicesInfo();

		try {

			connectionFactory = new ActiveMQConnectionFactory(
					ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD, LOCAL_HOST_TIP);
			connection = connectionFactory.createConnection();
			connection.start();
			
			// Session
			Session session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE);

			Queue replyTo = session.createTemporaryQueue();
			MessageConsumer consumer;

			consumer = session.createConsumer(replyTo);

			String queueName = "ActiveMQ.Statistics.Broker";
			Queue testQueue = session.createQueue(queueName);
			MessageProducer producer = session.createProducer(testQueue);
			Message msg = session.createMessage();
			msg.setJMSReplyTo(replyTo);
			producer.send(msg);

			MapMessage reply = (MapMessage) consumer.receive();

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
					msInfo.setNumberOfPendingMessage((Long) reply
							.getObject(name));
				}
				// System.out.println(name + "=" + reply.getObject(name));

			}
			
			session.close();
			connection.close();

		} catch (JMSException e1) {
			//noting to do
		}

//		Gson gson = new Gson();
//		String json = gson.toJson(msInfo);
//		System.out.println("Microservices info：" + json);
		return msInfo;
	}

}