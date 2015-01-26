/**
 * 
 */
package forkjoin.customization;

import java.util.Date;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * �Զ���ForkJoinTaskʾ��
 * <p>
 * Customizing tasks running in the Fork/Join framework.
 * <p>
 * Ĭ������£�ʹ�� ForkJoinPool ִ�е������� ForkJoinTask ����
 * ��ȻҲ������ Runnable �� Callable ���󣬵����ǲ������� Fork/Join ��ܵĹ�����ȡ�㷨��
 * <p>
 * һ������£�ֻ��Ҫ�̳��������� ForkJoinTask ��ʵ���༴�ɣ�
 * <li>RecursiveAction: ���������Ҫ���ؽ����
 * <li>RecursiveTask: ���������Ҫ���ؽ����
 * <p>
 * ��Ҳ����ֱ�Ӽ̳�ForkJoinTask��ʵ���Զ������񣬴Ӷ���������������̡�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��26��
 */
public class MyForkJoinTaskTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int array[] = new int[10000];
		ForkJoinPool pool = new ForkJoinPool();
		Task task = new Task("Task", array, 0, array.length);
		// �������ύ�� ForkJoinPool ִ�в��ȴ�����
		pool.invoke(task);
		pool.shutdown();

		System.out.printf("Main: End of the program.\n");
	}

	/**
	 * �Զ��� ForkJoinTask ʾ��
	 * <p>
	 * ��ʵ���������� RecursiveAction���� RecursiveTask�������Ƶġ�
	 */
	private abstract static class MyWorkerTask extends ForkJoinTask<Void> {// Void ��ʾû�з��ؽ��

		private static final long serialVersionUID = 1L;
		
		private String name;// ��������
		
		public MyWorkerTask(String name) {
			this.name = name;
		}

		/** �����������󷽷�����ʵ�� */
		
		/**
		 * ����������
		 */
		@Override
		public Void getRawResult() {
			// ��Ϊ������û�н�������Է���null
			return null;
		}

		/**
		 * ����������
		 */
		@Override
		protected void setRawResult(Void value) {
			// ��Ϊ������û�н�������Կ�ʵ��
		}

		/**
		 * �����߼��ľ���ʵ��
		 * <p>
		 * ����������ʵ��ί�и� compute() ��������ͳ�ƴ�ӡ�÷�������ʱ�䡣
		 */
		@Override
		protected boolean exec() {
			Date startDate = new Date();
			compute();
			Date finishDate = new Date();
			long diff = finishDate.getTime() - startDate.getTime();
			System.out.printf("MyWorkerTask: %s : %d Milliseconds to complete.\n", name, diff);
			return true;
		}
		
		/**
		 * �����������ĳ��󷽷�������ʵ�־���������߼���
		 */
		protected abstract void compute();

		public String getName() {
			return name;
		}
	}
	
	/**
	 * һ����������ʵ��
	 */
	private static class Task extends MyWorkerTask {
		
		private static final long serialVersionUID = 1L;
		
		private int array[];
		
		private int start;
		private int end;
		
		public Task(String name, int array[], int start, int end) {
			super(name);
			this.array = array;
			this.start = start;
			this.end = end;
		}
		
		/**
		 * ʵ�ָ���� compute() ����
		 */
		@Override
		protected void compute() {
			if (end - start > 100) {
				int mid = (end + start) / 2;
				Task task1 = new Task(this.getName() + "1", array, start, mid);
				Task task2 = new Task(this.getName() + "2", array, mid, end);
				invokeAll(task1, task2);
			} else {
				for (int i = start; i < end; i++) {
					array[i]++;
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
