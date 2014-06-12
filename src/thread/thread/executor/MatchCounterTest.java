/**
 * 
 */
package thread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 使用线程池MatchCounter示例
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
		
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		MatchCounter counter = new MatchCounter(dirPath, keyword, threadPool);
		Future<Integer> result = threadPool.submit(counter);
		
		try {
			System.out.println(result.get() + " files matched.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			threadPool.shutdown();// 关闭线程池
		}
	}
}