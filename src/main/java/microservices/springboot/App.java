package microservices.springboot;

/**
 * Hello world!
 *
 */
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class App {
	
	private final static String LOCAL_HOST_TIP = "tcp://localhost:61616";

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return getActiveMQQueue();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }
    
    public static String getActiveMQQueue() {

		// ConnectionFactory
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, LOCAL_HOST_TIP);
		
		// JMS Provider connection
		Connection connection;
		try {
			connection = connectionFactory.createConnection();

			
			// Session
			Session session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE);
			
			// Destination Get queue
			Destination destination = session.createQueue("my-queue");

			// Consumer
			MessageConsumer consumer = session.createConsumer(destination);
			connection.start();
			
				TextMessage message = (TextMessage) consumer.receive();
				if (null != message) {
					String text = message.getText();
					System.out.println("Message：" + message.getText());
					session.close();
					connection.close();
					return text;
				}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  "";

	}
}