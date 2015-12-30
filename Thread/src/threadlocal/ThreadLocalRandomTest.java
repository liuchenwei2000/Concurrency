/**
 * 
 */
package threadlocal;

import java.util.concurrent.ThreadLocalRandom;

/**
 * ThreadLocalRandom示例
 * <p>
 * ThreadLocalRandom 是Java7开始才提供的工具类，用来在并发环境下生成随机数。它就像是线程局部变量，
 * 每个想生成随机数的线程将会持有一个自己的生成器，但这些生成器被ThreadLocalRandom 类统一管理——这对客户端是透明的。
 * 在多线程并发访问的情况下，使用ThreadLocalRandom 比使用Math.random()可以减少并发线程之间的竞争，从而获得更好的性能。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年12月30日
 */
public class ThreadLocalRandomTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 3;
		for (int i = 0; i < n; i++) {
			new Thread(new Task()).start();
		}
	}

	private static class Task implements Runnable {
		
		public Task() {
			// 初始化本线程使用的随机数生成器
			ThreadLocalRandom.current();
		}

		@Override
		public void run() {
			String name = Thread.currentThread().getName();
			for (int i = 0; i < 10; i++) {
				// 生成10以内的随机数
				int randomInt = ThreadLocalRandom.current().nextInt(10);
				System.out.printf("%s: %d\n", name, randomInt);
			}
		}
	}
}
