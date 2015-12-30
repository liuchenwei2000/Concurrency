/**
 * 
 */
package thread.executor;

import java.util.concurrent.Executor;

/**
 * Executor执行器示例
 * <p>
 * Executor是执行已提交 Runnable任务的对象。
 * 此接口提供一种将任务提交与每个任务将如何运行的机制(包括线程使用的细节、调度等)分离开来的方法。
 * <p>
 * 通常使用 Executor而不是显式地创建线程，Executors类是创建执行器的工厂类，详见ThreadPoolTest。
 * <p>
 * 产生背景：<p>
 * 直接创建 Thread对象可以解决一些问题。
 * 在一些JVM中，创建 Thread是一项重量型的操作，重用现有 Thread比创建新线程要容易得多。
 * 而在另一些 JVM中，情况正好相反：Thread是轻量型的，可以在需要时很容易地新建一个线程。
 * JDK引入了Executor接口，这是对创建新线程的一种抽象，开发人员无需直接创建 Thread。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-17
 */
public class ExecutorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 如何使用执行器运行Runnable
		Executor executor = new DirectExecutor();
		executor.execute(new Task1());// 可以运行多个Runnable
		executor.execute(new Task2());
		
		executor = new NewThreadExecutor();
		executor.execute(new Task1());
		executor.execute(new Task2());
	}
	
	/**
	 * 直接执行器
	 * <p>
	 * Executor 接口并没有严格地要求执行是异步的，执行器完全可以在调用者的线程中运行已提交的任务。 
	 */
	private static class DirectExecutor implements Executor {
		
	     public void execute(Runnable r) {
	         r.run();
	     }
	 }
	
	/**
	 * 新线程执行器
	 * <p>
	 * 创建一个新的线程去执行每个任务。
	 */
	private static class NewThreadExecutor implements Executor {
		
	     public void execute(Runnable r) {
	        new Thread(r).start();
	     }
	 }
	
	private static class Task1 implements Runnable {

		public void run() {
			try {
				Thread.sleep(1000);
				System.out.println("task1 complete.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class Task2 implements Runnable {

		public void run() {
			try {
				Thread.sleep(1000);
				System.out.println("task2 complete.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
