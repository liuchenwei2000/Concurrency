/**
 * 
 */
package thread.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ScheduledExecutorService示例
 * <p>
 * ScheduledExecutorService接口具有为延时任务或周期性任务而设计的方法。
 * 它是一种允许线程机制的java.util.Timer的泛化。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-17
 */
public class ScheduledExecutorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		/** 延时任务 */
		// 为预定执行而构建的单线程池
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.shutdownNow();
		
		// 为预定执行而构建的固定线程池
		service = Executors.newScheduledThreadPool(3);
		
		// 创建并执行在给定延迟后启用的一次性操作Callable(本例是1000毫秒)
		ScheduledFuture<Integer> result = service.schedule(TaskFactory.createCallable(), 1000, TimeUnit.MILLISECONDS);
		System.out.println(result.get());
		
		// 创建并执行在给定延迟后启用的一次性操作Runnable(本例是1000毫秒)
		service.schedule(TaskFactory.createRunnable(), 1000, TimeUnit.MILLISECONDS);
		
		service.shutdown();
		/*
		 * 当调用了shutdown()方法后，若 Executor 中仍然有任务在等待延时结束后运行，默认的行为是：
		 * 任务仍会得到执行而不管 Executor 是否已结束。
		 * 
		 * 可通过调用 setExecuteExistingDelayedTasksAfterShutdownPolicy(boolean) 改变默认的处理方式：
		 * false：Executor shutdown后不再继续执行剩下的延时任务。
		 */
		((ScheduledThreadPoolExecutor) service).setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
		
		/** 周期任务 */
		service = Executors.newScheduledThreadPool(2);
		/*
		 * 创建并执行一个在给定初始延迟后首次启用的周期性操作，后续操作具有给定的周期。
		 * 也就是将在initialDelay(本例1000ms)后开始执行，然后在initialDelay+period(本例是2000ms)后执行，
		 * 接着在initialDelay + 2*period 后执行，依此类推。
		 * 
		 * 注意：如果某个任务执行时间（如5秒）超过了延时时间（如2秒），则可能会出现两个（或多个）任务同时在运行的情况。
		 */
		service.scheduleAtFixedRate(TaskFactory.createRunnable(), 1000, 2000, TimeUnit.MILLISECONDS);
		
		/*
		 * 创建并执行一个在给定初始延迟(本例1000ms)后首次启用的定期操作，
		 * 随后，在每一次执行终止和下一次执行开始之间都存在给定的延迟(本例2000ms)。
		 */
		service.scheduleWithFixedDelay(TaskFactory.createRunnable(), 1000, 2000, TimeUnit.MILLISECONDS);
		
		service.shutdown();
		
		service.awaitTermination(1, TimeUnit.DAYS);
	}
}
