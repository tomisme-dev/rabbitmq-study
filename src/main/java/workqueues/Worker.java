package workqueues;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import utils.RabbitMQFactory;

public class Worker implements Runnable {
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for(int i = 0; i < 1; i++) {
			executorService.execute(new Worker());
		}
		
		executorService.awaitTermination(10L, TimeUnit.MINUTES);
		
	}
	
	public void consumer() throws IOException, TimeoutException {
		DeliverCallback deliverCallback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				String msg = new String(message.getBody(), "utf-8");
				try {
					doWork(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println(Thread.currentThread().toString() + ":task finish:" + msg);
				}
			}
		};
		
		Channel channel = RabbitMQFactory.createChannel();
		channel.basicConsume(NewTask.QUEUE_NAME, true,deliverCallback, new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
				
			}
		});
	}
	
	public void doWork(String task) throws InterruptedException {
		for (char ch: task.toCharArray()) {
	        if (ch == '.') Thread.sleep(1000);
	    }
	}

	@Override
	public void run() {
		try {
			consumer();
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}
}
