/**
 * 
 */
package concurrency.collections;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * LinkedBlockingDeque ʾ��
 * <p>
 * LinkedBlockingDeque ��ʵ������������List�Ĺ��ܡ�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��16��
 */
public class LinkedBlockingDequeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LinkedBlockingDeque<String> list = new LinkedBlockingDeque<>(3);

		Thread clientThread = new Thread(new Client(list));
		clientThread.start();
		
		Thread serverThread = new Thread(new Server(list));
		serverThread.start();
		
		try {
			clientThread.join();
			serverThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("Main: End of the program.\n");
	}
	
	private static String getFormattedDate() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	/**
	 * ģ�ⷢ������Ŀͻ���
	 */
	private static class Client implements Runnable {

		private LinkedBlockingDeque<String> requestList;
		
		public Client(LinkedBlockingDeque<String> list) {
			this.requestList = list;
		}

		@Override
		public void run() {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 5; j++) {
					StringBuilder request = new StringBuilder();
					request.append(i);
					request.append("-");
					request.append(j);
					try {
						// ʹ�� put()�������Ԫ�أ���� LinkedBlockingDeque ��������������ȴ�ֱ���������Ԫ�ء�
						requestList.put(request.toString());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.printf("Client: %s at %s.\n", request, getFormattedDate());
				}
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.printf("Client: End.\n");
		}
	}
	
	/**
	 * ģ���������ķ�������
	 */
	private static class Server implements Runnable {

		private LinkedBlockingDeque<String> requestList;
		
		public Server(LinkedBlockingDeque<String> list) {
			this.requestList = list;
		}

		@Override
		public void run() {
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 3; j++) {
					try {
						// ʹ�� take()������ȡ���Ƴ�Ԫ�أ���� LinkedBlockingDeque �ѿգ���������ȴ�ֱ����Ԫ�ؿ����Ƴ���
						String request = requestList.take();
						// take()������������������ʽ���ֱ��ȡ�׸�����ĩԪ�ز�ɾ��
//						requestList.takeFirst();
//						requestList.takeLast();
						System.out.printf("Server: Request: %s at %s. Size: %d\n", request, getFormattedDate(), requestList.size());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					TimeUnit.MILLISECONDS.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
