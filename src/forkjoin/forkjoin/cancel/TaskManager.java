/**
 * 
 */
package forkjoin.cancel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;

/**
 * ���������
 * <p>
 * Fork/Join���ȡ��������������ƣ�
 * 1��ForkJoinPool���ṩȡ�����г������񣨲���������̬���ǵȴ�̬���ķ�����
 * 2��ȡ��һ������ʱ��������ȡ�������񴴽���������
 * <p>
 * ���������������ƣ�Ϊ�˹����������񣬲����˱��ࡣ
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2014��12��29��
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
	 * ȡ�����˲����������������������
	 */
	public void cancelTasks(ForkJoinTask<Integer> task) {
		for (ForkJoinTask<Integer> t : tasks) {
			if (t != task) {
				/* 
				 * ForkJoinTaskֻ����ȡ����δ���е����񣬶����Ѿ���ʼ���е��������cancel()����û���κ�Ч����
				 * �������ɹ���ȡ���򷵻�true����������Ѿ���ʼִ�л���ִ�н������򷵻�false��
				 * ����ֵ boolean mayInterruptIfRunning ��ĿǰForkJoinTask��ʵ����û��ʲô���á�
				 */
				t.cancel(true);
			}
		}
	}
}
