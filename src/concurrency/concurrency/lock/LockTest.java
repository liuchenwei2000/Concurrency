/**
 * 
 */
package concurrency.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock����ʾ��
 * <p>
 * ��ϣ��������Щ����������ܸ��»���һ�����ݽṹ�Ĵ���飬�ͱ���ȷ����Щ�������֮�������̲߳ſ���ʹ����ͬ�Ķ���
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-8
 */
public class LockTest {

	// ������ʹ�ÿ�������
	private Lock lock = new ReentrantLock();
	
	/**
	 * ʹ��lock�������ͷ����ĵ��ʹ���
	 * <p>
	 * ��synchronized��ͬ���ǣ�ʹ��lock������������Ҫ��ʽ���á�
	 */
	public void test() {
		/*
		 * ���ֽṹ��֤���κ�ʱ��ֻ��һ���߳��ܹ������ٽ�����critical section����
		 * 
		 * ��������ɹ���������һ�û���ͷŸ������߳���ӵ�С�һ��һ���̳߳��������������̶߳��޷�ͨ��lock�����ٽ��м�����
		 * �������̵߳���lock����ʱ�����ǻᱻ������ֱ�����������߳��ͷ�����
		 */
		lock.lock();
		try {
			// do something
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// һ��Ҫ��finally���ͷ���
			lock.unlock();
		}
	}
}