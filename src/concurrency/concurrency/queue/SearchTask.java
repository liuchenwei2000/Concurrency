/**
 * 
 */
package concurrency.queue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * 搜索线程
 *
 * @author 刘晨伟
 *
 * 创建日期：2013-6-12
 */
public class SearchTask implements Runnable {

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