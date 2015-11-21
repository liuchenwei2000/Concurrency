/**
 * 
 */
package concurrency.collections;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * PriorityBlockingQueue 示例
 * <p>
 * PriorityBlockingQueue 是并发环境下使用的有序（无界）列表，其内元素需要实现 Comparable 接口。
 * 当插入元素时，PriorityBlockingQueue 会根据元素实现的 compareTo() 方法来决定它的插入位置，越大的元素越靠后。
 * 此外，PriorityBlockingQueue 是阻塞队列，如果某些操作（比如队列为空时使用poll()方法
 * 获取并删除一个元素）不能立即执行则将会阻塞线程直到操作完成。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月16日
 */
public class PriorityBlockingQueueTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PriorityBlockingQueue<Event> queue = new PriorityBlockingQueue<>();
		
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
		
		System.out.printf("Main: Queue Size: %d\n",queue.size());
		// 主线程开始从 PriorityBlockingQueue 取出元素
		for (int i = 0; i < threads.length * 100; i++) {
			// 获取并移除此队列的头，如果此队列为空，则返回 null。
			Event event = queue.poll();
			System.out.println(event);
		}
		
		System.out.println("501 = " + queue.poll());
		System.out.printf("Main: Queue Size: %d\n", queue.size());
		
		try {
			// 获取并移除此队列的头，在指定的等待时间前等待可用的元素（如果有必要）。
			Event event = queue.poll(5, TimeUnit.SECONDS);
			System.out.println(event == null ? "time out" : event);
			// 获取并移除此队列的头部，在元素变得可用之前一直等待（如果有必要）。
//			event = queue.take();
			// 获取但不移除此队列的头；如果此队列为空，则返回 null。
//			event = queue.peek();
			// 完全移除此队列中的所有元素。
//			queue.clear();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Main: End of the program\n");
	}
	
	private static class Event implements Comparable<Event> {

		private int thread;
		private int priority;
		
		public Event(int thread, int priority) {
			this.thread = thread;
			this.priority = priority;
		}

		/**
		 * 实现比较方法
		 * <p>
		 * 返回值说明：<p>
		 * 1，表示this对象小于参数对象。
		 * 0，表示this对象等于参数对象。
		 * -1，表示this对象大于参数对象。
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(Event o) {
			if (this.priority > o.getPriority()) {
				return -1;
			} else if (this.priority < o.getPriority()) {
				return 1;
			}
			return 0;
		}

		public int getPriority() {
			return priority;
		}

		@Override
		public String toString() {
			return "Event [thread=" + thread + ", priority=" + priority + "]";
		}
	}

	/**
	 * 添加元素的任务
	 */
	private static class Task implements Runnable {

		private int id;
		private PriorityBlockingQueue<Event> events;
		
		public Task(int id, PriorityBlockingQueue<Event> list) {
			this.id = id;
			this.events = list;
		}

		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {
				events.add(new Event(id, i));
				// 将指定元素插入此优先级队列。该队列是无界的，所以此方法不会阻塞。 
//				events.put(new Event(id, i));
			}
		}
	}
}
