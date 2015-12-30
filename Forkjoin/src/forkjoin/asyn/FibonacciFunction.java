/**
 * 
 */
package forkjoin.asyn;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 斐波那契函数示例
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年12月29日
 */
public class FibonacciFunction extends RecursiveTask<Integer> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 斐波那契数列的前10项值 */
	private static int[] result = { 1, 1, 2, 3, 5, 8, 13, 21, 34 };

	private int n;
	
	public FibonacciFunction(int n) {
		this.n = n;
	}

	@Override
	protected Integer compute() {
		if (n < 10) {// 前十项直接返回结果
			return result[n - 1];
		}
		
		FibonacciFunction sub1 = new FibonacciFunction(n - 1);
		sub1.fork();
		FibonacciFunction sub2 = new FibonacciFunction(n - 2);
		sub2.fork();

		return sub1.join() + sub2.join();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FibonacciFunction task = new FibonacciFunction(25);

		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(task);

		pool.shutdown();
		
		try {
			pool.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(task.join());
	}
}
