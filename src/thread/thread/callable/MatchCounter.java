/**
 * 
 */
package thread.callable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 匹配统计器
 * <p>
 * 本例与concurrency.queue.BlockingQueueTest的例子相似。
 * 但现在仅仅是计算匹配关键字的文件数量，因此需要一个长时间运行的任务，返回一个整数。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-17
 */
public class MatchCounter implements Callable<Integer> {

	private String dir;
	private String keyword;
	
	private int counter = 0;
	
	public MatchCounter(String dir, String keyword) {
		this.dir = dir;
		this.keyword = keyword;
	}

	@Override
	public Integer call() throws Exception {
		File dirFile = new File(dir);
		File[] files = dirFile.listFiles();

		// 用来存放各个线程的计算结果
		List<Future<Integer>> results = new ArrayList<Future<Integer>>();
		
		for (File file : files) {
			// 对于每一个子目录都使用一个单独的线程去执行任务
			if (file.isDirectory()) {
				MatchCounter mcounter = new MatchCounter(file.getAbsolutePath(), keyword);
				FutureTask<Integer> task = new FutureTask<Integer>(mcounter);
				new Thread(task).start();
				results.add(task);
			} else {
				// 对于文件直接进行搜索，若匹配上就将计数器+1
				if (search(file)) {
					counter++;
				}
			}
		}
		// 最后将所有的子目录结果进行合计
		for (Future<Integer> result : results) {
			// 虽然每次调用get()都会阻塞直到结果可获得为止，但线程是并行运行的，所以并不需要等待多久
			counter += result.get();
		}
		return counter;
	}
	
	private boolean search(File file) {
		boolean found = false;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String temp = null;
			while ((!found) && (temp = br.readLine()) != null) {
				if (temp.contains(keyword)) {
					found = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return found;
	}
}