/**
 * 
 */
package concurrency.queue;

import java.io.File;
import java.util.concurrent.BlockingQueue;

/**
 * 文件枚举线程
 *
 * @author 刘晨伟
 *
 * 创建日期：2013-6-12
 */
public class FileListTask implements Runnable {

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