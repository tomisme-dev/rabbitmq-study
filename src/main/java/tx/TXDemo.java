package tx;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import utils.RabbitMQFactory;

public class TXDemo {
	
	public static void main(String[] args) throws IOException, TimeoutException {
		Channel chanel = RabbitMQFactory.createChannel();
		chanel.txSelect();
		try {
			chanel.basicPublish("", "time", MessageProperties.PERSISTENT_TEXT_PLAIN, "time1".getBytes());
			int i = 1/0;
			chanel.basicPublish("", "time", MessageProperties.PERSISTENT_TEXT_PLAIN, "time2".getBytes());
			chanel.txCommit();
		} catch (Exception e) {
			chanel.txRollback();
			e.printStackTrace();
//			消息重发
			chanel.basicPublish("", "time", MessageProperties.PERSISTENT_TEXT_PLAIN, "time1".getBytes());
			chanel.basicPublish("", "time", MessageProperties.PERSISTENT_TEXT_PLAIN, "time2".getBytes());
			chanel.txCommit();
		}

		
		
		chanel.getConnection().close();
	}
}
