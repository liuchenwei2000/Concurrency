/**
 * 
 */
package forkjoin.exception;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年12月29日
 */
public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] data = new int[100];

		MyTask task = new MyTask(data, 0, 100);

		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(task);

		pool.shutdown();

		try {
			pool.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 判断任务是否非正常结束（如果task抛出异常或者它的子task抛出异常则返回true）
		if (task.isCompletedAbnormally()) {
			System.out.printf("Main: An exception has ocurred\n");
			System.out.printf("Main: %s\n", task.getException().getMessage());// 能够拿到异常对象
		}

		System.out.printf("Main: result is %d\n", task.join());
	}
}
