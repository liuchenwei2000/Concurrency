/**
 * 
 */
package thread.callable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * MatchCounterʾ��
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
		
		MatchCounter counter = new MatchCounter(dirPath, keyword);
		FutureTask<Integer> task = new FutureTask<Integer>(counter);
		new Thread(task).start();
		
		try {
			// ��get()�����ĵ��ûᷢ������ֱ���пɻ�õĽ��Ϊֹ
			System.out.println(task.get() + " files matched.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}