/**
 * 
 */
package thread.executor;

/**
 * һ��RunnableTaskʵ�֣�������Test��ʹ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-25
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