/**
 * 
 */
package concurrency.queue;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 阻塞队列演示
 * <p>
 * 功能：搜索指定文件夹（及其子文件夹）下的文件中包含给定关键字的信息。
 * <p>
 * 先通过一个线程枚举指定文件夹下的所有文件，然后启动10个搜索线程同时展开搜索。
 * 这就涉及到枚举线程和搜索线程的等待问题和搜索线程之间的并发问题（不能重复搜索同一个文件）。
 * 实际上枚举线程模拟的是生产者线程，搜索线程模拟的是消费者线程。
 * <p>
 * 在本示例中，程序不需要任何显示的线程同步，而是将阻塞队列数据结构作为一种同步机制。
 *
 * @author 刘晨伟
 *
 * 创建日期：2013-6-12
 */
public class BlockingQueueTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dirPath = "./src";// 搜本Project下的源码文件
		String keyword = "java";
		
		// 容量为10的阻塞队列
		BlockingQueue<File> queue = new ArrayBlockingQueue<File>(10);
		// 先启动文件枚举线程
		new Thread(new FileListTask(dirPath, queue)).start();
		// 启动10个搜索线程同时展开搜索
		for (int i = 0; i < 10; i++) {
			new Thread(new SearchTask(keyword, queue)).start();
		}
	}
}