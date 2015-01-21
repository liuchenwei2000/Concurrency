/**
 * 
 */
package concurrency.collections;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
public class ArrayBlockingQueueTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dirPath = "./src";// 搜本Project下的源码文件
		String keyword = "Test";
		
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

/**
 * 文件枚举任务
 */
class FileListTask implements Runnable {

	// 结束文件标志
	public static final File END = new File("");
	
	private String dirPath;// 起始目录
	private BlockingQueue<File> queue;
	
	public FileListTask(String dirPath, BlockingQueue<File> queue) {
		this.dirPath = dirPath;
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			listFile(new File(dirPath));
			queue.put(END);// 最后将这个特定的结束标志加入阻塞队列
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void listFile(File dir) throws InterruptedException {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				listFile(file);
			} else {
				// 不断地将中间结果加入阻塞队列
				queue.put(file);
			}
		}
	}
}

/**
 * 搜索任务
 */
class SearchTask implements Runnable {

	private String keyword;
	private BlockingQueue<File> queue;
	
	public SearchTask(String keyword, BlockingQueue<File> queue) {
		this.keyword = keyword;
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			boolean complete = false;// 是否所有文件都搜索完毕
			// 不断的从阻塞队列中拿出文件进行搜索，直到全部文件都搜索完。
			while (!complete) {
				File file = queue.take();
				if (FileListTask.END == file) {// 通过特定标识判断是否全部搜索完成
					queue.put(file);// 这里要把标识再放进队列，以便其他线程知晓
					complete = true;
				} else {
					search(file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void search(File file) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String temp = null;
		int line = 0;
		while((temp = br.readLine())!=null){
			line++;
			if(temp.contains(keyword)){
				System.out.print("file path=" + file.getPath());
				System.out.print("  line number=" + line);
				System.out.println("  content=" + temp);
			}
		};
		br.close();
	}
}