/**
 * 
 */
package concurrency.customization;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ʵ�ֻ������ȼ���ִ����ʾ��
 * <p>
 * ���ڲ���ִ����ʹ�������������洢��ִ��������Щ�����յ���ִ�������Ⱥ�˳�����С�
 * ��һ�����ܵ�ѡ����ʹ�����ȶ��д洢��ִ�����������ַ�ʽ�£�
 * һ�����нϸ����ȼ��������񵽴�ִ����������Щ���絽�ﵫ���ȼ��͵�������ִ�С�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��22��
 */
public class PriorityBasedExecutor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 1��ʹ�� ���ȶ��� PriorityBlockingQueue ��Ϊ�洢��ִ������Ķ���
		ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 1,
				TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());

		for (int i = 0; i < 4; i++) {
			MyPriorityTask task = new MyPriorityTask("Task " + i, i);
			executor.execute(task);
		}

		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (int i = 4; i < 8; i++) {
			MyPriorityTask task = new MyPriorityTask("Task " + i, i);
			executor.execute(task);
		}

		executor.shutdown();

		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.printf("Main: End of the program.\n");
	}
	
	/**
	 * 2���������ȼ���������Ҫʵ�� Runnable��Comparable �����ӿ�
	 */
	private static class MyPriorityTask implements Runnable, Comparable<MyPriorityTask> {

		private String name;
		private int priority;
		
		public MyPriorityTask(String name, int priority) {
			this.name = name;
			this.priority = priority;
		}

		@Override
		public int compareTo(MyPriorityTask o) {
			// ���ȼ��ߵ�����ǰ��
			if(this.getPriority() > o.getPriority()) {
				return -1;
			}else if (this.getPriority() < o.getPriority()){
				return 1;
			}
			return 0;
		}
		
		@Override
		public void run() {
			System.out.printf("Running %s Priority : %d\n", name, priority);
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public int getPriority() {
			return priority;
		}
	}
}
