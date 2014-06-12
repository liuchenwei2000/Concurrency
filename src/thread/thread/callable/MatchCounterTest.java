/**
 * 
 */
package thread.callable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * MatchCounter示例
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-17
 */
public class MatchCounterTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dirPath = "./src";// 搜本Project下的源码文件
		String keyword = "java";
		
		MatchCounter counter = new MatchCounter(dirPath, keyword);
		FutureTask<Integer> task = new FutureTask<Integer>(counter);
		new Thread(task).start();
		
		try {
			// 对get()方法的调用会发生阻塞直到有可获得的结果为止
			System.out.println(task.get() + " files matched.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}