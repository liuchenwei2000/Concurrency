/**
 * 
 */
package thread.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Callable相关类示例
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-17
 */
public class CallableTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 启动一个Runnable任务的方式
		new Thread(new Calculation()).start();
		
		// 启动一个Callable任务的方式
		/**
		 * FutureTask包装器是一个很方便的将Callable转换成Future和Runnable的机制，因为它同时实现了这两个接口。
		 */
		FutureTask<Integer> futureTask = new FutureTask<Integer>(new IntegerCalculation());
		// 这里将FutureTask对象视为一个Runnable
		new Thread(futureTask).start();
		try {
			/**
			 * 这里将FutureTask对象视为一个Futrue
			 * <p>
			 * Future保存异步计算的结果，当使用它时可以启动一个计算，把计算的结果（一个Future对象）交给某线程。
			 * 然后主线程就可以去干其他事情，等到结果计算好之后就可以得到它。
			 */
			// 判断任务是否已经完成
			System.out.println("main:Future task is done? " + futureTask.isDone());
			// 判断任务是否已经取消
			System.out.println("main:Future task is cancelled? " + futureTask.isCancelled());
			// get()方法用来返回计算结果，如果计算尚未完成，该方法会阻塞直到计算完成。
			Integer result = futureTask.get();
			System.out.println("main:Toatl number is " + result);
			/**
			 * 取消任务
			 * <p>
			 * 如果任务尚未开始，它被取消后就不会再开始了。
			 * 如果任务正在进行，若参数是true，它就会被强行中断。
			 */
			futureTask.cancel(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Runnable封装一个异步运行的任务，是一个没有任何参数和返回值的异步方法。
	 */
	private static class Calculation implements Runnable {

		@Override
		public void run() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int result = 10000000;
			System.out.println("Runnable:Toatl number is " + result);
		}
	}

	/**
	 * Callable跟Runnable类似，也封装了一个异步运行的任务，但有返回值。
	 */
	private static class IntegerCalculation implements Callable<Integer> {

		@Override
		public Integer call() throws Exception {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int result = 10000000;
			System.out.println("Callable:Toatl number is " + result);
			return result;
		}
	}
}