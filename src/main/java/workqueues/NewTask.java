package workqueues;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import utils.RabbitMQFactory;

public class NewTask {
	public static final String QUEUE_NAME = "time";
	public static void main(String[] args) throws IOException, TimeoutException {
		Channel channel = RabbitMQFactory.createChannel();
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		for(int i = 0; i < 10; i++) {
			String message = i + ".now is " + LocalDateTime.now().toString();
			channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("utf-8"));
			System.out.println("Sent:[" + message + "]");
		}
		channel.getConnection().close();
	}
}
