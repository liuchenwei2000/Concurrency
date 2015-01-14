/**
 * 
 */
package thread.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ExecutorService示例
 * <p>
 * ExecutorService接口继承自Executor接口，它的主要作用有两个：
 * <li>1，作为线程池来使用，增加任务执行的效率。
 * <li>2，用来控制一组相关的线程，比如取消所有任务等等。
 * <p>
 * 产生背景：<p>
 * 尽管不必担心 Thread来自何处，但 Executor接口缺乏开发人员可能期望的某种功能:
 * 比如将执行需要访问数据库的 UI操作，如果该操作花费了很长时间，可能希望在它完成之前取消它。
 * 为此JDK创建了一个更加有用的抽象(ExecutorService接口)，它将线程启动工厂建模为一个可集中控制的服务。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-25
 */
public class ExecutorServiceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 一个ExecutorService对象
		ExecutorService executorService = Executors.newCachedThreadPool();
		// 创建10个任务
		List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
		tasks.add(new LongTask());
		for (int i = 0; i < 10; i++) {
			tasks.add(TaskFactory.createCallable());
		}
		
		try {
			long begin = System.currentTimeMillis();
			
			/**
			 * invokeAny方法
			 * <p>
			 * 执行给定的任务，任何(any)一个任务完成了就会立即返回结果。
			 * 因此，无法知道它返回的具体是哪个任务，也许是最快完成的那个。
			 * 在处理一个问题时，如果可以接受任何解决方案就可以使用这个方法。
			 */
			Integer result = executorService.invokeAny(tasks);
			System.out.println("result is " + result);
			long end = System.currentTimeMillis();
			System.out.println("invokeAny【" + ((end - begin) / 1000d) + "s】");
			
			/**
			 * invokeAll方法
			 * <p>
			 * 执行给定的任务，所有(all)任务全部完成了才会返回结果，代表所有任务的解决方案。
			 * 该方法的缺点是，如果第一个任务恰好花去了很多时间，执行器会一直等到它执行完才执行下一个任务。
			 * 返回列表的所有元素的 Future.isDone() 为 true。
			 */
			List<Future<Integer>> results = executorService.invokeAll(tasks);
			for (Future<Integer> future : results) {
				System.out.println("result is " + future.get());
			}
			long end2 = System.currentTimeMillis();
			System.out.println("invokeAll【" + ((end2 - end) / 1000d) + "s】");
			
			/**
			 * 如上例，将结果按可获得的顺序保存起来会更有意义，可通过ExecutorCompletionService来实现排列。
			 * 此类将安排那些完成时提交的任务，把它们放置在可使用 take 方法访问的队列上。
			 * 该服务管理一个Future对象的阻塞队列，保存提交的任务的执行结果。
			 */
			ExecutorCompletionService<Integer> service = new ExecutorCompletionService<Integer>(executorService);
			for (Callable<Integer> task : tasks) {
				service.submit(task);// 提交任务
			}
			for (int i = 0; i < tasks.size(); i++) {
				// take方法：移除下一个已完成的结果，如果没有任何已完成结果可用就阻塞。
				System.out.println(service.take().get());
			}
			long end3 = System.currentTimeMillis();
			System.out.println("submit/take【" + ((end3 - end2) / 1000d) + "s】");
			
			executorService.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class LongTask implements Callable<Integer> {

		@Override
		public Integer call() throws Exception {
			Thread.sleep(5000);
			return 5000;
		}
	}
}