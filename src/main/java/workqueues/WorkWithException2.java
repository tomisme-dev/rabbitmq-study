package workqueues;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

import utils.RabbitMQFactory;

public class WorkWithException2 {
	public static void main(String[] args) {
		Channel channel;
		try {
			channel = RabbitMQFactory.createChannel();
			channel.basicConsume(NewTask.QUEUE_NAME, true, (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "utf-8");
				System.out.println("WorkWithException2获消息:" + message);
				try {
					Thread.sleep(100 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int i = 1/0;
				System.out.println("异常后的东西:" + message);
			}, consumerTag -> {});
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
