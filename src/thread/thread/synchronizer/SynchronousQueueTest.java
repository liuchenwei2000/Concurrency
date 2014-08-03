/**
 * 
 */
package thread.synchronizer;

import java.util.concurrent.SynchronousQueue;

/**
 * SynchronousQueue示例
 * <p>
 * 同步队列是一种将生产者和消费者线程配对的机制。
 * 当一个线程调用SynchronousQueue上的put方法时，它会阻塞直到另一个线程调用take方法为止，反之亦然。
 * 与Exchanger不同，同步队列的数据只在一个方向上传递，从生产者到消费者。
 * <p>
 * SynchronousQueue是这样 一种阻塞队列，其中每个put必须等待一个take，反之亦然。
 * 同步队列没有任何内部容量，甚至连一个队列的容量都没有。
 * 不能在同步队列上进行 peek，因为仅在试图要取得元素时，该元素才存在；
 * 除非另一个线程试图移除某个元素，否则也不能（使用任何方法）添加元素；
 * 也不能迭代队列，因为其中没有元素可用于迭代。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-27
 */
public class SynchronousQueueTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new App().start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static class App {
		
		private SynchronousQueue<String> squeue = new SynchronousQueue<String>();
		
		public void start(){
			new Thread(new Worker()).start();
			new Thread(new Consumer()).start();
		}
		
		/**
		 * 生产者
		 */
		private class Worker implements Runnable {

			@Override
			public void run() {
				while (true) {
					try {
						double value = Math.random() * 1000;
						Thread.sleep((long) value);
						System.out.println("worker：put " + value);
						/*
						 * put将指定元素添加到此队列，如有必要则等待另一个线程接收它。
						 */
						squeue.put(value + "");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		/**
		 * 消费者
		 */
		private class Consumer implements Runnable {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep((long) (Math.random() * 1000));
						/*
						 * take获取并移除此队列的头，如有必要则等待另一个线程插入它。
						 */
						System.out.println("consumer：take " + squeue.take());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}