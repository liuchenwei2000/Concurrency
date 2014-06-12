/**
 * 
 */
package concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ���ֲ�ͬ�������Ƶĵȼ۹�ϵ
 * <p>
 * ������������������ò�ͬ�ķ�ʽ���в������ƣ���Ч���ǵȼ۵ġ�
 * <p>
 * �������ַ�ʽ��ѡ��ԭ��
 * <li>1�����synchronized�ؼ��ֿ��Թ������Ǿ�����ʹ�������������Լ��ٴ����������������
 * <li>2��ֻ���ڷǳ���ҪLock/Condition�ṹ�Ķ������Ե�ʱ���ʹ�����ǡ�
 *
 * @author ����ΰ
 *
 * �������ڣ�2013-6-8
 */
public class SynchronizedAndLockTest {

	/**
	 * ʹ��synchronizedʵ�ֲ�������
	 */
	public synchronized void method1() throws InterruptedException {
		// do something
		boolean flag = true;// ģ���߳����е�ĳ������
		while(flag){
			wait();// ���̼߳��뵽�ȴ�����
		}
		// do something
		// notify();// ������һ���ȴ��̵߳�����״̬
		notifyAll();// ������еȴ��̵߳�����״̬
	}
	
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	
	/**
	 * ʹ��lock����ʵ�ֲ�������
	 */
	public void method2() throws InterruptedException {
		lock.lock();
		try {
			// do something
			boolean flag = true;// ģ���߳����е�ĳ������
			while(flag){
				condition.await();
			}
			// do something
			// condition.signal();
			condition.signalAll();
			/**
			 * ��Ϊwait��notify��notifyAll��������Object�࣬����Condition�ķ���ֻ����await��signal��signalAll����
			 * �����Ͳ����Object���еķ�������ͻ�ˡ�
			 */
		} finally {
			lock.unlock();
		}
	}
}