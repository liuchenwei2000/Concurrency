/**
 * 
 */
package concurrency;

/**
 * 同步语句块
 * <p>
 * <li>1，<p>
 * 当两个并发线程同时访问某一个对象中的synchronized(this)同步代码块时，
 * 只能有一个线程得到执行，另一个线程必须等待当前线程执行完此代码块后才能执行。 
 * <li>2，<p>
 * 当一个线程访问对象的一个synchronized(this)同步代码块时，
 * 另一个线程仍然可以访问该对象中的非synchronized(this)同步代码块。
 * <li>3，<p>
 * 当一个线程访问对象的一个synchronized(this)同步代码块时，
 * 其他线程对此对象中所有其它synchronized(this)同步代码块的访问将被阻塞。  
 * <li>4，<p>
 * 以上规则对其它对象锁（未必非得是this的对象锁）同样适用。
 *
 * @author 刘晨伟
 *
 * 创建日期：2013-6-9
 */
public class SynBlockTest {

	/**
	 * 锁对象
	 * 
	 * 这是JDK5.0之前的用法，如果某个类需要多于一个的锁时（比如需要控制两个或以上不相干属性各自的并发访问问题时），
	 * 可以用这种方式创建多个锁对象，而不再使用当前对象的锁。
	 */ 
	private Object lock1 = new Object();
	
	private Object lock2 = new Object();
	
	/**
	 * synchronized语句块也是JDK5.0之前的写法，5.0之后也可使用Lock/Condition机制加锁。
	 */
	public void method1() {
		// 使用当前对象锁
		synchronized (this) {

		}
		// 出了synchronized语句块，就会释放锁

		// 使用lock1锁
		synchronized (lock1) {

		}

		// 使用lock2锁
		synchronized (lock2) {

		}
	}
}
