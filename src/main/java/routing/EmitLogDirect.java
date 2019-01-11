package routing;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import utils.RabbitMQFactory;

public class EmitLogDirect {
	public static final String EXCHANGE = "log_direct";
	public static enum LogRoutKey {
		error, info, warning
	}
	
	public static void main(String[] args) throws IOException, TimeoutException {
		Channel channel = RabbitMQFactory.createChannel();
		channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.DIRECT);
		
		String message = "";
		for(int i = 0; i < 10; i++) {
			if(i % 4 == 0) {
				message = "error:" + i;
				channel.basicPublish(EXCHANGE, LogRoutKey.error.toString(), null, message.getBytes());
			} else if(i % 4 == 1) {
				message = "warning:" + i;
				channel.basicPublish(EXCHANGE, LogRoutKey.info.toString(), null, message.getBytes());
			} else {
				message = "info:" + i;
				channel.basicPublish(EXCHANGE, LogRoutKey.warning.toString(), null, message.getBytes());
			}
			System.out.println("·¢ËÍÏûÏ¢:" + message);
		}
		
		channel.getConnection().close();
		
	}
}
