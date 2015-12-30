/**
 * 
 */
package concurrency.collections;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue 示例
 * <p>
 * DelayQueue 类是 Delayed 元素的一个无界阻塞队列，只有在延迟期满时才能从中提取该元素。
 * 遍历队列的相关方法会忽略那些延迟的对象，直到这些对象因为期满被激活。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月16日
 */
public class DelayQueueTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DelayQueue<Event> queue = new DelayQueue<>();

		// 先使用 5 个线程添加元素
		Thread[] threads = new Thread[5];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Task(i, queue));
			threads[i].start();
		}

		// 等待线程全部结束
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.printf("Main: Queue Size: %d\n", queue.size());
		
		// 每隔 500ms 打印一次队列内的元素
		do {
			int counter = 0;
			Event event;
			do {
				// 获取并移除此队列的头，如果此队列不包含具有已到期延迟时间的元素，则返回 null。
				event = queue.poll();
				// 获取并移除此队列的头部，在可从此队列获得到期延迟的元素之前一直等待（如有必要）。
//				event = queue.take();
				// 获取但不移除此队列的头部；如果此队列为空，则返回 null。
//				event = queue.peek();
				if (event != null) {
					counter++;
				}
			} while (event != null);
			System.out.printf("At %s you have read %d events\n", new Date(), counter);
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (queue.size() > 0);// 注意 size() 方法将返回所有元素数（包括延时对象和非延时对象）

		// 完全移除此队列中的所有元素。
//		queue.clear();
		System.out.printf("Main: End of the program\n");
	}
	
	/**
	 * 存入 DelayQueue 的元素需要实现 Delayed 接口，该接口用来标记那些应该在给定延迟时间之后执行的对象。
	 */
	private static class Event implements Delayed {

		private Date activationDate;// 对象激活日期

		public Event(Date activationDate) {
			this.activationDate = activationDate;
		}

		/**
		 * 返回与此对象相关的剩余延迟时间，以给定的时间单位表示。
		 * 
		 * @see java.util.concurrent.Delayed#getDelay(java.util.concurrent.TimeUnit)
		 */
		@Override
		public long getDelay(TimeUnit unit) {
			Date now = new Date();
			long diff = activationDate.getTime() - now.getTime();
			return unit.convert(diff, TimeUnit.MILLISECONDS);
		}
		
		/**
		 * Delayed 接口继承自 Comparable 接口，所以要实现下面的比较方法
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(Delayed o) {
			long result = this.getDelay(TimeUnit.NANOSECONDS)
					- o.getDelay(TimeUnit.NANOSECONDS);
			if (result < 0) {
				return -1;
			} else if (result > 0) {
				return 1;
			}
			return 0;
		}
	}

	/**
	 * 添加元素的任务
	 */
	private static class Task implements Runnable {

		private int id;
		private DelayQueue<Event> queue;
		
		public Task(int id, DelayQueue<Event> list) {
			this.id = id;
			this.queue = list;
		}

		@Override
		public void run() {
			Date now = new Date();
			Date delay = new Date(now.getTime() + (id * 1000));
			System.out.printf("Thread %s: %s\n", id, delay);
			// 添加 100 个延时对象
			for (int i = 0; i < 100; i++) {
				queue.add(new Event(delay));
				// 将指定元素插入此延迟队列。 
//				events.put(new Event(id, i));
			}
		}
	}
}
