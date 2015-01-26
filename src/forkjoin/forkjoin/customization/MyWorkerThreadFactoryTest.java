/**
 * 
 */
package forkjoin.customization;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * Fork/Join���ʹ�õ��Զ����̹߳���ʾ��
 * <p>
 * Implementing the ThreadFactory interface to generate custom threads for the Fork/Join framework.
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��26��
 */
public class MyWorkerThreadFactoryTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyWorkerThreadFactory factory=new MyWorkerThreadFactory();
		
		ForkJoinPool pool=new ForkJoinPool(4, factory, null, false);
		
		MyRecursiveTask task= new MyRecursiveTask(0, 100000);
		pool.execute(task);
		
		task.join();
		
		pool.shutdown();
		
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
			System.out.printf("Main: Result: %d\n", task.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.printf("Main: End of the program\n");
	}
	
	/**
	 * �Զ���WorkerThreadFactory
	 * <p>
	 * ForkJoinPool ������Java����APIһ����ʹ�ù����������̶߳���
	 * �̹߳�����Ҫʵ��ForkJoinPool.ForkJoinWorkerThreadFactory �ࡣ
	 */
	private static class MyWorkerThreadFactory implements ForkJoinWorkerThreadFactory {

		@Override
		public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
			return new MyWorkerThread(pool);
		}
	}
	
	/**
	 * �Զ���WorkerThreadʾ��
	 * <p>
	 * Fork/Join ���ʹ�õ��̳߳�Ϊ�����̣߳�worker thread����Javaʹ�ü̳��� Thread ��� ForkJoinWorkerThread ��ʵ�ֹ����̡߳�
	 * <p>
	 * ͳ�ƹ����߳������˶�������
	 */
	private static class MyWorkerThread extends ForkJoinWorkerThread {
		
		// ʹ��ThreadLocal������Ϊ����ÿ���̶߳���һ����������
		private static ThreadLocal<Integer> counter = new ThreadLocal<Integer>();

		protected MyWorkerThread(ForkJoinPool pool) {
			super(pool);
		}

		/**
		 * �������߳̿�ʼִ��ʱ������
		 * 
		 * @see java.util.concurrent.ForkJoinWorkerThread#onStart()
		 */
		@Override
		protected void onStart() {
			super.onStart();
			counter.set(0);// ��ʼ��������
			System.out.printf(
					"MyWorkerThread %d: Initializing task counter.\n", getId());
		}

		/**
		 * �������߳̽�������ʱ������
		 * <p>
		 * ���������������쳣�������������������쳣����ʱ��Exception���������ֵΪnull�����������������
		 * 
		 * @see java.util.concurrent.ForkJoinWorkerThread#onTermination(java.lang.Throwable)
		 */
		@Override
		protected void onTermination(Throwable exception) {
			// ��ӡ���߳�ִ�е�������
			System.out
					.printf("MyWorkerThread %d: %d\n", getId(), counter.get());
			super.onTermination(exception);
		}

		/**
		 * ���������ʱ����������
		 */
		public void addTask() {
			counter.set(counter.get() + 1);
		}
	}
	
	/**
	 * �� Fork/Join ��������е�����
	 */
	private static class MyRecursiveTask extends RecursiveTask<Integer> {
		
		private static final long serialVersionUID = 1L;

		/** ��ֵ */
		private static final int DEFAULT_SIZE = 100;
		
		private int first;// ��һ����������ֵ 
		private int last;// ���һ����������ֵ 
		
		public MyRecursiveTask(int first, int last) {
			this.first = first;
			this.last = last;
		}

		/**
		 * ��д�÷�����ʵ���ض�ҵ���߼�
		 * 
		 * @see java.util.concurrent.RecursiveTask#compute()
		 */
		@Override
		protected Integer compute() {
			// ÿ����һ�����߳�ִ�������� +1
			((MyWorkerThread)Thread.currentThread()).addTask();
			
			Integer result = 0;
			if(last - first > DEFAULT_SIZE) {
				int middle = (last + first) / 2;
				
				MyRecursiveTask task1 = new MyRecursiveTask(first, middle + 1);
				MyRecursiveTask task2 = new MyRecursiveTask(middle + 1, last); 
				
				invokeAll(task1, task2);
				
				try {
					Integer result1 = task1.get();
					Integer result2 = task2.get();
					result = groupResults(result1, result2);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			} else { 
				result = sum();
			}
			return result;
		}

		private Integer groupResults(Integer integer1, Integer integer2) {
			return integer1 + integer2;
		}

		private Integer sum() {
			int sum = 0;
			for (int i = first; i < last; i++) {
				sum += i;
			}
			return sum;
		}
	}
}
