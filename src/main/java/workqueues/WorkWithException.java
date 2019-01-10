package workqueues;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

import utils.RabbitMQFactory;

public class WorkWithException {
	public static void main(String[] args) {
		Channel channel;
		try {
			channel = RabbitMQFactory.createChannel();
			channel.basicQos(2);
			channel.basicConsume(NewTask.QUEUE_NAME, true, (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "utf-8");
				System.out.println("WorkWithException" + channel.hashCode() + "获消息:" + message);
//				int i = 1/0;
//				无法走下去, 但异常也会被吃掉,不会抛出
//				System.out.println("异常后的东西:" + message);
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
