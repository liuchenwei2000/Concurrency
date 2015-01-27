/**
 * 
 */
package testing;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * 监控ForkJoinPool示例
 * <p>
 * 演示 ForkJoinPool 对象所能提供的信息以及如何获取它们。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月27日
 */
public class MonitoringForkJoinPoolTest {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		ForkJoinPool pool = new ForkJoinPool();
		
		int array[] = new int[1000];
		
		Task task = new Task(array, 0, array.length);
		pool.execute(task);

		// 在任务运行期间，每隔1秒打印一次 ForkJoinPool 的状态信息
		while (!task.isDone()) {
			showLog(pool);
			TimeUnit.SECONDS.sleep(1);
		}

		pool.shutdown();
		pool.awaitTermination(1, TimeUnit.DAYS);

		showLog(pool);
		System.out.printf("Main: End of the program.\n");
	}
	
	private static void showLog(ForkJoinPool pool) {
		System.out.printf("**********************\n");
		System.out.printf("Main: Fork/Join Pool log\n");
		System.out.printf("Main: Fork/Join Pool: Parallelism: %d\n",
				pool.getParallelism());// 返回池的并行级次（parallelism level）
		System.out.printf("Main: Fork/Join Pool: Pool Size: %d\n",
				pool.getPoolSize());// 返回池中的工作线程数
		System.out.printf("Main: Fork/Join Pool: Active Thread Count: %d\n",
				pool.getActiveThreadCount());// 返回正在执行任务的工作线程数
		System.out.printf("Main: Fork/Join Pool: Running Thread Count: %d\n",
				pool.getRunningThreadCount());// 返回没有被任何同步机制阻塞的线程数
		System.out.printf("Main: Fork/Join Pool: Queued Submission: %d\n",
				pool.getQueuedSubmissionCount());// 返回已提交到池中但尚未执行的任务数
		System.out.printf("Main: Fork/Join Pool: Queued Tasks: %d\n",
				pool.getQueuedTaskCount());// 返回已经开始执行的任务数
		System.out.printf("Main: Fork/Join Pool: Queued Submissions: %s\n",
				pool.hasQueuedSubmissions());// 判断是否还有已提交到池中但尚未执行的任务
		System.out.printf("Main: Fork/Join Pool: Steal Count: %d\n",
				pool.getStealCount());// 线程间发生工作窃取的次数
		System.out.printf("Main: Fork/Join Pool: Terminated : %s\n",
				pool.isTerminated());// 判断池是否已结束运行
		System.out.printf("**********************\n");
	}
	
	/**
	 * 自定义 RecursiveAction
	 */
	private static class Task extends RecursiveAction {

		private static final long serialVersionUID = 1L;

		private int[] array;

		private int start;
		private int end;

		public Task(int array[], int start, int end) {
			this.array = array;
			this.start = start;
			this.end = end;
		}

		@Override
		protected void compute() {
			if (end - start > 100) {
				int mid = (start + end) / 2;
				Task task1 = new Task(array, start, mid);
				Task task2 = new Task(array, mid, end);
				task1.fork();
				task2.fork();
				task1.join();
				task2.join();
			} else {
				for (int i = start; i < end; i++) {
					array[i]++;
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
