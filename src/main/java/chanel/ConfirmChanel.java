package chanel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import utils.RabbitMQFactory;

public class ConfirmChanel {
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Channel channel = RabbitMQFactory.createChannel();
		channel.addConfirmListener(new ConfirmCallback() {
			@Override
			public void handle(long deliveryTag, boolean multiple) throws IOException {
//				消息发送成功
				System.out.printf("发送消息成功:%d,%s\n", deliveryTag, multiple);
			}
		}, new ConfirmCallback() {
			@Override
			public void handle(long deliveryTag, boolean multiple) throws IOException {
//				消息方式失败
				System.out.printf("发送消息失败:%d,%s\n", deliveryTag, multiple);
			}
		});
		
		channel.confirmSelect();
		channel.basicPublish("", "time", MessageProperties.PERSISTENT_TEXT_PLAIN, "test confirm1".getBytes());
		channel.basicPublish("", "time", MessageProperties.PERSISTENT_TEXT_PLAIN, "test confirm2".getBytes());
		channel.waitForConfirms(10 * 1000);	
		
		channel.getConnection().close();
	}
}
