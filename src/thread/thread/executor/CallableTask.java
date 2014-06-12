/**
 * 
 */
package thread.executor;

import java.util.concurrent.Callable;

/**
 * һ��Callableʵ�֣�������Test��ʹ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-25
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