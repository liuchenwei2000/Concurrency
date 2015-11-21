/**
 * 
 */
package concurrency.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock机制示例
 * <p>
 * 若希望保护那些多个操作才能更新或检查一个数据结构的代码块，就必须确保这些操作完成之后其他线程才可以使用相同的对象。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-8
 */
public class LockTest {

	// 锁对象，使用可重入锁
	private Lock lock = new ReentrantLock();
	
	/**
	 * 使用lock加锁、释放锁的典型代码
	 * <p>
	 * 与synchronized不同的是，使用lock加锁解锁都需要显式调用。
	 */
	public void test() {
		/*
		 * 这种结构保证在任何时刻只有一个线程能够进入临界区（critical section）。
		 * 
		 * 锁由最近成功获得锁并且还没有释放该锁的线程所拥有。一旦一个线程持有了锁，其他线程都无法通过lock方法再进行加锁，
		 * 当其他线程调用lock方法时，它们会被阻塞，直到持有锁的线程释放锁。
		 */
		lock.lock();
		try {
			// do something
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 一定要在finally中释放锁
			lock.unlock();
		}
	}
}
