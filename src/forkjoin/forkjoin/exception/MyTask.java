/**
 * 
 */
package forkjoin.exception;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 会抛出非受查异常的任务示例
 * <p>
 * 当任务抛出非受查异常时，程序不会结束运行，控制台也不会看到异常信息。
 * 当某个任务T抛出非受查异常时，这会影响它的父任务（将任务T加入到ForkJoinPool的任务）及父任务的父任务，以此类推。
 * 这些任务都不能正常结束。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年12月29日
 */
public class MyTask extends RecursiveTask<Integer> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int[] data;
	
	private int start;
	private int end;
	
	public MyTask(int[] data, int start, int end) {
		this.data = data;
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		System.out.printf("Task: Start from %d to %d\n", start, end);
		if (end - start > 10) {
			int middle = (end + start) / 2;
			MyTask task1 = new MyTask(data, start, middle);
			MyTask task2 = new MyTask(data, middle, end);
			invokeAll(task1, task2);
		} else {
			// 假设第四个任务出现异常
			if (start < 3 && end > 3) {
//				throw new RuntimeException("This task throws an"
//						+ "Exception: Task from " + start + " to " + end);
				// 或者使用下面的方式
				Exception ex = new RuntimeException("This task throws an" + "Exception: Task from " + start + " to " + end);
				completeExceptionally(ex);
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("Task: End form %d to %d\n", start, end);
		return 0;
	}
}
