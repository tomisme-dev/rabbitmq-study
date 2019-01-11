package rpc;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import utils.RabbitMQFactory;

public class RPCServer {
	private static final String RPC_QUEU_NAME = "rpc_queue";
	
	public static int fib(int n) {
		if(n == 0) {
			return 0;
		}
		if(n == 1) {
			return 1;
		}
		
		return fib(n-1) + fib(n-2);
	}
	
	public static void main(String[] args) throws IOException, TimeoutException {
		Channel channel = RabbitMQFactory.createChannel();
		channel.queueDeclare(RPC_QUEU_NAME, false, false, false, null);
//		channel.queuePurge(RPC_QUEU_NAME);
		channel.basicQos(1);
		
		System.out.println(" [X] Awaiting RPC requests");
		
		Object monitor = new Object();
		
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
             AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                     .Builder()
                     .correlationId(delivery.getProperties().getCorrelationId())
                     .build();

             String response = "";

             try {
                 String message = new String(delivery.getBody(), "UTF-8");
                 int n = Integer.parseInt(message);

                 System.out.println(" [.] fib(" + message + ")");
                 response += fib(n);
             } catch (RuntimeException e) {
                 System.out.println(" [.] " + e.toString());
             } finally {
                 channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
                 channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                 // RabbitMq consumer worker thread notifies the RPC server owner thread
//                 synchronized (monitor) {
//                	 monitor.notifyAll();
//				}
             }
         };
         
         channel.basicConsume(RPC_QUEU_NAME, false, deliverCallback, (consumerTag -> {
        	 System.out.println("Server cancle" + consumerTag);
         }));
         // Wait and be prepared to consume the message from RPC client.
//         while (true) {
//             synchronized (monitor) {
//                 try {
//                     monitor.wait();
//                 } catch (InterruptedException e) {
//                     e.printStackTrace();
//                 }
//             }
//         }
         System.out.println("server finish");
		
		
	}
	
	
}
