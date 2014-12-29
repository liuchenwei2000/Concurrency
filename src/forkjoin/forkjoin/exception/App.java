/**
 * 
 */
package forkjoin.exception;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2014��12��29��
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

		// �ж������Ƿ���������������task�׳��쳣����������task�׳��쳣�򷵻�true��
		if (task.isCompletedAbnormally()) {
			System.out.printf("Main: An exception has ocurred\n");
			System.out.printf("Main: %s\n", task.getException().getMessage());// �ܹ��õ��쳣����
		}

		System.out.printf("Main: result is %d\n", task.join());
	}
}
