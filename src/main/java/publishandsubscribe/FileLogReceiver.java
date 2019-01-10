package publishandsubscribe;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class FileLogReceiver extends AbstractReceiveLog {
	public static void main(String[] args) throws IOException, TimeoutException {
		AbstractReceiveLog logReceiver = new FileLogReceiver();
		logReceiver.getLogForMQ();
	}

	@Override
	public void output(String log) {
		System.out.println("ÎÄ¼þÊä³ö:" + log);
	}

}
