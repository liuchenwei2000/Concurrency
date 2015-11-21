/**
 * 
 */
package forkjoin.action;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * ForkJoinPool运行RecursiveAction示例
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年12月25日
 */
public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Product> products = ProductFactory.create(10000);
		
		// 创建最初的Task对象
		RecursiveAction task = new UpdatePriceTask(products, 0, products.size(), 0.2);
		
		// 创建默认ForkJoinPool实例，任务并行数等于CPU数
		ForkJoinPool pool = new ForkJoinPool();
		// 执行任务，这是一个异步调用，主线程会继续向下执行。
		pool.execute(task);
		
		// 监控ForkJoinPool的运行时参数
		do {
			System.out.printf("Main: Thread Count: %d\n", pool.getActiveThreadCount());
			System.out.printf("Main: Thread Steal: %d\n", pool.getStealCount());
			System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
			try {
				TimeUnit.MILLISECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!task.isDone());// 判断任务是否已经结束（包括正常结束、异常中止、任务取消）
		
		// 停止pool的运行
		pool.shutdown();
		// 判断任务是否是正常结束（异常中止和任务取消都不属于正常结束）
		if (task.isCompletedNormally()) {
			System.out.printf("Main: The process has completed normally.\n");
		}
		
		// 测试任务所做的操作是否正确
		for (int i = 0; i < products.size(); i++) {
			Product product = products.get(i);
			if (product.getPrice() != 12) {
				System.out.printf("Product %s: %f\n", product.getName(), product.getPrice());
			}
		}
		
		System.out.println("Main: End of the program.\n");
	}
}
