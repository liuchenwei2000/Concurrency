/**
 * 
 */
package thread.synchronizer;

import java.util.concurrent.Semaphore;

/**
 * Semaphore示例
 * <p>
 * Semaphore表示一个信号量，它管理大量的许可证，许可的数目是有限的，这样可以限制通过的线程数。
 * 实际上，没有什么许可对象，Semaphore仅维持一个计数器。
 * 如有必要，在许可可用前会阻塞每一个acquire()，然后再获取该许可。
 * 每个release()添加一个许可，从而可能释放一个正在阻塞的获取者。
 * 而且，许可不需要由获取它的线程释放，任意线程都可以释放。这种灵活性也带来了潜在的混乱。
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
		private Semaphore semaphore = new Semaphore(MAX);
		// 蹲位使用情况
		private boolean[] used = new boolean[MAX];

		/**
		 * 请求一个蹲位，返回蹲位序号
		 */
		public int in() throws Exception {
			/**
			 * 从信号量获取一个许可，在提供一个许可之前将一直阻塞此线程。
			 * 获得了一个许可将立即返回，并将可用许可数减1。
			 */
			semaphore.acquire();
			return getUnusedIndex();
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

		/**
		 * 离开指定蹲位
		 */
		public void out(int index) {
			markUsed(index, false);
			/**
			 * 释放一个许可，将信号量可用许可数加1。
			 * 若任意线程试图获取许可，则选中一个线程并将刚刚释放的许可给予它。
			 */
			semaphore.release();
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
				int index = toilet.in();
				System.out.println(person + " 进了第 " + (index + 1) + " 个蹲位。");
				Thread.sleep((long) (Math.random() * 2000));// 模拟上厕所
				System.out.println(person + " 上完厕所出来了。");
				toilet.out(index);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}