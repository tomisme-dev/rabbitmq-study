package publishandsubscribe;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsoleLogReceiver extends AbstractReceiveLog {
	public static void main(String[] args) throws IOException, TimeoutException {
		AbstractReceiveLog logReceiver = new ConsoleLogReceiver();
		logReceiver.getLogForMQ();
	}

	@Override
	public void output(String log) {
		System.out.println("¿ØÖÆÌ¨Êä³ö:" + log);
	}

}
