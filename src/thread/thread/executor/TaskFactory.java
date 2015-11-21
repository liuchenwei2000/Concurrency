/**
 * 
 */
package thread.executor;

import java.util.concurrent.Callable;

/**
 * Runnable/Callable 工厂，供其他Test类使用
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年8月2日
 */
public class TaskFactory {
	
	public static Runnable createRunnable() {
		return new RunnableTask();
	}

	public static Callable<Integer> createCallable() {
		return new CallableTask();
	}

	private static class RunnableTask implements Runnable {

		public void run() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("In Runnable");
		}
	}

	private static class CallableTask implements Callable<Integer> {

		public Integer call() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("In Callable");
			return 0;
		}
	}
}
