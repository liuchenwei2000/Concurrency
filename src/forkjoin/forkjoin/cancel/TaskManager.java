/**
 * 
 */
package forkjoin.cancel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;

/**
 * 任务管理器
 * <p>
 * Fork/Join框架取消任务的两点限制：
 * 1，ForkJoinPool不提供取消所有池中任务（不论是运行态还是等待态）的方法。
 * 2，取消一个任务时，并不会取消该任务创建的子任务。
 * <p>
 * 基于以上两点限制，为了管理所有任务，才有了本类。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年12月29日
 */
public class TaskManager {

	private List<ForkJoinTask<Integer>> tasks;

	public TaskManager() {
		this.tasks = new ArrayList<>();
	}

	public void addTask(ForkJoinTask<Integer> task) {
		tasks.add(task);
	}

	public void removeTask(ForkJoinTask<Integer> task) {
		tasks.remove(task);
	}

	/**
	 * 取消除了参数任务外的其它所有任务。
	 */
	public void cancelTasks(ForkJoinTask<Integer> task) {
		for (ForkJoinTask<Integer> t : tasks) {
			if (t != task) {
				/* 
				 * ForkJoinTask只允许取消尚未运行的任务，对于已经开始运行的任务调用cancel()方法没有任何效果。
				 * 如果任务成功被取消则返回true，如果任务已经开始执行或者执行结束，则返回false。
				 * 参数值 boolean mayInterruptIfRunning 在目前ForkJoinTask的实现中没有什么作用。
				 */
				t.cancel(true);
			}
		}
	}
}
