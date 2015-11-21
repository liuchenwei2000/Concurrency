/**
 * 
 */
package concurrency;

/**
 * synchronized关键字示例
 * <p>
 * 需要注意的是，每一个对象都有它自己的锁对象，如果两个线程试图访问同一个对象，锁就会串行的服务于访问。
 * 但是，如果两个线程访问不同的对象，那么每一个线程都会得到一个不同的锁，两者都不会发生阻塞。
 * 关键是搞清楚synchronized锁定的是哪个对象。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2007-12-13
 */
public class SynchronizedTest {

	/**
	 * 把synchronized当作方法修饰符时表示方法是同步的。
	 * 它锁定的是调用这个同步方法的对象，其他对象仍可调用这个方法。
	 */
	public synchronized void method1() {
		// ...
	}
	
	/**
	 * method1()相当于下面这种同步块
	 */
	public void method11() {
		synchronized (this) {
			// ...
		}
	}

	/**
	 * synchronized所加的隐式锁是可重入的。
	 * 如果一个线程已经获得了锁，它可以再次获得它，同时会增加锁的持有计数。
	 * 比如下面的同步方法中调用了另一个同步方法，它可以直接进入而不需要等待锁释放。
	 */
	public synchronized void method111() {
		method1();
	}
	
	/**
	 * 下面这个方法中锁的就是obj这个对象，谁拿到这个锁谁就可以运行它所控制的那段代码 。
	 * 当有一个明确的对象作为锁时，就可以这样写程序。
	 */
	public void method2(Object obj) {
		synchronized (obj) {
			// ...
		}
	}
	
	/**
	 * 当没有明确的对象作为锁，只是想让一段代码同步时，可以创建一个特殊的instance变量(它得是一个对象)来充当锁。
	 */
	// 特殊的实例变量(这种类型开销最少)
	private byte[] lock = new byte[0];
	
	public void method3() {
		synchronized (lock) {
			// ...
		}
	}
	
	/**
	 * 防止多个线程同时访问这个类中的静态方法，锁住的是class对象，这个锁对类的所有实例都起作用。
	 */
	public synchronized static void method4() {
		// ...
	}

	/**
	 * 和synchronized static方法产生的效果是一样的
	 */
	public static void method44() {
		// 锁住的是 class对象
		synchronized (SynchronizedTest.class) {
			// ...
		}
	}
}
