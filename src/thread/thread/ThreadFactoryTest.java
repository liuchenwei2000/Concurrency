/**
 * 
 */
package thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * ThreadFactory示例
 * <p>
 * 工厂模式在线程中的使用，JDK中的很多API都是使用的ThreadFactory创建线程而不是new。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月7日
 */
public class ThreadFactoryTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyThreadFactory tf = new MyThreadFactory("my");
		
		for (int i = 0; i < 5; i++) {
			tf.newThread(new Task()).start();
		}
		
		System.out.println(tf);
	}

	/**
	 * 自定义ThreadFactory
	 */
	private static class MyThreadFactory implements ThreadFactory {

		private String basicName;// 基本名称
		private int counter;// 计数器
		private List<String> stats;// 统计信息
	
		public MyThreadFactory(String basicName) {
			super();
			this.basicName = basicName;
			this.counter=0;
			this.stats = new ArrayList<String>();
		}

		/**
		 * 必须实现的方法，该方法可以控制创建线程的过程，比如增加一些统计信息、限制线程数量等等。
		 * 
		 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
		 */
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, basicName + "-Thread_" + counter);
			counter++;
			stats.add(String.format("\nCreated thread %d with name %s on %s",
					t.getId(), t.getName(), new Date()));
			return t;
		}

		@Override
		public String toString() {
			return "MyThreadFactory 【stats=" + stats + "\n】";
		}
	}
	
	private static class Task implements Runnable {

		public void run() {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
