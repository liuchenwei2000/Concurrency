/**
 * 
 */
package thread.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ʹ�� Executor �����з��ؽ��������ʾ��
 * <p>
 * �����з��ؽ����������Ҫ�õ������ӿڣ�
 * 1��Callable
 * ������Ҫʵ�ָýӿڣ�����call()������ʵ��ҵ���߼���������ֵ��
 * 2��Future
 * �ܹ���ȡ Callable ����ķ��ؽ��������������״̬��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��15��
 */
public class TaskReturnsResultTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ʹ�� 2 ���̵߳��̳߳� ���� 5 ���׳˼�������
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
		
		List<Future<Integer>> resultList = new ArrayList<>();
		Random random = new Random();
		
		// 1���� Executor �ύȫ������
		for (int i = 0; i < 5; i++) {
			int number = random.nextInt(10);
			FactorialCalculator task = new FactorialCalculator(number);
			// �� Executor �ύ Callable ������������һ�� Future ����
			// ��ͨ�� Future ����� get()������ȡ�������ʱ�ţ����ܣ���������
			Future<Integer> result = executor.submit(task);
			resultList.add(result);
		}

		// 2����� �������� �Ƿ�ȫ�����
		do {
			System.out.printf("Main: Number of Completed Tasks: %d\n", executor.getCompletedTaskCount());
			for (int i = 0; i < resultList.size(); i++) {
				Future<Integer> result = resultList.get(i);
				// �ж� ���� �Ƿ��Ѿ����
				System.out.printf("Main: Task %d: %s\n", i, result.isDone());
			}

			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (executor.getCompletedTaskCount() < resultList.size());// ������������ͽ�������ʱ�����������������
		
		// 3����ӡ������
		System.out.printf("Main: Results\n");
		for (int i = 0; i < resultList.size(); i++) {
			Future<Integer> result = resultList.get(i);
			Integer number = null;
			try {
				// Future �� get()����������ֱ���������
				number = result.get();
				// Ҳ����ʹ�� ���г�ʱ���Ƶİ汾
//				number = result.get(10, TimeUnit.SECONDS);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.printf("Main: Task %d: %d\n", i, number);
		}

		executor.shutdown();
	}

	/**
	 * �׳˼�����
	 */
	private static class FactorialCalculator implements Callable<Integer> {

		private int n;
		
		public FactorialCalculator(int n) {
			this.n = n;
		}

		/**
		 * ʵ�־����߼������ؽ��
		 * 
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Integer call() throws Exception {
			if (n == 0 || n == 1) {
				return 1;
			}
			int result = 1;
			for (int i = 2; i <= n; i++) {
				result *= i;
				TimeUnit.MICROSECONDS.sleep(20);
			}
			return result;
		}
	}
}
