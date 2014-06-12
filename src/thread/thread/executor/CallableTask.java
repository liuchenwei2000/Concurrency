/**
 * 
 */
package thread.executor;

import java.util.concurrent.Callable;

/**
 * 一个Callable实现，供其他Test类使用
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-25
 */
public class CallableTask implements Callable<Integer> {

	/**
	 * (non-Javadoc)
	 *
	 * @see java.util.concurrent.Callable#call()
	 */
	public Integer call() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("In Callable");
		return 0;
	}
}