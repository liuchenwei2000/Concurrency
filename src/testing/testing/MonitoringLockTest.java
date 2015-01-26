/**
 * 
 */
package testing;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ���������ʾ��
 * <p>
 * ��ʾ Lock ���������ṩ����Ϣ�Լ���λ�ȡ���ǡ�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��26��
 */
public class MonitoringLockTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 5���߳̾���ͬһ����
		MyLock lock = new MyLock();
		
		Thread[] threads = new Thread[5];
		for (int i = 0; i < 5; i++) {
			Task task = new Task(lock);
			threads[i] = new Thread(task);
			threads[i].start();
		}
		
		// ���15�������Ļ�ȡ/�ͷ����
		for (int i=0; i<15; i++) {
			System.out.printf("************************\n");
			System.out.printf("Lock: Owner : %s\n", lock.getOwnerName());
			System.out.printf("Lock: Queued Threads: %s\n", lock.hasQueuedThreads());
			
			if (lock.hasQueuedThreads()) {// �Ƿ����߳��ڵȴ������
				// ���صȴ���������߳�����
				System.out.printf("Lock: Queue Length: %d\n", lock.getQueueLength());
				System.out.printf("Lock: Queued Threads: ");
				
				Collection<Thread> lockedThreads = lock.getThreads();
				for (Thread lockedThread : lockedThreads) {
					System.out.printf("%s ", lockedThread.getName());
				}
				System.out.printf("\n");
			}
			
			System.out.printf("Lock: Fairness: %s\n", lock.isFair());// ���Ƿ��ǹ�ƽģʽ
			System.out.printf("Lock: Locked: %s\n", lock.isLocked());// ���Ƿ�ĳ���̳߳���
			System.out.printf("************************\n");
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * һ���Զ����������
	 * <p>
	 * ReentrantLock ������һЩ protected �������Ի�ȡ���������Ϣ�����Ա���̳�����
	 * ������Щ��Ϣͨ���µ� public ������¶�����Թ���س���ʹ�á�
	 */
	private static class MyLock extends ReentrantLock {

		private static final long serialVersionUID = 1L;
		
		/**
		 * ���ص�ǰ���������߳�����
		 */
		public String getOwnerName() {
			if (getOwner() == null) {
				return "NONE";
			} else {
				// getOwner() ���س��������̶߳���
				return getOwner().getName();
			}
		}
		
		/**
		 * ���ص�ǰ�ȴ�������ļ���
		 */
		public Collection<Thread> getThreads() {
			return this.getQueuedThreads();
		}
	}
	
	private static class Task implements Runnable {

		private Lock lock;
		
		public Task(Lock lock) {
			this.lock = lock;
		}

		/**
		 * ������0.5�����ͷ�
		 */
		@Override
		public void run() {
			for (int i=0; i<5; i++) {
				lock.lock();
				System.out.printf("%s: Get the Lock.\n", Thread.currentThread().getName());
				try {
					TimeUnit.MILLISECONDS.sleep(500);
					System.out.printf("%s: Free the Lock.\n", Thread.currentThread().getName());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		}
	}
}
