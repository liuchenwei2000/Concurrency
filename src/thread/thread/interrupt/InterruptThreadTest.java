/**
 * 
 */
package thread.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * 中断线程示例
 * <p>
 * 当我们需要取消某个任务（结束运行某个线程）时，可以使用中断机制。
 * 中断机制可用来标示某个线程已被要求中止运行，该机制独特的地方在于：
 * 线程自身必须检查自己是否已被中断，并决定是否对中断做出回应（线程完全可以无视中断请求继续执行下去）。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月6日
 */
public class InterruptThreadTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread thread = new Thread(new PrimeGenerator());
		thread.start();
		
		try {
			TimeUnit.MILLISECONDS.sleep(100);
			/*
			 * 线程对象持有一个boolean类型属性来标示是否已被中断：
			 * 当调用 interrupt()方法时会将该属性值设为true；
			 * 调用 isInterrupted()方法将返回该属性值。
			 */
			thread.interrupt();
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Main thread ends.");
	}

	private static class PrimeGenerator implements Runnable {

		private int number = 2;

		@Override
		public void run() {
			while (true) {
				if (isPrime(number)) {
					System.out.println(number);
				}
				// 线程检查中断状态并作出反应
				if (Thread.currentThread().isInterrupted()) {
					System.out.println("Thread is interrupted.");
					/*
					 * Thread.interrupted()方法可判断线程是否已被中断，该方法有副作用，线程的中断状态由该方法清除。
					 * 而thread.isInterrupted()方法没有副作用，不会改变中断状态，因此推荐使用它。
					 */
					if (Thread.interrupted()) {
						System.out.println("Thread after interrupted()");
					}
					if (Thread.interrupted()) {
						System.out.println("Thread after interrupted() again");
					} else {
						/*
						 * isAlive()方法可用来判断线程在当前是否存活着（可运行或被阻塞）。
						 */
						if (Thread.currentThread().isAlive()) {
							System.out.println("Thread is alive!");
						}
						System.out.println("Thread is dead!");
						return;
					}
				}
				number++;
			}
		}

		private boolean isPrime(int n) {
			int max = (int) Math.sqrt(n);
			for (int i = 2; i <= max; i++) {
				if (n % i == 0) {
					return false;
				}
			}
			return true;
		}
	}
}
