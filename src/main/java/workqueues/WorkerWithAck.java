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
//					ackȡ��, ��Ϣ���·������
					System.out.println("ackȡ��");
					channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
				} finally {
//					ackȷ��
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
		System.out.println("WorkerWithAck����Ϣ:" + message);
		int i = 1/0;
//		�޷�����ȥ, ���쳣Ҳ�ᱻ�Ե�,�����׳�
		System.out.println("�쳣��Ķ���:" + message);
	}
}
