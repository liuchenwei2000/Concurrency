/**
 * 
 */
package testing;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ��� Executor ����ʾ��
 * <p>
 * ��ʾ ThreadPoolExecutor ���������ṩ��״̬��Ϣ�Լ���λ�ȡ���ǡ�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��26��
 */
public class MonitoringExecutorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		
		Random random = new Random();
		// �ύ����ʮ������
		for (int i = 0; i < 10; i++) {
			executor.submit(new Task(random.nextInt(10000)));
		}
		// ��� 5��
		for (int i = 0; i < 5; i++) {
			showLog(executor);
			TimeUnit.SECONDS.sleep(1);
		}
		// �ر��̳߳�
		executor.shutdown();
		System.out.println("Main: Shutdown the thread pool.\n");
		// �ټ�� 5��
		for (int i = 0; i < 5; i++) {
			showLog(executor);
			TimeUnit.SECONDS.sleep(1);
		}
		
		executor.awaitTermination(1, TimeUnit.DAYS);
		System.out.printf("Main: End of the program.\n");
	}
	
	/**
	 * ��ӡ�̳߳�״̬��Ϣ
	 */
	private static void showLog(ThreadPoolExecutor executor) {
		System.out.printf("*********************\n");
		System.out.printf("Main: Executor Log");
		System.out.printf("Main: Executor: Core Pool Size: %d\n",
				executor.getCorePoolSize());// ���غ����߳����������̳߳�û����������ʱ�����е���С�߳���
		System.out.printf("Main: Executor: Pool Size: %d\n",
				executor.getPoolSize());// ���س��ڵ�ǰ�߳���
		System.out.printf("Main: Executor: Active Count: %d\n",
				executor.getActiveCount());// ��������ִ��������߳���
		System.out.printf("Main: Executor: Task Count: %d\n",
				executor.getTaskCount());// �������ƻ�ִ�е���������
		System.out.printf("Main: Executor: Completed Task Count: %d\n",
				executor.getCompletedTaskCount());// ��������ɵ�������
		System.out.printf("Main: Executor: Shutdown: %s\n",
				executor.isShutdown());// �Ƿ��ѵ��� shutdown()�����ر��̳߳�
		System.out.printf("Main: Executor: Terminating: %s\n",
				executor.isTerminating());// �Ƿ��ѵ��� shutdown() ��ط����������̳߳����ر�
		System.out.printf("Main: Executor: Terminated: %s\n",
				executor.isTerminated());// �̳߳��Ƿ��ѹر�
		System.out.printf("*********************\n");
	}

	/**
	 * һ����˯��ָ�����������
	 */
	private static class Task implements Runnable {

		private long ms;
		
		public Task(long ms) {
			this.ms = ms;
		}

		/**
		 * ˯��ָ������
		 */
		@Override
		public void run() {
			System.out.printf("%s: Begin\n", Thread.currentThread().getName());
			try {
				TimeUnit.MILLISECONDS.sleep(ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.printf("%s: End\n", Thread.currentThread().getName());
		}
	}
}
