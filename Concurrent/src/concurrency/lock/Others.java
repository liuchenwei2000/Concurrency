/**
 * 
 */
package concurrency.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 锁的其他一些特点演示
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2007-12-13
 */
public class Others {

	/**
	 * 下面这种方式会创建一个公平锁对象
	 * <p>
	 * 公平锁策略会优待那些等待了最长时间的线程，但是保证公平可能会极大影响性能(比普通锁慢很多)。
	 * 另外，即便使用了公平锁，也不能保证线程调度器是公平的。
	 */
	private ReentrantLock lock = new ReentrantLock(true);
	
	public void method() throws InterruptedException {
		/*
		 * tryLock()方法
		 * <p>
		 * 线程在调用lock方法来获得另一个线程所持有的锁的时候，很可能发生阻塞，所以应该更谨慎的申请锁。
		 * <p>
		 * tryLock方法试图申请一个锁，在成功获得锁后返回true，否则立即返回false，然后线程可以立即离开去做其他事情。
		 * 还可以为尝试获得锁的操作设定时限，到了时限还未获得则返回false。
		 * <p>
		 * 带一个超时参数的tryLock方法在处理公平性时和lock方法是一样的。
		 * 而无参tryLock方法不会考虑公平性，如果它被调用时锁可获得，则当前线程立即获得锁，而不管是否有线程等待了很久。
		 * 如果不想发生这样的事情，可以像下面这种方式一起调用。
		 */
		if (lock.tryLock() || lock.tryLock(10, TimeUnit.SECONDS)) {
			lock.lock();
			try {
				// ...
			} finally {
				lock.unlock();
			}
		} else {
			// do something else
		}
	}
}
