/**
 * 
 */
package forkjoin.cancel;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * �������в���ָ����ֵ������
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
		int[] data = new ArrayGenerator().generate(100);
		TaskManager taskManager = new TaskManager();
		
		SearchNumberTask task = new SearchNumberTask(data, 0, data.length, 7, taskManager);
		
		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(task);
		
		pool.shutdown();
		
		try {
			pool.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("Main: The program has finished\n");
	}
}
