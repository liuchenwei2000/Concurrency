/**
 * 
 */
package forkjoin.task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

/**
 * �������
 * <p>
 * ��һ�����䷶Χ�������������ĺ͡�
 * <p>
 * ʵ�� RecursiveTask ���Ƽ���ʽ��
 * <pre>
 * If (problem size > size){
 * 	tasks=divide(task);// �ֽ�����
 * 	execute(tasks);// ����������
 * 	groupResults()// �ϲ����
 * 	return result;
 * } else {
 * 	resolve problem;// �������
 * 	return result;
 * }
 * </pre>
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2014��12��25��
 */
public class SumTask extends RecursiveTask<Integer> {// ��Ҫ���ؽ�������Լ̳�RecursiveTask
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** ��ֵ */
	private static final int DEFAULT_SIZE = 100;
	
	private int first;// ��һ����������ֵ 
	private int last;// ���һ����������ֵ 
	
	public SumTask(int first, int last) {
		this.first = first;
		this.last = last;
	}

	/**
	 * ��д�÷�����ʵ���ض�ҵ���߼�
	 * 
	 * @see java.util.concurrent.RecursiveTask#compute()
	 */
	@Override
	protected Integer compute() {
		Integer result = 0;
		// ������ֵ������������ֽ�ɸ�С�����������߶��������
		if(last - first > DEFAULT_SIZE) {
			int middle = (last + first) / 2;
			
			SumTask task1 = new SumTask(first, middle + 1);
			SumTask task2 = new SumTask(middle + 1, last); 
			
			/*
			 * �ֽ������ͨ������ForkJoinTask.invokeAll����ִ������������
			 * 
			 * �÷�����һ��ͬ�����ã��������һֱ�ȵ�������ȫ����ɲŻ��������ִ�С�
			 * ��������ȴ����������ʱ�����и������worker thread��Ѱ�������ȴ����е�������������
			 * ��ˣ�Fork/Join����ṩ�˱�Runnable��Callable����Ч����������ܡ�
			 */ 
			invokeAll(task1, task2);
			
			try {
				// ForkJoinTask ʵ����Future�ӿڣ�ͨ��get()��������ֵ��
				Integer result1 = task1.get();
				Integer result2 = task2.get();
				// Ҳ����ʹ������ķ�ʽ��������ֵ�������ʱ�򷵻�null
				// task1.get(100, TimeUnit.MILLISECONDS);
				
				// �ϲ�����������Ľ�����γɱ�����Ľ��
				result = groupResults(result1, result2);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		} else {// С����ֵ��ֱ��ִ������
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
