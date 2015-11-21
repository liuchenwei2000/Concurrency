/**
 * 
 */
package forkjoin;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * ForkJoinPool示例
 * <p>
 * 本例主要讲述ForkJoinPool与其他ExecutorService的不同之处。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年12月25日
 */
public class ForkJoinPoolTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 通过指定并行数创建ForkJoinPool实例
		ForkJoinPool pool = new ForkJoinPool(5);
		// 执行ForkJoinTask，会使用工作窃取算法
		pool.execute(new MyForkJoinTask());
		// 执行Runnable，不会使用工作窃取算法，过程与其他ExecutorService类似。
		pool.execute(new MyRunnableTask());
		
		// 与 execute(ForkJoinTask)不同的是：invoke(ForkJoinTask)是同步调用，直到ForkJoinTask运行结束invoke方法才会返回。
		pool.invoke(new MyForkJoinTask());
		
		try {
			// 因为ForkJoinPool实现了ExecutorService接口，所以也实现了下面的两个方法，这两个方法也不会使用工作窃取算法。
			pool.invokeAll(Arrays.asList(new MyCallableTask()));
			pool.invokeAny(Arrays.asList(new MyCallableTask()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 可以使用ForkJoinTask.adapt方法将传统的Runnable或Callable任务适配成ForkJoinTask
		pool.execute(ForkJoinTask.adapt(new MyRunnableTask()));
		pool.execute(ForkJoinTask.adapt(new MyCallableTask()));
	}
	
	private static class MyForkJoinTask extends RecursiveAction {

		private static final long serialVersionUID = 1L;

		@Override
		protected void compute() {
		}
	}
	
	private static class MyRunnableTask implements Runnable {

		@Override
		public void run() {
		}
	}
	
	private static class MyCallableTask implements Callable<Object> {

		@Override
		public Object call() throws Exception {
			return null;
		}
	}
}
