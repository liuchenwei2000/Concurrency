/**
 * 
 */
package thread.executor;

import java.util.concurrent.Callable;

/**
 * Runnable/Callable ������������Test��ʹ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2014��8��2��
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
