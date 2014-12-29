/**
 * 
 */
package forkjoin.cancel;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * ����ĳ��ֵ�������г��ֵ�λ��
 * <p>
 * һ���ҵ��������ֵ�һ��λ�ü��̷��أ��������ٲ�������λ�á�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2014��12��29��
 */
public class SearchNumberTask extends RecursiveTask<Integer> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int NOT_FOUND = -1;
	
	private int[] data;// ����
	private int start;
	private int end;
	private int number;// �����ҵ���ֵ

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
				// �ҵ�ָ����ֵ��ȡ����������
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
	 * ��дcancel������������ȡ��ʱ��ӡ��Ϣ
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
