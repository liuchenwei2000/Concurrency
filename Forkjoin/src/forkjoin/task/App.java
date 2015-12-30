/**
 * 
 */
package forkjoin.task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * ForkJoinPool运行RecursiveTask示例
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年12月25日
 */
public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 10000;
		// 创建最初的Task对象
		RecursiveTask<Integer> task = new SumTask(0, n);
		
		// 创建默认ForkJoinPool实例，任务并行数等于CPU数
		ForkJoinPool pool = new ForkJoinPool();
		// 执行任务，这是一个异步调用，主线程会继续向下执行。
		pool.execute(task);
		
		// 监控ForkJoinPool的运行时参数
		do {
			try {
				TimeUnit.MILLISECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!task.isDone());// 判断任务是否已经结束（包括正常结束、异常中止、任务取消）
		
		// 停止pool的运行
		pool.shutdown();
		
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// 测试任务所做的操作是否正确
		try {
			System.out.printf("【ForkJoinPool】Sum of %d: %d\n", n, task.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		System.out.printf("【Formula】Sum of %d: %d\n", n, n*(n-1)/2);
		System.out.println("Main: End of the program.\n");
	}
}
