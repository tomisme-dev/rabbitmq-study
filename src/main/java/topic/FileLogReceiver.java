package topic;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import utils.RabbitMQFactory;

public class FileLogReceiver {
public static final List<String> routKeys = Arrays.asList("#");
	
	public static void main(String[] args) throws IOException, TimeoutException {
		FileLogReceiver receiver = new FileLogReceiver();
		receiver.getMessageFromMQ();
	}
	
	public void getMessageFromMQ() throws IOException, TimeoutException {
		Channel channel = RabbitMQFactory.createChannel();
		channel.exchangeDeclare(EmitLogTopic.EXCHANGE, BuiltinExchangeType.TOPIC);
		String queueName = channel.queueDeclare().getQueue();
		
//		绑定多个route key
		for (String logRoutKey : routKeys) {
			channel.queueBind(queueName, EmitLogTopic.EXCHANGE, logRoutKey);
		}
		
//		创建消费者
		channel.basicConsume(queueName, true, new DeliverCallback() {
			
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
