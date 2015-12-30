/**
 * 
 */
package forkjoin.cancel;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 查找某数值在数组中出现的位置
 * <p>
 * 一旦找到该数出现的一个位置即刻返回，而不用再查找其他位置。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年12月29日
 */
public class SearchNumberTask extends RecursiveTask<Integer> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int NOT_FOUND = -1;
	
	private int[] data;// 数据
	private int start;
	private int end;
	private int number;// 待查找的数值

	private TaskManager taskManager;
	
	public SearchNumberTask(int[] data, int start, int end, int number,
			TaskManager taskManager) {
		this.data = data;
		this.start = start;
		this.end = end;
		this.number = number;
		this.taskManager = taskManager;
	}

	@Override
	protected Integer compute() {
		System.out.println("Task: " + start + "-" + end);

		int index = NOT_FOUND;
		if (end - start > 10) {
			index = launchTasks();
		} else {
			index = lookForNumber();
		}
		return index;
	}

	private int launchTasks() {
		int middle = (start + end) / 2;
		SearchNumberTask task1 = new SearchNumberTask(data, start, middle, number, taskManager);
		taskManager.addTask(task1);
		task1.fork();
		
		SearchNumberTask task2 = new SearchNumberTask(data, middle, end, number, taskManager);
		taskManager.addTask(task2);
		task2.fork();
		
		int index = task1.join();
		if(index != NOT_FOUND) {
			return index;
		}
		
		index = task2.join();
		return index;
	}

	private int lookForNumber() {
		for (int i = start; i < end; i++) {
			if(data[i] == number) {
				System.out.printf("Task: Number %d found in position %d\n", number, i);
				// 找到指定数值则取消其他任务
				taskManager.cancelTasks(this);
				return i;
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return NOT_FOUND;
	}

	/**
	 * 重写cancel方法，在任务被取消时打印信息
	 * 
	 * @see java.util.concurrent.ForkJoinTask#cancel(boolean)
	 */
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		boolean result = super.cancel(mayInterruptIfRunning);
		System.out.printf("Task: Canceled task from %d to %d\n", start, end);
		return result;
	}
}
