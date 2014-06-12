/**
 * 
 */
package concurrency.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock测试
 * <p>
 * ReentrantLock是可重入的锁。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-8
 */
public class ReentrantLockTest {

	// 可重入锁
	private ReentrantLock lock = new ReentrantLock();
	
	public void test(){
		showThreadHoldCount("test() begins...");
		lock.lock();
		try {
			showThreadHoldCount("test() add lock...");
			/**
			 * 因为线程能够重复的获取它已经拥有的锁， 锁对象维护一个持有计数来追踪对lock方法的嵌套调用。
			 * 线程在每次调用lock后都要调用unlock来释放锁，由于这个特性，被一个锁保护的代码可以调用另一个使用相同锁保护的方法。 
			 * 
			 * 运行原理如下：
			 * test方法调用anotherMethod方法，而anotherMethod方法也会锁住lock对象，现在该lock对象的持有数是2。
			 * 当anotherMethod方法退出后，持有数变回1，当test方法退出后，持有数编程0，线程就把锁释放了。
			 */
			anotherMethod();
			showThreadHoldCount("anotherMethod() done...");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
			showThreadHoldCount("test() remove lock...");
		}
	}

	private void anotherMethod() {
		lock.lock();
		try {
			showThreadHoldCount("anotherMethod() add lock...");
			// do something
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	private void showThreadHoldCount(String msg){
		System.out.println(msg);
		Thread t = Thread.currentThread();
		// 查询当前线程保持此锁的次数。
		int holdCount = lock.getHoldCount();
		System.out.println(t.getName() + " holds lock count=" + holdCount);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ReentrantLockTest().test();
	}
}