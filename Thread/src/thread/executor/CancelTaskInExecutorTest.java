/**
 * 
 */
package thread.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 取消 Executor 中的任务示例
 * <p>
 * 可以使用 Future 对象的cancel()方法来取消Executor中的任务。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月15日
 */
public class CancelTaskInExecutorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool();
		
		System.out.printf("Main: Executing the Task\n");
		// 提交任务并立即执行
		Future<String> result = executor.submit(new Task());
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("Main: Canceling the Task\n");
		/*
		 * 取消任务
		 * 
		 * 1，如果任务已经结束，或者早已被取消，或者因为其他原因不能被取消，本方法将返回false并且任务不会被取消。
		 * 2，如果任务正在 Executor 中等待空闲线程，则它会在尚未运行前就被取消。
		 * 如果任务已经在运行中，它是否会被取消取决于本方法的参数：
		 * true：即便任务在运行中也一样会被取消。
		 * false：如果任务在运行中则不会被取消。
		 */
		result.cancel(true);
		
		System.out.printf("Main: Canceled: %s\n", result.isCancelled());// 是否被取消
		System.out.printf("Main: Done: %s\n", result.isDone());// 是否已结束

		executor.shutdown();
		System.out.printf("Main: The executor has finished\n");
	}

	private static class Task implements Callable<String> {

		@Override
		public String call() throws Exception {
			while (true) {
				System.out.printf("Task: Test\n");
				TimeUnit.MILLISECONDS.sleep(100);
			}
		}
	}
}
