/**
 * 
 */
package concurrency;

/**
 * volatile关键字
 * <p>
 * volatile关键字为一个对象实例的域的同步访问提供了一个免锁机制。
 * <p>
 * 在下面三个条件下，对一个域的并发访问是安全的：
 * <li>域是volatile修饰的。
 * <li>对域的访问有锁保护。
 * <li>域是final的，并且在构造器调用完成后被访问。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2007-12-13
 */
public class VolatileTest {

	/**
	 * 使用锁机制对域的同步访问进行控制
	 * <p>
	 * 本例使用synchronized关键字，和使用Lock对象加锁是一样的效果
	 */
	class UseLock {
		
		private boolean done;
		
		/**
		 * 如果另一个线程已经对对象加了锁，本方法再被其他线程调用时将会阻塞
		 */
		public synchronized boolean isDone(){
			return done;
		}
		
		public synchronized void setDone(boolean done){
			this.done = done;
		}
	}

	/**
	 * 使用volatile对域的同步访问进行控制
	 */
	class UseVolatile {
		
		// 把域声明为volatile表明该域会被并发更新/访问，编译器和虚拟机会对其进行并发控制
		private volatile boolean done;
		
		/**
		 * 访问一个volatile变量比访问一个一般变量要慢，这是为线程安全付出的代价
		 */
		public boolean isDone(){
			return done;
		}
		
		public void setDone(boolean done){
			this.done = done;
		}
	}
}