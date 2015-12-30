/**
 * 
 */
package concurrency.customization;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 定制 ThreadFactory 示例
 * <p>
 * Java提供了ThreadFactory 接口，用来实现一个 Thread 对象工厂。
 * Java并发API的一些高级工具，如 Executor framework 或 Fork/Join framework 都使用线程工厂创建线程。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月22日
 */
public class CustomizingThreadFactory {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyThreadFactory threadFactroy = new MyThreadFactory("mythread");
		
		// 独立使用 定制的 ThreadFactory
		Thread thread = threadFactroy.newThread(new SleepOneSecondTask());
		
		thread.start();
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("Main: Thread information %s\n", thread);
		
		// 可以通过 ThreadFactory 与 Executor 框架进行整合
		ExecutorService pool = Executors.newCachedThreadPool(threadFactroy);
		
		pool.submit(new SleepOneSecondTask());
		pool.shutdown();
		
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("Main: End of the example.");
	}
	
	/**
	 * 自定义 ThreadFactory 类
	 * <p>
	 * 一旦有了自定义的线程类，就需要实现相应的线程工厂。但这并不是必须的。
	 * 如果想在Java提供的并发API中使用自定义线程，通常都需要实现线程工厂。
	 */
	private static class MyThreadFactory implements ThreadFactory {

		private String prefix;// 线程名前缀
		private int counter;// 计数器
		
		public MyThreadFactory(String prefix) {
			super();
			this.prefix = prefix;
			this.counter = 1;
		}

		/**
		 * 只需要实现这一个方法
		 * 
		 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
		 */
		@Override
		public Thread newThread(Runnable r) {
			return new MyThread(r, prefix + "-" + (counter++));
		}
	}
	
	
	/**
	 * 自定义线程类，具有记录线程创建、开始、结束时间的功能。
	 */
	private static class MyThread extends Thread {

		private Date creationDate;
		private Date startDate;
		private Date finishDate;

		public MyThread(Runnable target, String name) {
			super(target, name);
			this.creationDate = new Date();
		}

		@Override
		public void run() {
			this.startDate = new Date();
			super.run();
			this.finishDate = new Date();
		}

		/**
		 * 运行时长
		 */
		public long getExecutionTime() {
			return finishDate.getTime() - startDate.getTime();
		}

		@Override
		public String toString() {
			StringBuilder buffer = new StringBuilder();
			buffer.append(getName());
			buffer.append(": ");
			buffer.append(" Creation Date: ");
			buffer.append(creationDate);
			buffer.append(" : Running time: ");
			buffer.append(getExecutionTime());
			buffer.append(" ms.");
			return buffer.toString();
		}
	}
	
	private static class SleepOneSecondTask implements Runnable {

		@Override
		public void run() {
			try {
				System.out.println("SleepOneSecondTask：I will sleep 1 second");
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
