/**
 * 
 */
package forkjoin.task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * ForkJoinPool����RecursiveTaskʾ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2014��12��25��
 */
public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 10000;
		// ���������Task����
		RecursiveTask<Integer> task = new SumTask(0, n);
		
		// ����Ĭ��ForkJoinPoolʵ����������������CPU��
		ForkJoinPool pool = new ForkJoinPool();
		// ִ����������һ���첽���ã����̻߳��������ִ�С�
		pool.execute(task);
		
		// ���ForkJoinPool������ʱ����
		do {
			try {
				TimeUnit.MILLISECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!task.isDone());// �ж������Ƿ��Ѿ����������������������쳣��ֹ������ȡ����
		
		// ֹͣpool������
		pool.shutdown();
		
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// �������������Ĳ����Ƿ���ȷ
		try {
			System.out.printf("��ForkJoinPool��Sum of %d: %d\n", n, task.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		System.out.printf("��Formula��Sum of %d: %d\n", n, n*(n-1)/2);
		System.out.println("Main: End of the program.\n");
	}
}
