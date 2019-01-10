package publishandsubscribe;

import java.io.IOException;
import java.time.LocalTime;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import utils.RabbitMQFactory;

public class EmitLog {
	public static final String EXCHANGE_NAME = "log";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		Channel channel = RabbitMQFactory.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
		for(int i = 0; i < 10; i++) {
			String message = "log" + i + ":" + LocalTime.now().toString();
			channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("utf-8"));
			System.out.println("sent: " + message);
		}
		channel.getConnection().close();
	}
}
