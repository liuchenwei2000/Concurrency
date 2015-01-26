/**
 * 
 */
package forkjoin.customization;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * Fork/Join框架使用的自定义线程工厂示例
 * <p>
 * Implementing the ThreadFactory interface to generate custom threads for the Fork/Join framework.
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月26日
 */
public class MyWorkerThreadFactoryTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyWorkerThreadFactory factory=new MyWorkerThreadFactory();
		
		ForkJoinPool pool=new ForkJoinPool(4, factory, null, false);
		
		MyRecursiveTask task= new MyRecursiveTask(0, 100000);
		pool.execute(task);
		
		task.join();
		
		pool.shutdown();
		
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
			System.out.printf("Main: Result: %d\n", task.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.printf("Main: End of the program\n");
	}
	
	/**
	 * 自定义WorkerThreadFactory
	 * <p>
	 * ForkJoinPool 像其他Java并发API一样，使用工厂来创建线程对象。
	 * 线程工厂需要实现ForkJoinPool.ForkJoinWorkerThreadFactory 类。
	 */
	private static class MyWorkerThreadFactory implements ForkJoinWorkerThreadFactory {

		@Override
		public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
			return new MyWorkerThread(pool);
		}
	}
	
	/**
	 * 自定义WorkerThread示例
	 * <p>
	 * Fork/Join 框架使用的线程称为工作线程（worker thread），Java使用继承自 Thread 类的 ForkJoinWorkerThread 来实现工作线程。
	 * <p>
	 * 统计工作线程运行了多少任务。
	 */
	private static class MyWorkerThread extends ForkJoinWorkerThread {
		
		// 使用ThreadLocal变量是为了让每个线程独享一个计数器。
		private static ThreadLocal<Integer> counter = new ThreadLocal<Integer>();

		protected MyWorkerThread(ForkJoinPool pool) {
			super(pool);
		}

		/**
		 * 当工作线程开始执行时被调用
		 * 
		 * @see java.util.concurrent.ForkJoinWorkerThread#onStart()
		 */
		@Override
		protected void onStart() {
			super.onStart();
			counter.set(0);// 初始化计数器
			System.out.printf(
					"MyWorkerThread %d: Initializing task counter.\n", getId());
		}

		/**
		 * 当工作线程结束运行时被调用
		 * <p>
		 * 包括正常结束和异常结束，方法参数即是异常结束时的Exception对象，如果其值为null则表明是正常结束。
		 * 
		 * @see java.util.concurrent.ForkJoinWorkerThread#onTermination(java.lang.Throwable)
		 */
		@Override
		protected void onTermination(Throwable exception) {
			// 打印该线程执行的任务数
			System.out
					.printf("MyWorkerThread %d: %d\n", getId(), counter.get());
			super.onTermination(exception);
		}

		/**
		 * 当任务加入时递增计数器
		 */
		public void addTask() {
			counter.set(counter.get() + 1);
		}
	}
	
	/**
	 * 在 Fork/Join 框架中运行的任务
	 */
	private static class MyRecursiveTask extends RecursiveTask<Integer> {
		
		private static final long serialVersionUID = 1L;

		/** 阈值 */
		private static final int DEFAULT_SIZE = 100;
		
		private int first;// 第一个数的索引值 
		private int last;// 最后一个数的索引值 
		
		public MyRecursiveTask(int first, int last) {
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
			// 每运行一次则将线程执行任务数 +1
			((MyWorkerThread)Thread.currentThread()).addTask();
			
			Integer result = 0;
			if(last - first > DEFAULT_SIZE) {
				int middle = (last + first) / 2;
				
				MyRecursiveTask task1 = new MyRecursiveTask(first, middle + 1);
				MyRecursiveTask task2 = new MyRecursiveTask(middle + 1, last); 
				
				invokeAll(task1, task2);
				
				try {
					Integer result1 = task1.get();
					Integer result2 = task2.get();
					result = groupResults(result1, result2);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			} else { 
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
}
