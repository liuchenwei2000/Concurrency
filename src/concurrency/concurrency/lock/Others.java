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
	 * 如果线程调度器选择忽略一个等待了很长时间的线程，那此线程根本没机会得到锁的公平对待。
	 */
	private ReentrantLock lock = new ReentrantLock(true);
	
	public void method() throws InterruptedException {
		/**
		 * tryLock()方法
		 * <p>
		 * 如果锁没有被另一个线程保持，该方法立即返回true值，当前线程将持有锁，并将锁的保持计数设置为1。
		 * 如果当前线程已经持有锁，则将保持计数加1，该方法将返回true 。
		 * 如果锁定被另一个线程保持，此方法将立即返回false值。
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