/**
 * 
 */
package forkjoin.exception;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * ���׳����ܲ��쳣������ʾ��
 * <p>
 * �������׳����ܲ��쳣ʱ�����򲻻�������У�����̨Ҳ���ῴ���쳣��Ϣ��
 * ��ĳ������T�׳����ܲ��쳣ʱ�����Ӱ�����ĸ����񣨽�����T���뵽ForkJoinPool�����񣩼�������ĸ������Դ����ơ�
 * ��Щ���񶼲�������������
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2014��12��29��
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
			// ������ĸ���������쳣
			if (start < 3 && end > 3) {
//				throw new RuntimeException("This task throws an"
//						+ "Exception: Task from " + start + " to " + end);
				// ����ʹ������ķ�ʽ
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
