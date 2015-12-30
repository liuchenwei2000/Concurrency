/**
 * 
 */
package thread.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 使用 Executor 运行有返回结果的任务示例
 * <p>
 * 对于有返回结果的任务，主要用到两个接口：
 * 1，Callable
 * 任务需要实现该接口，并在call()方法中实现业务逻辑，并返回值。
 * 2，Future
 * 能够获取 Callable 对象的返回结果，并管理它的状态。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月15日
 */
public class TaskReturnsResultTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 使用 2 个线程的线程池 运行 5 个阶乘计算任务
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
		
		List<Future<Integer>> resultList = new ArrayList<>();
		Random random = new Random();
		
		// 1，向 Executor 提交全部任务
		for (int i = 0; i < 5; i++) {
			int number = random.nextInt(10);
			FactorialCalculator task = new FactorialCalculator(number);
			// 向 Executor 提交 Callable 任务并立即返回一个 Future 对象
			// 在通过 Future 对象的 get()方法获取真正结果时才（可能）会阻塞。
			Future<Integer> result = executor.submit(task);
			resultList.add(result);
		}

		// 2，监控 所有任务 是否全部完成
		do {
			System.out.printf("Main: Number of Completed Tasks: %d\n", executor.getCompletedTaskCount());
			for (int i = 0; i < resultList.size(); i++) {
				Future<Integer> result = resultList.get(i);
				// 判断 任务 是否已经完成
				System.out.printf("Main: Task %d: %s\n", i, result.isDone());
			}

			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (executor.getCompletedTaskCount() < resultList.size());// 当完成任务数和结果数相等时表明所有任务都已完成
		
		// 3，打印任务结果
		System.out.printf("Main: Results\n");
		for (int i = 0; i < resultList.size(); i++) {
			Future<Integer> result = resultList.get(i);
			Integer number = null;
			try {
				// Future 的 get()方法会阻塞直到任务完成
				number = result.get();
				// 也可以使用 带有超时限制的版本
//				number = result.get(10, TimeUnit.SECONDS);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.printf("Main: Task %d: %d\n", i, number);
		}

		executor.shutdown();
	}

	/**
	 * 阶乘计算器
	 */
	private static class FactorialCalculator implements Callable<Integer> {

		private int n;
		
		public FactorialCalculator(int n) {
			this.n = n;
		}

		/**
		 * 实现具体逻辑并返回结果
		 * 
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Integer call() throws Exception {
			if (n == 0 || n == 1) {
				return 1;
			}
			int result = 1;
			for (int i = 2; i <= n; i++) {
				result *= i;
				TimeUnit.MICROSECONDS.sleep(20);
			}
			return result;
		}
	}
}
