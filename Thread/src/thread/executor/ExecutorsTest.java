/**
 * 
 */
package thread.executor;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Executors类示例
 * <p>
 * Executors类是Executor、ExecutorService、ScheduledExecutorService、ThreadFactory和 Callable类的工厂。
 * 提供了一系列实用的方法，比如创建线程池对象。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-17
 */
public class ExecutorsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		// 在需要时创建新线程，空闲线程会被保留60秒的线程池
//		Executors.newCachedThreadPool();
//		// 池中包含固定数量的线程，空闲线程会被一直保留的线程池
//		// 即便任务数比线程数多都也不会再创建新线程，任务会等待直到有线程空闲下来才会执行。
//		Executors.newFixedThreadPool(10);
//		// 只有一个线程的线程池，这个线程会按顺序执行每一个提交上来的任务
//		Executors.newSingleThreadExecutor();
//		// 为预定执行而构建的固定线程池
//		Executors.newScheduledThreadPool(10);
//		// 为预定执行而构建的单线程池
//		Executors.newSingleThreadScheduledExecutor();

		try {
			testInterruptExecutorService();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void testInterruptExecutorService() throws InterruptedException {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new Task());

		TimeUnit.SECONDS.sleep(3);

		System.out.println("shutdown ExecutorService");
		executorService.shutdownNow();

		TimeUnit.SECONDS.sleep(3);
	}

	private static class Task implements Runnable {

		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				System.out.println(new Date());
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					System.out.println("interrupted");
				}
			}
		}
	}
}
