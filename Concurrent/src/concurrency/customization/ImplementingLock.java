/**
 * 
 */
package concurrency.customization;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 实现自定义的Lock类示例
 * <p>
 * 锁是Java并发API提供的基本同步机制之一。
 * 它允许程序员保护代码的临界区，也就是说在某个时刻只有一个线程能执行这个临界区代码块。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月23日
 */
public class ImplementingLock {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyLock lock = new MyLock();
		
		for (int i = 0; i < 10; i++) {
			new Thread(new ShareLockTask("Task-" + i, lock)).start();
		}
		
		// 主线程也参与 锁 的获取和释放
		boolean value;
		do {
			try {
				// 使用tryLock()方法尝试获取锁。等待1秒，如果无法获得锁则打印一条信息并重新尝试。
				value = lock.tryLock(1, TimeUnit.SECONDS);
				if (!value) {
					System.out.printf("Main: Trying to get the Lock\n");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				value = false;
			}
		} while (!value);
		
		System.out.printf("Main: Got the lock\n");
		lock.unlock();// 释放锁
		
		System.out.printf("Main: End of the program\n");
	}

	/**
	 * 自定义同步器
	 * <p>
	 * Java并发API提供了 AbstractQueuedSynchronizer 类用来实现拥有锁或信号量特征的同步机制。
	 * 它是一个抽象类，它提供如下两个操作：控制对临界区的访问、管理正在等待访问临界区的阻塞线程队列。
	 * 这两个操作是基于以下两个抽象方法：tryAcquire、tryRelease
	 * <p>
	 * AbstractQueuedSynchronizer 类的其他实现类都是私有的内部类，所以不能直接使用它们。
	 */
	private static class MyQueuedSynchronizer extends AbstractQueuedSynchronizer {

		private static final long serialVersionUID = 1L;
		
		/*
		 * 使用 AtomicInteger 原子变量来控制对临界区的访问。
		 * 如果锁是自由的，这个变量的值为0，表明线程可以访问临界区。
		 * 如果锁是阻塞的，这个变量的值为1，表明线程不能访问这个临界区。
		 */
		private AtomicInteger state;

		public MyQueuedSynchronizer() {
			this.state = new AtomicInteger(0);
		}

		/**
		 * 当尝试访问临界区时调用这个方法。
		 * <p>
		 * 若调用这个方法的线程可以访问临界区，此方法返回true，否则返回false。
		 * 
		 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#tryAcquire(int)
		 */
		@Override
		protected boolean tryAcquire(int arg) {
			// 该方法试图将变量state的值从0变成1。如果成功，则返回true，否则返回false。
			return state.compareAndSet(0, 1);
		}
		
		/**
		 * 当尝试退出对临界区的访问时调用这个方法。
		 * <p>
		 * 若调用这个方法的线程可以释放对临界区的访问，此方法返回true，否则返回false。
		 * 
		 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#tryAcquire(int)
		 */
		@Override
		protected boolean tryRelease(int arg) {
			// 该方法试图将变量state的值从1变成0。如果成功，则返回true，否则返回false。
			return state.compareAndSet(1, 0);
		}
	}
	
	/**
	 * 自定义的锁
	 */
	private static class MyLock implements Lock {

		private AbstractQueuedSynchronizer synchronizer;
		
		public MyLock() {
			this.synchronizer = new MyQueuedSynchronizer();
		}

		/**
		 * 当将要访问一个临界区时，调用这个方法。
		 * 如果有线程正在访问这个临界区，其他线程将阻塞，直到锁释放后它们被唤醒，从而获取对临界区的访问权。
		 * 
		 * @see java.util.concurrent.locks.Lock#lock()
		 */
		@Override
		public void lock() {
			synchronizer.acquire(1);
		}

		@Override
		public void lockInterruptibly() throws InterruptedException {
			synchronizer.acquireInterruptibly(1);
		}

		@Override
		public boolean tryLock() {
			try {
				return synchronizer.tryAcquireNanos(1, 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		public boolean tryLock(long time, TimeUnit unit)
				throws InterruptedException {
			try {
				return synchronizer.tryAcquireNanos(1, TimeUnit.NANOSECONDS.convert(time, unit));
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}

		/**
		 * 在临界区结束时调用这个方法，以便释放锁允许其他线程访问此临界区。
		 * 
		 * @see java.util.concurrent.locks.Lock#unlock()
		 */
		@Override
		public void unlock() {
			synchronizer.release(1);
		}

		@Override
		public Condition newCondition() {
			// 创建 synchronizer 对象的内部类 ConditionObject 实例
			return synchronizer.new ConditionObject();
		}
	}
	
	private static class ShareLockTask implements Runnable {
		
		private MyLock lock;
		private String name;
		
		public ShareLockTask(String name, MyLock lock) {
			this.name = name;
			this.lock = lock;
		}

		/**
		 * 获得锁后睡眠2秒然后释放锁
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			lock.lock();
			System.out.printf("Task %s: Take the lock\n", name);
			try {
				TimeUnit.SECONDS.sleep(2);
				System.out.printf("Task %s: Free the lock\n", name);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}
}
