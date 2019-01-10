package utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQFactory {
	private static ConnectionFactory factory = null;
	private static byte[] lock = new byte[1];
	
	public static ConnectionFactory getFactory() {
		if(factory == null) {
			synchronized (lock) {
				if(factory == null) {
					factory = new ConnectionFactory();
					factory.setUsername("admin");
					factory.setPassword("admin");
					factory.setVirtualHost("kingdee");
					factory.setHost("172.20.14.161");
				}
			}
		}
		return factory;
	}
	
	public static Connection getConnection() throws IOException, TimeoutException {
		return getFactory().newConnection();
	}
	
	public static Channel createChannel() throws IOException, TimeoutException {
		return getConnection().createChannel();
	}
	
}
