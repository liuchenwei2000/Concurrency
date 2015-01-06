/**
 * 
 */
package thread;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * join()方法示例
 * <p>
 * 某些情形下，需要等待线程任务结束才能继续向下执行，比如初始化某些资源后才能进行以后的操作，
 * 此时可以使用thread.join()方法来使调用该方法的线程等待目标线程thread完成，然后再向下执行。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月6日
 */
public class JoinTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread t1 = new Thread(new DataSourceLoader());
		t1.start();

		try {
			t1.join();// 主线程会等待 t1 线程执行完才会继续向下执行
			// 设定一个超时时限，超过该时限就不等了；若时限内 t1 线程执行完毕则主线程立即向下执行
//			t1.join(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.printf("Main: Configuration has been loaded: %s\n", new Date());
	}

	/**
	 * 模拟耗时的资源初始化
	 */
	private static class DataSourceLoader implements Runnable {

		@Override
		public void run() {
			System.out.printf("Beginning data sources loading: %s\n", new Date());
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.printf("Data sources loading has finished: %s\n", new Date());
		}
	}
}
