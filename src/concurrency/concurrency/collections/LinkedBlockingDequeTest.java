/**
 * 
 */
package concurrency.collections;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * LinkedBlockingDeque 示例
 * <p>
 * LinkedBlockingDeque 类实现了阻塞并发List的功能。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月16日
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
	 * 模拟发出请求的客户端
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
						// 使用 put()方法添加元素，如果 LinkedBlockingDeque 已满，则会阻塞等待直到可以添加元素。
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
	 * 模拟接受请求的服务器端
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
						// 使用 take()方法获取并移除元素，如果 LinkedBlockingDeque 已空，则会阻塞等待直到有元素可以移除。
						String request = requestList.take();
						// take()方法还有如下两种形式，分别获取首个、最末元素并删除
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
