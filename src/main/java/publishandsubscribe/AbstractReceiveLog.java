package publishandsubscribe;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import utils.RabbitMQFactory;

public abstract class AbstractReceiveLog {
	public abstract void output(String log);
	
	public void getLogForMQ() throws IOException, TimeoutException {
		Channel channel = RabbitMQFactory.createChannel();
		channel.exchangeDeclare(EmitLog.EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
//		申明一个临时队列
		String queueName = channel.queueDeclare().getQueue();
		
//		将channel绑定到queue上
		channel.queueBind(queueName, EmitLog.EXCHANGE_NAME, "");
		
//		channel绑定消费者
		channel.basicConsume(queueName, true, new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				byte[] body = message.getBody();
				output(new String(body, "utf-8"));
			}
		}, new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
				
			}
		});
	}
}
