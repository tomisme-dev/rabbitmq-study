package topic;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import utils.RabbitMQFactory;

public class EmitLogTopic {
	public static final String EXCHANGE = "log_topic";
	public static final List<String> devices = Arrays.asList("kern", "cron");
	public static final List<String> logLevels = Arrays.asList("error", "info", "warning");
	
	public static enum LogRoutKey {
		error, info, warning
	}
	
	public static void main(String[] args) throws IOException, TimeoutException {
		Channel channel = RabbitMQFactory.createChannel();
		channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);
		
		String message = "";
		for(int i = 0; i < 10; i++) {
			int deviceIndex = i % devices.size();
			int levelIndex = i % logLevels.size();
			
			String routeKey = devices.get(deviceIndex) + "." + logLevels.get(levelIndex);
			message = routeKey + ":" + i;
			channel.basicPublish(EXCHANGE, routeKey, null, message.getBytes());
			System.out.println("·¢ËÍÏûÏ¢:" + message);
		}
		
		channel.getConnection().close();
		
	}
}
