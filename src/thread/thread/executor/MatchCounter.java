/**
 * 
 */
package thread.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * ƥ��ͳ����
 * <p>
 * ������ thread.callable.MatchCounter�����ӹ�������ȫһ�£�ֻ��ʵ�����в�ͬ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-17
 */
public class MatchCounter implements Callable<Integer> {

	// ʹ�õ��̳߳�
	private ExecutorService threadPool;
	
	private String dir;
	private String keyword;
	
	private int counter = 0;
	
	public MatchCounter(String dir, String keyword, ExecutorService threadPool) {
		this.dir = dir;
		this.keyword = keyword;
		this.threadPool = threadPool;
	}

	@Override
	public Integer call() throws Exception {
		File dirFile = new File(dir);
		File[] files = dirFile.listFiles();

		List<Future<Integer>> results = new ArrayList<Future<Integer>>();
		for (File file : files) {
			if (file.isDirectory()) {
				MatchCounter mcounter = new MatchCounter(file.getAbsolutePath(), keyword, threadPool);
				// ʹ���̳߳���ִ�����񣬶��������ֹ������߳�
				Future<Integer> result = threadPool.submit(mcounter);
				results.add(result);
			} else {
				if (search(file)) {
					counter++;
				}
			}
		}
		for (Future<Integer> result : results) {
			counter += result.get();
		}
		return counter;
	}
	
	private boolean search(File file) {
		boolean found = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String temp = null;
			while ((!found) && (temp = br.readLine()) != null) {
				if (temp.contains(keyword)) {
					found = true;
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return found;
	}
}