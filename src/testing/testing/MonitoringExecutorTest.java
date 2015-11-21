/**
 * 
 */
package testing;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 监控 Executor 对象示例
 * <p>
 * 演示 ThreadPoolExecutor 对象所能提供的状态信息以及如何获取它们。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月26日
 */
public class MonitoringExecutorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		
		Random random = new Random();
		// 提交运行十个任务
		for (int i = 0; i < 10; i++) {
			executor.submit(new Task(random.nextInt(10000)));
		}
		// 监控 5秒
		for (int i = 0; i < 5; i++) {
			showLog(executor);
			TimeUnit.SECONDS.sleep(1);
		}
		// 关闭线程池
		executor.shutdown();
		System.out.println("Main: Shutdown the thread pool.\n");
		// 再监控 5秒
		for (int i = 0; i < 5; i++) {
			showLog(executor);
			TimeUnit.SECONDS.sleep(1);
		}
		
		executor.awaitTermination(1, TimeUnit.DAYS);
		System.out.printf("Main: End of the program.\n");
	}
	
	/**
	 * 打印线程池状态信息
	 */
	private static void showLog(ThreadPoolExecutor executor) {
		System.out.printf("*********************\n");
		System.out.printf("Main: Executor Log");
		System.out.printf("Main: Executor: Core Pool Size: %d\n",
				executor.getCorePoolSize());// 返回核心线程数，即当线程池没有任务运行时所保有的最小线程数
		System.out.printf("Main: Executor: Pool Size: %d\n",
				executor.getPoolSize());// 返回池内当前线程数
		System.out.printf("Main: Executor: Active Count: %d\n",
				executor.getActiveCount());// 返回正在执行任务的线程数
		System.out.printf("Main: Executor: Task Count: %d\n",
				executor.getTaskCount());// 返回曾计划执行的任务总数
		System.out.printf("Main: Executor: Completed Task Count: %d\n",
				executor.getCompletedTaskCount());// 返回已完成的任务数
		System.out.printf("Main: Executor: Shutdown: %s\n",
				executor.isShutdown());// 是否已调用 shutdown()方法关闭线程池
		System.out.printf("Main: Executor: Terminating: %s\n",
				executor.isTerminating());// 是否已调用 shutdown() 相关方法而导致线程池正关闭
		System.out.printf("Main: Executor: Terminated: %s\n",
				executor.isTerminated());// 线程池是否已关闭
		System.out.printf("*********************\n");
	}

	/**
	 * 一个会睡眠指定毫秒的任务
	 */
	private static class Task implements Runnable {

		private long ms;
		
		public Task(long ms) {
			this.ms = ms;
		}

		/**
		 * 睡眠指定毫秒
		 */
		@Override
		public void run() {
			System.out.printf("%s: Begin\n", Thread.currentThread().getName());
			try {
				TimeUnit.MILLISECONDS.sleep(ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.printf("%s: End\n", Thread.currentThread().getName());
		}
	}
}
