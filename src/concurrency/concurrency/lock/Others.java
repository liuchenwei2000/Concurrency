/**
 * 
 */
package concurrency.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ��������һЩ�ص���ʾ
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2007-12-13
 */
public class Others {

	/**
	 * �������ַ�ʽ�ᴴ��һ����ƽ������
	 * <p>
	 * ��ƽ�����Ի��Ŵ���Щ�ȴ����ʱ����̣߳����Ǳ�֤��ƽ���ܻἫ��Ӱ������(����ͨ�����ܶ�)��
	 * ���⣬����ʹ���˹�ƽ����Ҳ���ܱ�֤�̵߳������ǹ�ƽ�ġ�
	 * ����̵߳�����ѡ�����һ���ȴ��˺ܳ�ʱ����̣߳��Ǵ��̸߳���û����õ����Ĺ�ƽ�Դ���
	 */
	private ReentrantLock lock = new ReentrantLock(true);
	
	public void method() throws InterruptedException {
		/**
		 * tryLock()����
		 * <p>
		 * �����û�б���һ���̱߳��֣��÷�����������trueֵ����ǰ�߳̽����������������ı��ּ�������Ϊ1��
		 * �����ǰ�߳��Ѿ����������򽫱��ּ�����1���÷���������true ��
		 * �����������һ���̱߳��֣��˷�������������falseֵ��
		 * ������Ϊ���Ի�����Ĳ����趨ʱ�ޣ�����ʱ�޻�δ����򷵻�false��
		 * <p>
		 * ��һ����ʱ������tryLock�����ڴ���ƽ��ʱ��lock������һ���ġ�
		 * ���޲�tryLock�������ῼ�ǹ�ƽ�ԣ������������ʱ���ɻ�ã���ǰ�߳�������������������Ƿ����̵߳ȴ��˺ܾá�
		 * ������뷢�����������飬�������������ַ�ʽһ����á�
		 */
		if (lock.tryLock() || lock.tryLock(10, TimeUnit.SECONDS)) {
			lock.lock();
			try {
				// ...
			} finally {
				lock.unlock();
			}
		} else {
			// do something else
		}
	}
}