/**
 * 
 */
package thread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ʹ���̳߳�MatchCounterʾ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-17
 */
public class MatchCounterTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dirPath = "./src";// �ѱ�Project�µ�Դ���ļ�
		String keyword = "java";
		
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		MatchCounter counter = new MatchCounter(dirPath, keyword, threadPool);
		Future<Integer> result = threadPool.submit(counter);
		
		try {
			System.out.println(result.get() + " files matched.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			threadPool.shutdown();// �ر��̳߳�
		}
	}
}