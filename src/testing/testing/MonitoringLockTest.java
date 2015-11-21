/**
 * 
 */
package testing;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 监控锁对象示例
 * <p>
 * 演示 Lock 对象所能提供的信息以及如何获取它们。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月26日
 */
public class MonitoringLockTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 5个线程竞争同一个锁
		MyLock lock = new MyLock();
		
		Thread[] threads = new Thread[5];
		for (int i = 0; i < 5; i++) {
			Task task = new Task(lock);
			threads[i] = new Thread(task);
			threads[i].start();
		}
		
		// 监控15秒内锁的获取/释放情况
		for (int i=0; i<15; i++) {
			System.out.printf("************************\n");
			System.out.printf("Lock: Owner : %s\n", lock.getOwnerName());
			System.out.printf("Lock: Queued Threads: %s\n", lock.hasQueuedThreads());
			
			if (lock.hasQueuedThreads()) {// 是否还有线程在等待获得锁
				// 返回等待获得锁的线程数量
				System.out.printf("Lock: Queue Length: %d\n", lock.getQueueLength());
				System.out.printf("Lock: Queued Threads: ");
				
				Collection<Thread> lockedThreads = lock.getThreads();
				for (Thread lockedThread : lockedThreads) {
					System.out.printf("%s ", lockedThread.getName());
				}
				System.out.printf("\n");
			}
			
			System.out.printf("Lock: Fairness: %s\n", lock.isFair());// 锁是否是公平模式
			System.out.printf("Lock: Locked: %s\n", lock.isLocked());// 锁是否被某个线程持有
			System.out.printf("************************\n");
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 一个自定义的锁对象
	 * <p>
	 * ReentrantLock 类内有一些 protected 方法可以获取锁的相关信息，所以本类继承它，
	 * 并把这些信息通过新的 public 方法暴露出来以供监控程序使用。
	 */
	private static class MyLock extends ReentrantLock {

		private static final long serialVersionUID = 1L;
		
		/**
		 * 返回当前持有锁的线程名称
		 */
		public String getOwnerName() {
			if (getOwner() == null) {
				return "NONE";
			} else {
				// getOwner() 返回持有锁的线程对象
				return getOwner().getName();
			}
		}
		
		/**
		 * 返回当前等待获得锁的集合
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
		 * 持有锁0.5秒再释放
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
