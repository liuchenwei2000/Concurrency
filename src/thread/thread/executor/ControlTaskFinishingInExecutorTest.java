/**
 * 
 */
package thread.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * 控制 Executor 中执行完毕的任务示例
 * <p>
 * FutureTask 类提供的 done()方法可以用来在 Executor 中运行的任务结束后再执行某些特定代码。
 * 常被用来执行一些后处理操作，比如生成统计报告、使用Email发送结果或释放某些资源。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月15日
 */
public class ControlTaskFinishingInExecutorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool();
		
		ResultTask[] resultTasks = new ResultTask[5];

		for (int i = 0; i < 5; i++) {
			ExecutableTask executableTask = new ExecutableTask("Task " + i);
			// 使用 ResultTask 控制 ExecutableTask
			resultTasks[i] = new ResultTask(executableTask);
			executor.submit(resultTasks[i]);
		}
		
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// 强行取消任务
		for (int i = 0; i < resultTasks.length; i++) {
			resultTasks[i].cancel(true);
		}
		// 将正常完成的任务结果打印出来
		for (int i = 0; i < resultTasks.length; i++) {
			try {
				if (!resultTasks[i].isCancelled()) {
					System.out.printf("%s\n", resultTasks[i].get());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		executor.shutdown();
	}

	/**
	 * 普通的 Callable 任务
	 */
	private static class ExecutableTask implements Callable<String> {
		
		private String name;
		
		public ExecutableTask(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}

		@Override
		public String call() throws Exception {
			try {
				long duration = (long) (Math.random() * 10);
				System.out.printf("%s: Waiting %d seconds for results.\n", this.name, duration);
				TimeUnit.SECONDS.sleep(duration);
			} catch (InterruptedException e) {
			}
			return "Hello, world. I'm " + name;
		}
	}
	
	/**
	 * FutureTask 实现示例，用来控制 ExecutableTask 结束时行为。
	 * <p>
	 * 当 FutureTask 控制的任务运行结束后，done()方法会被 FutureTask 对象在内部自动调用，准确地说，
	 * 它会在任务状态切换到 isDone 之后被调用，而不管任务是正常结束还是被取消。
	 */
	private static class ResultTask extends FutureTask<String> {

		private String name;

		public ResultTask(Callable<String> callable) {
			super(callable);
			this.name = ((ExecutableTask) callable).getName();
		}

		/**
		 * done() 方法的默认实现是空的，可以通过覆盖该方法来实现特定行为。
		 */
		@Override
		protected void done() {
			if (isCancelled()) {
				System.out.printf("%s: Has been canceled\n", name);
			} else {
				System.out.printf("%s: Has finished\n", name);
			}
		}
	}
}
