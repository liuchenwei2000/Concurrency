/**
 * 
 */
package concurrency.customization;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 定制 ThreadPoolExecutor 示例
 * <p>
 * 本例将继承 ThreadPoolExecutor 实现具有计时和统计功能的执行器：
 * 它能够计算每个任务的运行时间，并能在执行器关闭时执行统计工作。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月22日
 */
public class CustomizingThreadPoolExecutor {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyThreadPoolExecutor myExecutor = new MyThreadPoolExecutor(2, 4, 1000,
				TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
		
		for (int i = 0; i < 10; i++) {
			SleepTwoSecondsTask task = new SleepTwoSecondsTask();
			myExecutor.execute(task);
		}
		
		try {
			TimeUnit.SECONDS.sleep(6);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		myExecutor.shutdown();
		
		try {
			myExecutor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("Main: End of the program.\n");
	}
}

/**
 * 定制 ThreadPoolExecutor 类
 */
class MyThreadPoolExecutor extends ThreadPoolExecutor {

	// 存放任务运行的起始时间
	private ConcurrentHashMap<String, Date> startTimes;
	
	public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		this.startTimes = new ConcurrentHashMap<>();
	}
	
	/**
	 * 重写任务将要执行前动作
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread, java.lang.Runnable)
	 */
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		System.out.printf("MyThreadPoolExecutor: A task is beginning: %s : %s\n",
				t.getName(), r.hashCode());
		// 记录任务执行的起始时间
		startTimes.put(String.valueOf(r.hashCode()), new Date());
	}

	/**
	 * 重写任务执行完成后动作
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
	 */
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		// 计算任务运行时间
		Date startDate = startTimes.remove(String.valueOf(r.hashCode()));
		Date finishDate = new Date();
		long diff = finishDate.getTime() - startDate.getTime();
		
		System.out.printf("MyThreadPoolExecutor: A task is finishing. Duration: %d ms\n", diff);
	}

	/**
	 * 重写shutdown()方法，加入打印统计信息的功能
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#shutdown()
	 */
	@Override
	public void shutdown() {
		System.out.printf("MyThreadPoolExecutor: Going to shutdown.\n");
		statistics();
		super.shutdown();
	}

	/**
	 * 重写shutdownNow()方法，加入打印统计信息的功能
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#shutdownNow()
	 */
	@Override
	public List<Runnable> shutdownNow() {
		System.out.printf("MyThreadPoolExecutor: Going to immediately shutdown.\n");
		statistics();
		return super.shutdownNow();
	}

	private void statistics() {
		// 已执行结束的任务数
		System.out.printf("MyThreadPoolExecutor: Executed tasks: %d\n", getCompletedTaskCount());
		// 尚在运行中的任务数
		System.out.printf("MyThreadPoolExecutor: Running tasks: %d\n", getActiveCount());
		// 已提交但尚未执行的任务数
		System.out.printf("MyThreadPoolExecutor: Pending tasks: %d\n", getQueue().size());
	}
}

class SleepTwoSecondsTask implements Runnable {
	
	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
