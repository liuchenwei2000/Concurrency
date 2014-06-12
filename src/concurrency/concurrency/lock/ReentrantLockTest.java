/**
 * 
 */
package concurrency.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock����
 * <p>
 * ReentrantLock�ǿ����������
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-8
 */
public class ReentrantLockTest {

	// ��������
	private ReentrantLock lock = new ReentrantLock();
	
	public void test(){
		showThreadHoldCount("test() begins...");
		lock.lock();
		try {
			showThreadHoldCount("test() add lock...");
			/**
			 * ��Ϊ�߳��ܹ��ظ��Ļ�ȡ���Ѿ�ӵ�е����� ������ά��һ�����м�����׷�ٶ�lock������Ƕ�׵��á�
			 * �߳���ÿ�ε���lock��Ҫ����unlock���ͷ���������������ԣ���һ���������Ĵ�����Ե�����һ��ʹ����ͬ�������ķ����� 
			 * 
			 * ����ԭ�����£�
			 * test��������anotherMethod��������anotherMethod����Ҳ����סlock�������ڸ�lock����ĳ�������2��
			 * ��anotherMethod�����˳��󣬳��������1����test�����˳��󣬳��������0���߳̾Ͱ����ͷ��ˡ�
			 */
			anotherMethod();
			showThreadHoldCount("anotherMethod() done...");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
			showThreadHoldCount("test() remove lock...");
		}
	}

	private void anotherMethod() {
		lock.lock();
		try {
			showThreadHoldCount("anotherMethod() add lock...");
			// do something
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	private void showThreadHoldCount(String msg){
		System.out.println(msg);
		Thread t = Thread.currentThread();
		// ��ѯ��ǰ�̱߳��ִ����Ĵ�����
		int holdCount = lock.getHoldCount();
		System.out.println(t.getName() + " holds lock count=" + holdCount);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ReentrantLockTest().test();
	}
}