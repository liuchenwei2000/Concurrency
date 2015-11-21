/**
 * 
 */
package concurrency.collections;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * ConcurrentLinkedDeque 示例
 * <p>
 * 并发List允许多个线程同时增加或删除元素而不会造成数据不一致。
 * Java7引入的 ConcurrentLinkedDeque 实现了非阻塞并发List功能。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月16日
 */
public class ConcurrentLinkedDequeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConcurrentLinkedDeque<String> list = new ConcurrentLinkedDeque<>();
		
		// 先使用 100 个线程添加元素
		Thread[] addTasks = new Thread[100];
		for (int i = 0; i < addTasks.length; i++) {
			addTasks[i] = new Thread(new AddTask(list));
			addTasks[i].start();
		}
		System.out.printf("Main: %d AddTask threads have been launched\n", addTasks.length);
		
		// 等待线程全部结束
		for (Thread thread : addTasks) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		/*
		 * 获取List元素数量可以通过 size() 方法，但要记住该方法的返回值并非是完全准确的，特别是
		 * 当调用该方法时尚有其他线程增加或删除元素。所以只有在确保没有其他线程修改List时，该方法才会返回准确的值。
		 */
		System.out.printf("Main: Size of the List: %d\n", list.size());// 这里应该返回 10000
		
		// 再用 100 个线程删除元素
		Thread[] pollTasks = new Thread[100];
		for (int i = 0; i < pollTasks.length; i++) {
			pollTasks[i] = new Thread(new PollTask(list));
			pollTasks[i].start();
		}
		System.out.printf("Main: %d PollTask threads have been launched\n", pollTasks.length);
		
		// 等待线程全部结束
		for (Thread thread : pollTasks) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("Main: Size of the List: %d\n", list.size());// 这里应该返回 0
		
	}

	/**
	 * 添加元素的任务
	 */
	private static class AddTask implements Runnable {

		private ConcurrentLinkedDeque<String> list;
		
		public AddTask(ConcurrentLinkedDeque<String> list) {
			this.list = list;
		}

		@Override
		public void run() {
			String name = Thread.currentThread().getName();
			for (int i = 0; i < 100; i++) {
				list.add(name + "-" + i);
			}
		}
	}
	
	/**
	 * 删除元素的任务
	 */
	private static class PollTask implements Runnable {

		private ConcurrentLinkedDeque<String> list;
		
		public PollTask(ConcurrentLinkedDeque<String> list) {
			this.list = list;
		}

		@Override
		public void run() {
			for (int i = 0; i < 50; i++) {
				// 下面的方法可以获取元素并删除，如果列表为空则返回 null
				list.pollFirst();// 首个元素
				list.pollLast();// 最后的元素
				
				// 下面的方法可以只获取元素而不做其他操作，如果列表为空则抛出 NoSuchElementExcpetion
//				list.getFirst();
//				list.getLast();
				
				// 下面的方法可以只获取元素而不做其他操作，如果列表为空则返回 null
//				list.peekFirst();
//				list.peekLast();
				
				// 下面的方法可以获取元素并删除，如果列表为空则抛出 NoSuchElementExcpetion
//				list.removeFirst();
//				list.removeLast();
			}
		}
	}
}
