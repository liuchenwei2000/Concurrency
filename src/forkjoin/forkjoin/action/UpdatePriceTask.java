/**
 * 
 */
package forkjoin.action;

import java.util.List;
import java.util.concurrent.RecursiveAction;

/**
 * 更新价格任务
 * <p>
 * 批量更新大数据量Product的价格。
 * <p>
 * 实现 RecursiveAction 的推荐形式：
 * <pre>
 * If (problem size > default size){
 * 	tasks=divide(task);// 分解任务
 * 	execute(tasks);// 运行子任务
 * } else {
 * 	resolve problem using another algorithm;// 真正解决问题
 * }
 * </pre>
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年12月25日
 */
public class UpdatePriceTask extends RecursiveAction {// 不需要返回结果，所以继承RecursiveAction
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 阈值 */
	private static final int DEFAULT_SIZE = 10;
	
	// 待处理的所有Product
	private List<Product> products;
	
	private int first;// 起始Product的索引值 
	private int last;// 末尾Product的索引值 
	
	private double increment;// 价格增幅
	
	public UpdatePriceTask(List<Product> products, int first, int last,
			double increment) {
		this.products = products;
		this.first = first;
		this.last = last;
		this.increment = increment;
	}

	/**
	 * 重写该方法，实现特定业务逻辑
	 * 
	 * @see java.util.concurrent.RecursiveAction#compute()
	 */
	@Override
	protected void compute() {
		// 大于阈值，则将任务继续分解成更小的两个（或者多个）任务。
		if(last - first > DEFAULT_SIZE) {
			int middle = (last + first)/2;
			System.out.printf("Task:Pending tasks %s%n", getQueuedTaskCount());
			UpdatePriceTask task1 = new UpdatePriceTask(products, first, middle + 1, increment);
			UpdatePriceTask task2 = new UpdatePriceTask(products, middle + 1, last, increment); 
			/*
			 * 分解任务后，通过调用ForkJoinTask.invokeAll方法执行两个子任务。
			 * 
			 * 该方法是一个同步调用，父任务会一直等到子任务全部完成才会继续向下执行。
			 * 当父任务等待子任务完成时，运行父任务的worker thread会寻找其他等待运行的任务并运行它。
			 * 因此，Fork/Join框架提供了比Runnable和Callable更高效的任务管理功能。
			 */ 
			invokeAll(task1, task2);
		} else {// 小于阈值，直接执行任务
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
