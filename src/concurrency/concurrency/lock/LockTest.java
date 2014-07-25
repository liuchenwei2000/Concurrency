/**
 * 
 */
package concurrency.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ������
 * <p>
 * ��Ҫע����ǣ�ÿһ�����������Լ�����������������߳���ͼ����ͬһ���������ͻᴮ�еķ����ڷ��ʡ�
 * ���ǣ���������̷߳��ʲ�ͬ�Ķ�����ôûһ���̶߳���õ�һ����ͬ���������߶����ᷢ��������
 * <p>
 * ��ϣ��������Щ����������ܸ��»���һ�����ݽṹ�Ĵ���飬
 * �ͱ���ȷ����Щ�������֮�������̲߳ſ���ʹ����ͬ�Ķ���
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-8
 */
public class LockTest {

	// ������
	private Lock lock = new ReentrantLock();
	
	/**
	 * ʹ��lock�������ͷ����ĵ��ʹ���
	 * <p>
	 * ��synchronized��ͬ���ǣ�ʹ��lock������������Ҫ��ʽ���á�
	 */
	public void test() {
		/*
		 * ���ֽṹ��֤���κ�ʱ��ֻ��һ���߳��ܹ������ٽ�����
		 * 
		 * ��������ɹ���������һ�û���ͷŸ������߳���ӵ�С�
		 * һ��һ���߳���ס�������������̶߳��޷�ͨ��lock�����ڽ��м�����
		 * �������̵߳���lock����ʱ�����ǻᱻ������ֱ����һ���߳��ͷ�������
		 */
		lock.lock();
		try {
			// do something
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// һ��Ҫ��finally�н����ͷ���
			lock.unlock();
		}
	}
}