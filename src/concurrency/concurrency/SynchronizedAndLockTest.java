/**
 * 
 */
package concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 两种不同并发控制的等价关系
 * <p>
 * 本类的两个方法，采用不同的方式进行并发控制，但效果是等价的。
 * <p>
 * 对于两种方式的选择原则：
 * <li>1，如果synchronized关键字可以工作，那就优先使用它，这样可以减少代码数量，避免出错。
 * <li>2，只有在非常需要Lock/Condition结构的独有特性的时候才使用它们。
 *
 * @author 刘晨伟
 *
 * 创建日期：2013-6-8
 */
public class SynchronizedAndLockTest {

	/**
	 * 使用synchronized实现并发控制
	 */
	public synchronized void method1() throws InterruptedException {
		// do something
		boolean flag = true;// 模拟线程运行的某种条件
		while(flag){
			wait();// 把线程加入到等待集中
		}
		// do something
		// notify();// 解除随机一个等待线程的阻塞状态
		notifyAll();// 解除所有等待线程的阻塞状态
	}
	
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	
	/**
	 * 使用lock对象实现并发控制
	 */
	public void method2() throws InterruptedException {
		lock.lock();
		try {
			// do something
			boolean flag = true;// 模拟线程运行的某种条件
			while(flag){
				condition.await();
			}
			// do something
			// condition.signal();
			condition.signalAll();
			/**
			 * 因为wait和notify、notifyAll方法属于Object类，所以Condition的方法只能用await和signal、signalAll命名
			 * 这样就不会和Object类中的方法名冲突了。
			 */
		} finally {
			lock.unlock();
		}
	}
}