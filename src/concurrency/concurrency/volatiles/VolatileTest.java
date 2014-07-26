/**
 * 
 */
package concurrency.volatiles;

/**
 * volatile关键字
 * <p>
 * 在Java内存模型中，有主内存main memory，每个线程也有自己的内存缓冲区(如寄存器)。
 * 为了性能，一个线程会在自己的内存缓冲区中保持要访问的变量的副本。
 * 这样就会出现：
 * 在某个瞬间，同一个变量在线程A内存缓冲区中的值可能与线程B内存缓冲区中的值，或者与主内存中的值不一致的情况。 
 * <p>
 * 实例变量声明为volatile，意味着这个变量是随时会被其他线程修改的，因此不能将它缓存在线程内存缓冲区中。
 * <p>
 * volatile一般情况下不能代替加锁机制，因为volatile不能保证操作的原子性。
 * volatile只能保证不同线程操作的实例域是同一块内存，但依然可能出现写入脏数据的情况。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2007-12-13
 */
public class VolatileTest {

	/**
	 * 使用锁机制对域的同步访问进行控制
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