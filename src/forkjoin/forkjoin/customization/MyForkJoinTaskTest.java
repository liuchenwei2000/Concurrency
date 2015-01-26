/**
 * 
 */
package forkjoin.customization;

import java.util.Date;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * 自定义ForkJoinTask示例
 * <p>
 * Customizing tasks running in the Fork/Join framework.
 * <p>
 * 默认情况下，使用 ForkJoinPool 执行的任务都是 ForkJoinTask 对象。
 * 虽然也可以是 Runnable 或 Callable 对象，但它们不能利用 Fork/Join 框架的工作窃取算法。
 * <p>
 * 一般情况下，只需要继承下面两个 ForkJoinTask 的实现类即可：
 * <li>RecursiveAction: 如果任务不需要返回结果。
 * <li>RecursiveTask: 如果任务需要返回结果。
 * <p>
 * 但也可以直接继承ForkJoinTask类实现自定义任务，从而控制整个运算过程。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月26日
 */
public class MyForkJoinTaskTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int array[] = new int[10000];
		ForkJoinPool pool = new ForkJoinPool();
		Task task = new Task("Task", array, 0, array.length);
		// 将任务提交到 ForkJoinPool 执行并等待结束
		pool.invoke(task);
		pool.shutdown();

		System.out.printf("Main: End of the program.\n");
	}

	/**
	 * 自定义 ForkJoinTask 示例
	 * <p>
	 * 本实现类的意义和 RecursiveAction（及 RecursiveTask）是类似的。
	 */
	private abstract static class MyWorkerTask extends ForkJoinTask<Void> {// Void 表示没有返回结果

		private static final long serialVersionUID = 1L;
		
		private String name;// 任务名称
		
		public MyWorkerTask(String name) {
			this.name = name;
		}

		/** 下面三个抽象方法必须实现 */
		
		/**
		 * 返回运算结果
		 */
		@Override
		public Void getRawResult() {
			// 因为本任务没有结果，所以返回null
			return null;
		}

		/**
		 * 设置运算结果
		 */
		@Override
		protected void setRawResult(Void value) {
			// 因为本任务没有结果，所以空实现
		}

		/**
		 * 任务逻辑的具体实现
		 * <p>
		 * 本例将具体实现委托给 compute() 方法，并统计打印该方法运行时间。
		 */
		@Override
		protected boolean exec() {
			Date startDate = new Date();
			compute();
			Date finishDate = new Date();
			long diff = finishDate.getTime() - startDate.getTime();
			System.out.printf("MyWorkerTask: %s : %d Milliseconds to complete.\n", name, diff);
			return true;
		}
		
		/**
		 * 本任务新增的抽象方法，用于实现具体的任务逻辑。
		 */
		protected abstract void compute();

		public String getName() {
			return name;
		}
	}
	
	/**
	 * 一个具体任务实现
	 */
	private static class Task extends MyWorkerTask {
		
		private static final long serialVersionUID = 1L;
		
		private int array[];
		
		private int start;
		private int end;
		
		public Task(String name, int array[], int start, int end) {
			super(name);
			this.array = array;
			this.start = start;
			this.end = end;
		}
		
		/**
		 * 实现父类的 compute() 方法
		 */
		@Override
		protected void compute() {
			if (end - start > 100) {
				int mid = (end + start) / 2;
				Task task1 = new Task(this.getName() + "1", array, start, mid);
				Task task2 = new Task(this.getName() + "2", array, mid, end);
				invokeAll(task1, task2);
			} else {
				for (int i = start; i < end; i++) {
					array[i]++;
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
