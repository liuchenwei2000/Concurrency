/**
 * 
 */
package forkjoin.asyn;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * 在指定目录中查找具有指定扩展名的文件示例
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年12月29日
 */
public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileProcessor noteTask = new FileProcessor("E:/Programs/Notes","java");
		FileProcessor techTask = new FileProcessor("E:/Programs/Tech","java");
		FileProcessor toysTask = new FileProcessor("E:/Programs/Toys","java");
		FileProcessor webTask = new FileProcessor("E:/Programs/Web","java");
		
		ForkJoinPool pool = new ForkJoinPool();
		// 异步执行四个任务
		pool.execute(noteTask);
		pool.execute(techTask);
		pool.execute(toysTask);
		pool.execute(webTask);
		
		do {
			System.out.printf("******************************************\n");
			System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
			System.out.printf("Main: Active Threads: %d\n", pool.getActiveThreadCount());
			System.out.printf("Main: Task Count: %d\n", pool.getQueuedTaskCount());
			System.out.printf("Main: Steal Count: %d\n", pool.getStealCount());
			System.out.printf("******************************************\n");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while ((!noteTask.isDone()) || (!techTask.isDone())
				|| (!toysTask.isDone()) || (!webTask.isDone()));
		
		pool.shutdown();
		
		// 获取每个任务的结果
		List<String> results = noteTask.join();
		System.out.printf("Note : %d files found.\n", results.size());
		results = techTask.join();
		System.out.printf("Tech : %d files found.\n", results.size());
		results = toysTask.join();
		System.out.printf("Toys : %d files found.\n", results.size());
		results = webTask.join();
		System.out.printf("Web : %d files found.\n", results.size());
	}
}
