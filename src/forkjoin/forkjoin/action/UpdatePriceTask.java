/**
 * 
 */
package forkjoin.action;

import java.util.List;
import java.util.concurrent.RecursiveAction;

/**
 * ���¼۸�����
 * <p>
 * �������´�������Product�ļ۸�
 * <p>
 * ʵ�� RecursiveAction ���Ƽ���ʽ��
 * <pre>
 * If (problem size > default size){
 * 	tasks=divide(task);// �ֽ�����
 * 	execute(tasks);// ����������
 * } else {
 * 	resolve problem using another algorithm;// �����������
 * }
 * </pre>
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2014��12��25��
 */
public class UpdatePriceTask extends RecursiveAction {// ����Ҫ���ؽ�������Լ̳�RecursiveAction
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** ��ֵ */
	private static final int DEFAULT_SIZE = 10;
	
	// �����������Product
	private List<Product> products;
	
	private int first;// ��ʼProduct������ֵ 
	private int last;// ĩβProduct������ֵ 
	
	private double increment;// �۸�����
	
	public UpdatePriceTask(List<Product> products, int first, int last,
			double increment) {
		this.products = products;
		this.first = first;
		this.last = last;
		this.increment = increment;
	}

	/**
	 * ��д�÷�����ʵ���ض�ҵ���߼�
	 * 
	 * @see java.util.concurrent.RecursiveAction#compute()
	 */
	@Override
	protected void compute() {
		// ������ֵ������������ֽ�ɸ�С�����������߶��������
		if(last - first > DEFAULT_SIZE) {
			int middle = (last + first)/2;
			System.out.printf("Task:Pending tasks %s%n", getQueuedTaskCount());
			UpdatePriceTask task1 = new UpdatePriceTask(products, first, middle + 1, increment);
			UpdatePriceTask task2 = new UpdatePriceTask(products, middle + 1, last, increment); 
			/*
			 * �ֽ������ͨ������ForkJoinTask.invokeAll����ִ������������
			 * 
			 * �÷�����һ��ͬ�����ã��������һֱ�ȵ�������ȫ����ɲŻ��������ִ�С�
			 * ��������ȴ����������ʱ�����и������worker thread��Ѱ�������ȴ����е�������������
			 * ��ˣ�Fork/Join����ṩ�˱�Runnable��Callable����Ч����������ܡ�
			 */ 
			invokeAll(task1, task2);
		} else {// С����ֵ��ֱ��ִ������
			updatePrices();
		}
	}

	private void updatePrices() {
		for (int i = first; i < last; i++) {
			Product product = products.get(i);
			product.setPrice(product.getPrice() * (1 + increment));
		}
	}
}
