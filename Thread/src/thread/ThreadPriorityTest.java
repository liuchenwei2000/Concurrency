/**
 * 
 */
package thread;

/**
 * 线程优先级
 * <p>
 * 每一个线程都有一个优先级。对于线程调度器而言，当它有机会挑选一个新的线程运行时，就会先考虑高优先级的线程。
 * 但是，线程优先级是高度依赖操作系统的。当虚拟机依赖于宿主机平台的线程实现机制时，
 * Java线程优先级会映射到宿主机平台上的优先级，宿主机平台的优先级级别可能跟Java里不同。
 * <p>
 * 比如Windows系统中7个优先级级别，而Sun为Linux提供的Java虚拟机中，线程优先级被完全忽略，所有线程具有相同的优先级。
 * 因此，最好仅将线程优先级看做线程调度器的一个参考因素，不可将程序功能的正确性依赖于优先级。
 * <p>
 * 如果某个线程的优先级太低，而其他高优先级的线程又很少发生阻塞，那么低优先级的线程可能永远无法给调度执行，这就是“饿死”。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-7
 */
public class ThreadPriorityTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		showThreadPriority();
		/*
		 * 默认情况下，一个线程继承它的父线程的优先级，父线程就是启动它的那个线程。
		 */
		Thread t1 = new Thread(new PriorityWorker(), "t1");
		t1.start();
		/*
		 * 可以通过setPriority方法设定线程的优先级，共分十级，由Thread类中的**_PRIORITY常量表示。
		 */
		Thread t2 = new Thread(new PriorityWorker(), "t2");
		t2.setPriority(Thread.MAX_PRIORITY);// 最高优先级（10）
		t2.start();

		Thread t3 = new Thread(new PriorityWorker(), "t3");
		t3.setPriority(Thread.MIN_PRIORITY);// 最低优先级（1）
		t3.start();
	}
	
	private static void showThreadPriority(){
		Thread t = Thread.currentThread();
		int priority = t.getPriority();
		System.out.println("Thread " + t.getName() + " priority is " + priority);
	}
	
	private static class PriorityWorker implements Runnable {

		@Override
		public void run() {
			showThreadPriority();
		}
	}
}
