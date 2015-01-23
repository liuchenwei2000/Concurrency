/**
 * 
 */
package concurrency.customization;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ���� ThreadPoolExecutor ʾ��
 * <p>
 * �������̳� ThreadPoolExecutor ʵ�־��м�ʱ��ͳ�ƹ��ܵ�ִ������
 * ���ܹ�����ÿ�����������ʱ�䣬������ִ�����ر�ʱִ��ͳ�ƹ�����
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��22��
 */
public class CustomizingThreadPoolExecutor {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyThreadPoolExecutor myExecutor = new MyThreadPoolExecutor(2, 4, 1000,
				TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
		
		for (int i = 0; i < 10; i++) {
			SleepTwoSecondsTask task = new SleepTwoSecondsTask();
			myExecutor.execute(task);
		}
		
		try {
			TimeUnit.SECONDS.sleep(6);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		myExecutor.shutdown();
		
		try {
			myExecutor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("Main: End of the program.\n");
	}
}

/**
 * ���� ThreadPoolExecutor ��
 */
class MyThreadPoolExecutor extends ThreadPoolExecutor {

	// ����������е���ʼʱ��
	private ConcurrentHashMap<String, Date> startTimes;
	
	public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		this.startTimes = new ConcurrentHashMap<>();
	}
	
	/**
	 * ��д����Ҫִ��ǰ����
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread, java.lang.Runnable)
	 */
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		System.out.printf("MyThreadPoolExecutor: A task is beginning: %s : %s\n",
				t.getName(), r.hashCode());
		// ��¼����ִ�е���ʼʱ��
		startTimes.put(String.valueOf(r.hashCode()), new Date());
	}

	/**
	 * ��д����ִ����ɺ���
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
	 */
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		// ������������ʱ��
		Date startDate = startTimes.remove(String.valueOf(r.hashCode()));
		Date finishDate = new Date();
		long diff = finishDate.getTime() - startDate.getTime();
		
		System.out.printf("MyThreadPoolExecutor: A task is finishing. Duration: %d ms\n", diff);
	}

	/**
	 * ��дshutdown()�����������ӡͳ����Ϣ�Ĺ���
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#shutdown()
	 */
	@Override
	public void shutdown() {
		System.out.printf("MyThreadPoolExecutor: Going to shutdown.\n");
		statistics();
		super.shutdown();
	}

	/**
	 * ��дshutdownNow()�����������ӡͳ����Ϣ�Ĺ���
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#shutdownNow()
	 */
	@Override
	public List<Runnable> shutdownNow() {
		System.out.printf("MyThreadPoolExecutor: Going to immediately shutdown.\n");
		statistics();
		return super.shutdownNow();
	}

	private void statistics() {
		// ��ִ�н�����������
		System.out.printf("MyThreadPoolExecutor: Executed tasks: %d\n", getCompletedTaskCount());
		// ���������е�������
		System.out.printf("MyThreadPoolExecutor: Running tasks: %d\n", getActiveCount());
		// ���ύ����δִ�е�������
		System.out.printf("MyThreadPoolExecutor: Pending tasks: %d\n", getQueue().size());
	}
}

class SleepTwoSecondsTask implements Runnable {
	
	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
