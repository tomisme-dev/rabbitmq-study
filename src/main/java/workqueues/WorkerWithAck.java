package workqueues;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;

import utils.RabbitMQFactory;

public class WorkerWithAck {
	public static void main(String[] args) {
		Channel channel;
		try {
			channel = RabbitMQFactory.createChannel();
			channel.queueDeclare(NewTask.QUEUE_NAME, true, false, false, null);
			channel.basicQos(2);
			channel.basicConsume(NewTask.QUEUE_NAME, false, (consumerTag, delivery) -> {
				
				try {
					doWork(delivery);
					channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
				} catch (Exception e) {
//					ack取消, 消息重新放入队列
					System.out.println("ack取消");
					channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
				} finally {
//					ack确认
//					channel.basicAck(delivery.getEnvelope().getDeliveryTag(), true);
				}
			}, consumerTag -> {});
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void doWork(Delivery delivery) throws UnsupportedEncodingException {
		String message = new String(delivery.getBody(), "utf-8");
		System.out.println("WorkerWithAck获消息:" + message);
		int i = 1/0;
//		无法走下去, 但异常也会被吃掉,不会抛出
		System.out.println("异常后的东西:" + message);
	}
}
