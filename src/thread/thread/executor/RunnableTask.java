/**
 * 
 */
package thread.executor;

/**
 * 一个RunnableTask实现，供其他Test类使用
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-25
 */
public class RunnableTask implements Runnable {

	/**
	 * (non-Javadoc)
	 *
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("In Runnable");
	}
}