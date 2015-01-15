/**
 * 
 */
package thread.executor;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolExecutor 示例
 * <p>
 * 创建一个新线程的代价还是很高的，因为涉及与操作系统的交互。
 * 如果程序创建大量生存期很短的线程，那就应该使用线程池（Thread Pool）。
 * <p>
 * 一个线程池包含大量准备运行的空闲线程，将一个Runnable对象提交给线程池，线程池中的一个线程就会调用run方法，
 * 当run方法退出时，线程不会死亡，而是继续在池中准备为下一个请求提供服务。
 * <p>
 * 另外使用线程池可以减少并发线程的数量，创建大量的线程会降低性能甚至使虚拟机崩溃。
 * 如果程序会创建许多线程，那就应该使用一个线程数固定的线程池来限制并发线程的数量。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-17
 */
public class ThreadPoolExecutorTest {

	/**
	 * 使用线程池的一般步骤：
	 * 1，使用Excutors类创建相应的线程池。
	 * 2，调用submit提交一个Runnable或Callable对象。
	 * 3，如果希望取消任务或者提交了一个Callable对象，那就保存好submit返回的Future对象。
	 * 4，当不想在提交任何任务时调用shutdown。
	 */
	public static void main(String[] args) throws Exception {
		/**
		 * 使用固定数量线程池做例子
		 * <p>
		 * 对于固定数量线程池，如果提交的任务数大于空闲线程数，那么得不到服务的任务将会等待。
		 * 当其他任务完成后，有了空闲线程，它们就能运行了。
		 */
		ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
		
		/**
		 * 提交一个Runnable对象
		 * <p>
		 * 返回一个Future对象，用于查看任务执行状态，但get方法在完成时会返回null。
		 */
		pool.execute(TaskFactory.createRunnable());
		
		// 通过下面的方法返回 线程池的信息
		System.out.printf("Pool Size：%d\n", pool.getPoolSize());// 池内真正的线程数
		System.out.printf("Active Count：%d\n", pool.getActiveCount());// 池内正在运行任务的线程数
		System.out.printf("Completed Task Count：%d\n", pool.getCompletedTaskCount());// 已完成的任务数
		
		/**
		 * 当用完一个连接池后，要调用下面的方法关闭连接池。
		 * 被关闭的执行器不再接受新的任务，当所有任务都完成后，池中的线程就死亡了。
		 */
		pool.shutdown();
		
		/**
		 * 也可以调用下面的方法，池会取消所有还没开始的任务并试图中断正在运行的线程。
		 * 返回值是尚未运行的任务列表。
		 */
		List<Runnable> unRunTask = pool.shutdownNow();
		System.out.println(unRunTask.size());
		
		// 若已调用 shutdown()或 shutdownNow()则返回 true
		System.out.println(pool.isTerminated());
		// 若已调用 shutdown()则返回 true
		System.out.println(pool.isShutdown());
		
		// 阻塞调用线程直到超时或池内所有任务都已完成
		pool.awaitTermination(1, TimeUnit.DAYS);
	}
}