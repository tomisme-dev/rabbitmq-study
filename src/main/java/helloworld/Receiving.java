package helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import utils.RabbitMQFactory;
import workqueues.NewTask;

public class Receiving {
	private final static String QUEUE_NAME = "hello";

	public static void main(String[] argv) throws Exception {
		Channel channel = RabbitMQFactory.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			try {
				Thread.sleep(3*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		    String message = new String(delivery.getBody(), "UTF-8");
		    System.out.println(" [x] Received '" + message + "'");
		};
		
		channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
		channel.basicConsume(NewTask.QUEUE_NAME, true, deliverCallback, consumerTag -> { });
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	}
}
