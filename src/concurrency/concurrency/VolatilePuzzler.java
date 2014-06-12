/**
 * 
 */
package concurrency;

/**
 * volatile示例
 * <p>
 * volatile修饰的域在被线程访问时，每次都会强制从共享内存中重读该域最新的值。
 * 而且，当域的值发生变化时，也会强制线程将变化值回写到共享内存。
 * 这样在任何时刻，两个不同的线程总是看到某个域的同一个值。
 * <p>
 * volatile一般情况下不能代替sychronized，因为volatile不能保证操作的原子性。
 * 当变量的值由自身的上一个值决定时，如n=n+1、n++等，volatile关键字将失效，
 * 只有当变量的值和自身上一个值无关时对该变量的操作才是原子级别的，如n = m + 1
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2007-12-13
 */
public class VolatilePuzzler {

	public static int n = 0;

	static class Task implements Runnable {

		public void run() {
			try {
				for (int i = 0; i < 10; i++) {
					n++; // n=n+1;也一样
					Thread.sleep(3); // 为了使运行结果更随机，延迟3毫秒
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread threads[] = new Thread[100];
		// 建立100个线程
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Task());
		}
		// 运行刚才建立的100个线程
		for (int i = 0; i < threads.length; i++) { 
			threads[i].start();
		}
		// 确保100个线程都执行完
		for (int i = 0; i < threads.length; i++) { 
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// n 的期望值应该是1000，但实际结果并不是
		System.out.println("n = " + VolatilePuzzler.n);
	}
}