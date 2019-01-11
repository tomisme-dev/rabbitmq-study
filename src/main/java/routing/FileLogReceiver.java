package routing;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import routing.EmitLogDirect.LogRoutKey;
import utils.RabbitMQFactory;

public class FileLogReceiver {
	public static final String QUEUE_NAME = "file_log";
	public static final List<LogRoutKey> routeKeys = Arrays.asList(LogRoutKey.error);
	
	public static void main(String[] args) throws IOException, TimeoutException {
		FileLogReceiver fileLogReceiver = new FileLogReceiver();
		fileLogReceiver.getMessageFromMQ();
	}
	
	public void getMessageFromMQ() throws IOException, TimeoutException {
		Channel channel = RabbitMQFactory.createChannel();
		channel.exchangeDeclare(EmitLogDirect.EXCHANGE, BuiltinExchangeType.DIRECT);
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		
//		绑定多个route key
		for (LogRoutKey logRoutKey : routeKeys) {
			channel.queueBind(QUEUE_NAME, EmitLogDirect.EXCHANGE, logRoutKey.toString());
		}
		
//		创建消费者
		channel.basicConsume(QUEUE_NAME, true, new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				byte[] body = message.getBody();
				System.out.println("文件:"  + new String(body));
			}
		}, new CancelCallback() {
			
			@Override
			public void handle(String consumerTag) throws IOException {
			}
		});
		
	}
}
