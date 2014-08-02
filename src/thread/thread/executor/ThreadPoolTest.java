/**
 * 
 */
package thread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 线程池示例
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
public class ThreadPoolTest {

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
		 * 对于固定数量线程池，如果提交的任务书大于空闲线程数，那么得不到服务的任务将会等待。
		 * 当其他任务完成后，有了空闲线程，它们就能运行了。
		 */
		ExecutorService pool = Executors.newFixedThreadPool(10);
		
		// 可以使用下面三种方法将一个Runnable或者Callable对象提交给线程池：
		/**
		 * 提交一个Runnable对象
		 * <p>
		 * 返回一个Future对象，用于查看任务执行状态，但get方法在完成时会返回null。
		 */
		Future<?> future1 = pool.submit(TaskFactory.createRunnable());
		System.out.println("future1=" + future1.get());
		
		/**
		 * 提交一个Runnable对象和指定结果对象
		 * <p>
		 * 返回一个Future对象，用于查看任务执行状态，get方法在完成时会返回参数传入的对象。
		 */
		Future<Object> future2 = pool.submit(TaskFactory.createRunnable(), new Object());
		System.out.println("future2=" + future2.get());
		
		/**
		 * 提交一个Callable对象
		 * <p>
		 * 返回一个Future对象，用于查看任务执行状态，get方法在完成时会返回计算结果。
		 */
		Future<Integer> future3 = pool.submit(TaskFactory.createCallable());
		System.out.println("future3=" + future3.get());
		
		/**
		 * 当用完一个连接池后，要调用下面的方法关闭连接池。
		 * 被关闭的执行器不再接受新的任务，当所有任务都完成后，池中的线程就死亡了。
		 */
		pool.shutdown();
		
		/**
		 * 也可以调用下面的方法，池会取消所有还没开始的任务并试图中断正在运行的线程。
		 */
		pool.shutdownNow();
	}
}