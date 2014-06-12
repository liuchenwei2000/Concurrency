/**
 * 
 */
package thread.synchronizer;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch示例
 * <p>
 * CountDownLatch实现了倒计数锁存器的功能，只不过这个倒计数的操作是原子操作，
 * 同时只能有一个线程去操作这个计数器，也就是同时只能有一个线程去减这个计数器里面的值。
 * <p>
 * 在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
 * <p>
 * 用给定的计数初始化CountDownLatch实例，调用countDown()方法会将计数减1。
 * 在当前计数达到0之前，await方法会一直阻塞，之后锁存器会释放所有等待的线程，await的所有后续调用都将立即返回。
 * 这种现象只出现一次，计数无法被重置，若需要重置计数，可考虑使用CyclicBarrier。
 * <p>
 * 另外，CountDownLatch和CyclicBarrier的区别还可以从下面的角度理解：
 * <li>CountDownLatch:一个线程(或者多个)， 等待另外N个线程完成某个事情之后才能执行。
 * <li>CyclicBarrier: N个线程相互等待，任何一个线程完成之前，所有的线程都必须等待。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-26
 */
public class CountDownLatchTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Team().doWork();
	}
}

/**
 * 模拟某个团队
 * <p>
 * 先由Leader布置任务，然后组员们开始工作，等工作全部完成后，Leader来验收工作。
 */
class Team {

	/**
	 * 任务开始信号，由Leader来发这个信号
	 * <p>
	 * 将计数1初始化的CountDownLatch用作一个简单的开/关锁存器(或叫入口)。
	 * 在通过调用countDown()的线程打开入口前，所有调用await的线程将一直在入口处等待。
	 */
	private CountDownLatch startSignal = new CountDownLatch(1);
	
	/**
	 * 任务完成信号，由4名组员来发这个信号
	 * <p>
	 * 用计数N初始化的CountDownLatch可以使一个线程在N个线程完成某项操作之前一直等待，
	 * 或者使其在某项操作完成N次之前一直等待。
	 */
	private CountDownLatch doneSignal = new CountDownLatch(4);

	/**
	 * 团队开始工作
	 */
	public void doWork() {
		// 每个人代表一个独立的线程
		new Thread(new Leader()).start();
		for (int i = 0; i < 4; i++) {
			new Thread(new Worker("worker" + i)).start();
		}
	}

	/**
	 * Team Leader
	 */
	class Leader implements Runnable {

		@Override
		public void run() {
			try {
				Thread.sleep(1000);
				System.out.println("Leader：开始布置任务...");
				/**
				 * 递减锁存器的计数，如果技术到达0，则释放所有等待的线程；若计数已经是0，则不发生任何操作。
				 * <p>
				 * 调用countDown()方法的线程不需要等待，只有那些调用await方法的线程需要等待。
				 */
				startSignal.countDown();
				System.out.println("Leader：任务布置完了，兄弟们开始干吧，全干完了我来验收！");
				/**
				 * 使当前线程在锁存器倒计数至0之前一直等待，除非线程被中断；若计数已经是0，则立即返回。
				 */
				doneSignal.await();// 需要等待所有组员完成任务
				System.out.println("Leader：我来验收了，兄弟们干的很好！");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 组员
	 */
	class Worker implements Runnable {

		private String name;

		public Worker(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			try {
				System.out.println(name + " 静等 Leader 布置任务...");
				startSignal.await();// 需要等待Leader布置任务
				System.out.println(name + " 开始干活！");
				Thread.sleep((long) (Math.random() * 10000));
				doneSignal.countDown();
				System.out.println(name + " 干完活了！");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}