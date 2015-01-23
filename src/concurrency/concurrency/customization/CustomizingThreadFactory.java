/**
 * 
 */
package concurrency.customization;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * ���� ThreadFactory ʾ��
 * <p>
 * Java�ṩ��ThreadFactory �ӿڣ�����ʵ��һ�� Thread ���󹤳���
 * Java����API��һЩ�߼����ߣ��� Executor framework �� Fork/Join framework ��ʹ���̹߳��������̡߳�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��22��
 */
public class CustomizingThreadFactory {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyThreadFactory threadFactroy = new MyThreadFactory("mythread");
		
		// ����ʹ�� ���Ƶ� ThreadFactory
		Thread thread = threadFactroy.newThread(new SleepOneSecondTask());
		
		thread.start();
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("Main: Thread information %s\n", thread);
		
		// ����ͨ�� ThreadFactory �� Executor ��ܽ�������
		ExecutorService pool = Executors.newCachedThreadPool(threadFactroy);
		
		pool.submit(new SleepOneSecondTask());
		pool.shutdown();
		
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("Main: End of the example.");
	}
	
	/**
	 * �Զ��� ThreadFactory ��
	 * <p>
	 * һ�������Զ�����߳��࣬����Ҫʵ����Ӧ���̹߳��������Ⲣ���Ǳ���ġ�
	 * �������Java�ṩ�Ĳ���API��ʹ���Զ����̣߳�ͨ������Ҫʵ���̹߳�����
	 */
	private static class MyThreadFactory implements ThreadFactory {

		private String prefix;// �߳���ǰ׺
		private int counter;// ������
		
		public MyThreadFactory(String prefix) {
			super();
			this.prefix = prefix;
			this.counter = 1;
		}

		/**
		 * ֻ��Ҫʵ����һ������
		 * 
		 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
		 */
		@Override
		public Thread newThread(Runnable r) {
			return new MyThread(r, prefix + "-" + (counter++));
		}
	}
	
	
	/**
	 * �Զ����߳��࣬���м�¼�̴߳�������ʼ������ʱ��Ĺ��ܡ�
	 */
	private static class MyThread extends Thread {

		private Date creationDate;
		private Date startDate;
		private Date finishDate;

		public MyThread(Runnable target, String name) {
			super(target, name);
			this.creationDate = new Date();
		}

		@Override
		public void run() {
			this.startDate = new Date();
			super.run();
			this.finishDate = new Date();
		}

		/**
		 * ����ʱ��
		 */
		public long getExecutionTime() {
			return finishDate.getTime() - startDate.getTime();
		}

		@Override
		public String toString() {
			StringBuilder buffer = new StringBuilder();
			buffer.append(getName());
			buffer.append(": ");
			buffer.append(" Creation Date: ");
			buffer.append(creationDate);
			buffer.append(" : Running time: ");
			buffer.append(getExecutionTime());
			buffer.append(" ms.");
			return buffer.toString();
		}
	}
	
	private static class SleepOneSecondTask implements Runnable {

		@Override
		public void run() {
			try {
				System.out.println("SleepOneSecondTask��I will sleep 1 second");
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
