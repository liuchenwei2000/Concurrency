/**
 * 
 */
package forkjoin.task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

/**
 * 求和任务
 * <p>
 * 求一个区间范围内所有正整数的和。
 * <p>
 * 实现 RecursiveTask 的推荐形式：
 * <pre>
 * If (problem size > size){
 * 	tasks=divide(task);// 分解任务
 * 	execute(tasks);// 运行子任务
 * 	groupResults()// 合并结果
 * 	return result;
 * } else {
 * 	resolve problem;// 解决问题
 * 	return result;
 * }
 * </pre>
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年12月25日
 */
public class SumTask extends RecursiveTask<Integer> {// 需要返回结果，所以继承RecursiveTask
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 阈值 */
	private static final int DEFAULT_SIZE = 100;
	
	private int first;// 第一个数的索引值 
	private int last;// 最后一个数的索引值 
	
	public SumTask(int first, int last) {
		this.first = first;
		this.last = last;
	}

	/**
	 * 重写该方法，实现特定业务逻辑
	 * 
	 * @see java.util.concurrent.RecursiveTask#compute()
	 */
	@Override
	protected Integer compute() {
		Integer result = 0;
		// 大于阈值，则将任务继续分解成更小的两个（或者多个）任务。
		if(last - first > DEFAULT_SIZE) {
			int middle = (last + first) / 2;
			
			SumTask task1 = new SumTask(first, middle + 1);
			SumTask task2 = new SumTask(middle + 1, last); 
			
			/*
			 * 分解任务后，通过调用ForkJoinTask.invokeAll方法执行两个子任务。
			 * 
			 * 该方法是一个同步调用，父任务会一直等到子任务全部完成才会继续向下执行。
			 * 当父任务等待子任务完成时，运行父任务的worker thread会寻找其他等待运行的任务并运行它。
			 * 因此，Fork/Join框架提供了比Runnable和Callable更高效的任务管理功能。
			 */ 
			invokeAll(task1, task2);
			
			try {
				// ForkJoinTask 实现了Future接口，通过get()返回运算值。
				Integer result1 = task1.get();
				Integer result2 = task2.get();
				// 也可以使用下面的方式返回运算值，如果超时则返回null
				// task1.get(100, TimeUnit.MILLISECONDS);
				
				// 合并各个子任务的结果，形成本任务的结果
				result = groupResults(result1, result2);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		} else {// 小于阈值，直接执行任务
			result = sum();
		}
		return result;
	}

	private Integer groupResults(Integer integer1, Integer integer2) {
		return integer1 + integer2;
	}

	private Integer sum() {
		int sum = 0;
		for (int i = first; i < last; i++) {
			sum += i;
		}
		return sum;
	}
}
