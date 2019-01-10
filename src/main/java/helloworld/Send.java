package helloworld;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

import utils.RabbitMQFactory;

public class Send {
	private final static String QUEUE_NAME = "hello";
	public static void main(String[] args) throws IOException, TimeoutException {
			Channel channel = RabbitMQFactory.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			String message = "ÄãºÃ!Hello World! " + LocalDateTime.now().toString();
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes("utf-8"));
			System.out.println(" [x] Sent '" + message + "'");
			channel.close();
			channel.getConnection().close();
	}
}
