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
 * ƥ��ͳ����
 * <p>
 * ������concurrency.queue.BlockingQueueTest���������ơ�
 * �����ڽ����Ǽ���ƥ��ؼ��ֵ��ļ������������Ҫһ����ʱ�����е����񣬷���һ��������
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-17
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

		// ������Ÿ����̵߳ļ�����
		List<Future<Integer>> results = new ArrayList<Future<Integer>>();
		
		for (File file : files) {
			// ����ÿһ����Ŀ¼��ʹ��һ���������߳�ȥִ������
			if (file.isDirectory()) {
				MatchCounter mcounter = new MatchCounter(file.getAbsolutePath(), keyword);
				FutureTask<Integer> task = new FutureTask<Integer>(mcounter);
				new Thread(task).start();
				results.add(task);
			} else {
				// �����ļ�ֱ�ӽ�����������ƥ���Ͼͽ�������+1
				if (search(file)) {
					counter++;
				}
			}
		}
		// ������е���Ŀ¼������кϼ�
		for (Future<Integer> result : results) {
			// ��Ȼÿ�ε���get()��������ֱ������ɻ��Ϊֹ�����߳��ǲ������еģ����Բ�����Ҫ�ȴ����
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