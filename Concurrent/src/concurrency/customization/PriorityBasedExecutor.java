/**
 * 
 */
package concurrency.customization;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 实现基于优先级的执行器示例
 * <p>
 * 在内部，执行器使用阻塞队列来存储待执行任务，这些任务按照到达执行器的先后顺序排列。
 * 另一个可能的选择是使用优先队列存储待执行任务，在这种方式下，
 * 一个具有较高优先级的新任务到达执行器后会比那些更早到达但优先级低的任务先执行。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月22日
 */
public class PriorityBasedExecutor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 1，使用 优先队列 PriorityBlockingQueue 作为存储待执行任务的队列
		ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 1,
				TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());

		for (int i = 0; i < 4; i++) {
			MyPriorityTask task = new MyPriorityTask("Task " + i, i);
			executor.execute(task);
		}

		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (int i = 4; i < 8; i++) {
			MyPriorityTask task = new MyPriorityTask("Task " + i, i);
			executor.execute(task);
		}

		executor.shutdown();

		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.printf("Main: End of the program.\n");
	}
	
	/**
	 * 2，具有优先级的任务需要实现 Runnable、Comparable 两个接口
	 */
	private static class MyPriorityTask implements Runnable, Comparable<MyPriorityTask> {

		private String name;
		private int priority;
		
		public MyPriorityTask(String name, int priority) {
			this.name = name;
			this.priority = priority;
		}

		@Override
		public int compareTo(MyPriorityTask o) {
			// 优先级高的排在前面
			if(this.getPriority() > o.getPriority()) {
				return -1;
			}else if (this.getPriority() < o.getPriority()){
				return 1;
			}
			return 0;
		}
		
		@Override
		public void run() {
			System.out.printf("Running %s Priority : %d\n", name, priority);
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public int getPriority() {
			return priority;
		}
	}
}
