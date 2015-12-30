/**
 * 
 */
package thread.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 控制 Executor 拒绝接收的任务示例
 * <p>
 * 当 Executor 调用了shutdown()方法后，它就不再接收新的任务。
 * 可以通过为其设定 RejectedExecutionHandler 来管理被拒绝的任务。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月15日
 */
public class ControlRejectedTaskOfExecutorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RejectedTaskController controller = new RejectedTaskController();
		
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		// 为 executor 设置 RejectedExecutionHandler
		executor.setRejectedExecutionHandler(controller);

		System.out.printf("Main: Starting.\n");
		for (int i = 0; i < 3; i++) {
			Task task = new Task("Task" + i);
			executor.submit(task);
		}

		System.out.printf("Main: Shutting down the Executor.\n");
		executor.shutdown();

		System.out.printf("Main: Sending another Task.\n");
		Task task = new Task("RejectedTask");
		/*
		 * 在 Executor 调用 shutdown()后继续提交任务，该任务会被拒绝。
		 * 
		 * 如果 Executor 有 RejectedExecutionHandler，则任务被拒绝时会调用rejectedExecution()方法；
		 * 如果没有则会抛出 RejectedExecutionExeption。
		 */
		executor.submit(task);

		System.out.println("Main: End");
	}

	/**
	 * 被拒绝的任务控制器
	 */
	private static class RejectedTaskController implements RejectedExecutionHandler {

		/**
		 * 当某个任务被 Executor 拒绝时该方法就会被调用
		 * 
		 * @param r
		 *            被拒绝的任务
		 * @param executor
		 *            拒绝了该任务的执行器
		 */
		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			System.out.printf(
					"RejectedTaskController: The task %s has been rejected\n",
					r.toString());
			System.out.printf("RejectedTaskController: %s\n",
					executor.toString());
			System.out.printf("RejectedTaskController: Terminating: %s\n",
					executor.isTerminating());
			System.out.printf("RejectedTaksController: Terminated: %s\n",
					executor.isTerminated());
		}
	}
	
	/**
	 * 普通的任务
	 */
	private static class Task implements Runnable {
		
		private String name;

		public Task(String name) {
			this.name = name;
		}

		public void run() {
			System.out.println("Task " + name + ": Starting");
			try {
				long duration = (long) (Math.random() * 10);
				System.out
						.printf("Task %s: ReportGenerator: Generating a report during %d seconds\n",
								name, duration);
				TimeUnit.SECONDS.sleep(duration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.printf("Task %s: Ending\n", name);
		}
	}
}
