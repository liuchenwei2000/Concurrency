/**
 * 
 */
package forkjoin.action;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * ForkJoinPool����RecursiveActionʾ��
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
		List<Product> products = ProductFactory.create(10000);
		
		// ���������Task����
		RecursiveAction task = new UpdatePriceTask(products, 0, products.size(), 0.2);
		
		// ����Ĭ��ForkJoinPoolʵ����������������CPU��
		ForkJoinPool pool = new ForkJoinPool();
		// ִ����������һ���첽���ã����̻߳��������ִ�С�
		pool.execute(task);
		
		// ���ForkJoinPool������ʱ����
		do {
			System.out.printf("Main: Thread Count: %d\n", pool.getActiveThreadCount());
			System.out.printf("Main: Thread Steal: %d\n", pool.getStealCount());
			System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
			try {
				TimeUnit.MILLISECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!task.isDone());// �ж������Ƿ��Ѿ����������������������쳣��ֹ������ȡ����
		
		// ֹͣpool������
		pool.shutdown();
		// �ж������Ƿ��������������쳣��ֹ������ȡ��������������������
		if (task.isCompletedNormally()) {
			System.out.printf("Main: The process has completed normally.\n");
		}
		
		// �������������Ĳ����Ƿ���ȷ
		for (int i = 0; i < products.size(); i++) {
			Product product = products.get(i);
			if (product.getPrice() != 12) {
				System.out.printf("Product %s: %f\n", product.getName(), product.getPrice());
			}
		}
		
		System.out.println("Main: End of the program.\n");
	}
}
