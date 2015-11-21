/**
 * 
 */
package concurrency.synchronizer;

import java.util.concurrent.Semaphore;

/**
 * Semaphore示例
 * <p>
 * 信号量是一个计数器，用来保护对一个（或多个）共享资源的访问。
 * Semaphore表示一个信号量，它管理大量的许可证，许可的数目是有限的，这样可以限制通过的线程数。
 * 实际上，没有什么许可对象，Semaphore仅维持一个计数器。
 * <p>
 * 当一个线程想要访问共享资源时，首先需要向信号量获取许可，如果信号量的内部计数大于0
 * （意味着有空闲资源可供使用）则将计数减一后允许该线程访问共享资源；
 * 如果等于0（意味着没有空闲资源）则会将该线程阻塞直到内部计数大于0。
 * 当线程访问共享资源结束时，必须释放持有的信号量许可，这样信号量的内部计数会加一。
 * <p>
 * 二进制信号量是一个特例，它保护对唯一共享资源的访问，内部计数只有0和1两个值。
 * <p>
 * 通常用于限制可以访问某些资源的线程数目。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-28
 */
public class SemaphoreTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 模拟8个人上同一个卫生间的竞争情况
		Toilet toilet = new Toilet();
		for (int i = 0; i < 8; i++) {
			new Thread(new ToToiletTask("Person" + (i + 1), toilet)).start();
		}
	}

	/**
	 * 卫生间
	 * <p>
	 * 这是一个稀缺的资源，多个人一起来的时候，必须得排队使用，本类模拟这个过程。
	 */
	private static class Toilet {

		private static final int MAX = 3;

		// 信号量对象，本卫生间只有3个蹲位
		private final Semaphore semaphore = new Semaphore(MAX);
		// 蹲位使用情况
		private boolean[] used = new boolean[MAX];

		/**
		 * 使用厕所
		 */
		public void use() {
			/* 
			 * 下面的try-catch-finally结构是使用Semaphore的典型用法：
			 * 
			 * 1，调用acquire()方法获取Semaphore的许可。
			 * 2，对共享资源进行访问。
			 * 3，调用release()方法释放持有的Semaphore许可。
			 */
			try {
				/*
				 * 从信号量获取一个许可，在提供一个许可之前将一直阻塞此线程。
				 * 获得了一个许可将立即返回，并将可用许可数减1。
				 */
				semaphore.acquire();
				// 在信号量没有许可而导致线程阻塞等待时，可能会发生中断，使用下面的方式将会忽略中断异常。
//				semaphore.acquireUninterruptibly();
				// 下面的方法会测试能否获得许可，如果能则返回true（同时线程将会获得一个许可），不能则立即返回false，而不是等待其他线程释放许可。
//				boolean result = semaphore.tryAcquire();
				int index = getUnusedIndex();
				System.out.println("第 " + (index + 1) + " 个蹲位使用中……");
				Thread.sleep((long) (Math.random() * 2000));// 模拟上厕所
				markUsed(index, false);
				System.out.println("第 " + (index + 1) + " 个蹲位使用完毕!!!");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				/*
				 * 释放一个许可，将信号量可用许可数加1。 
				 * 若任意线程试图获取许可，则选中一个线程并将刚刚释放的许可给予它。
				 */
				semaphore.release();
			}
		}

		/**
		 * 信号量封装所需的同步，用来限制对卫生间的访问，这同维持卫生间本身一致性所需的同步是分开的。
		 * 换句话说，信号量的同步保证了同时只能有有限的线程访问资源(卫生间)，
		 * 而资源自身的同步是为了保证这有限的线程之间竞争(蹲位)时的数据一致性。
		 * 如果没有资源自身的同步，可能会导致两个人上卫生间的同一个蹲位。
		 */
		private synchronized int getUnusedIndex() {
			for (int i = 0; i < used.length; i++) {
				if (!used[i]) {
					markUsed(i, true);
					return i;
				}
			}
			return -1;// 理应不会执行到这里
		}

		private synchronized void markUsed(int index, boolean b) {
			used[index] = b;
		}
	}

	/**
	 * 模拟人上卫生间的过程
	 */
	private static class ToToiletTask implements Runnable {

		private String person;
		private Toilet toilet;

		public ToToiletTask(String person, Toilet toilet) {
			this.person = person;
			this.toilet = toilet;
		}

		@Override
		public void run() {
			try {
				System.out.println(person + " 要上厕所。");
				toilet.use();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
