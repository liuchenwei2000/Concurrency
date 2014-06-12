/**
 * 
 */
package concurrency.lock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock����
 * <p>
 * ReentrantReadWriteLock�ǿ�����Ķ�д����
 * <p>
 * Ӧ�ó��������кܶ��̶߳���ĳ�����ݽṹ�ж�ȡ���ݶ��������̶߳�������޸�ʱ��
 * ����������£������ȡ�̹߳�������Ǻ��ʵģ���д���߳���Ȼ�����ǻ�����ʵġ�
 * <p>
 * ����������ʹ���ڻ����У���Ϊ�����еĶ��󱻹�����д����Ķ�������ż���Ż��޸���������е����ݡ�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-8
 */
public class ReentrantReadWriteLockTest {

	// ��������
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	// �������ɱ�������������õ����������ų�����д����
	private Lock readLock = lock.readLock();
	// д�������ų�����������д�����Ͷ�����
	private Lock writeLock = lock.writeLock();
	
	private double totalNumber = 0;
	
	/**
	 * ��ȡ����
	 */
	public double getTotalNumber() {
		/**
		 * �̻߳�ȡ������ǰ��������
		 * 1��û�������̳߳���д����
		 * 2��û��д���������д���󣬵������̺߳ͳ��������߳���ͬһ����
		 */
		readLock.lock();
		System.out.println("Thread " + Thread.currentThread().getName() + " ����˶�����");
		try {
			return totalNumber;
		} finally {
			readLock.unlock();
			System.out.println("Thread " + Thread.currentThread().getName() + " �ͷ��˶�����");
		}
	}

	/**
	 * �޸�����
	 */
	public double setTotalNumber(int n) {
		/**
		 * �߳̽���д����ǰ��������
		 * 1��û�������̳߳��ж�����
		 * 2��û�������̳߳���д����
		 */
		writeLock.lock();
		System.out.println("Thread " + Thread.currentThread().getName() + " �����д����");
		try {
			totalNumber = n;
			Thread.sleep(3);// Ϊ��ʹ���н����������ӳ�3����
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			writeLock.unlock();
			System.out.println("Thread " + Thread.currentThread().getName() + " �ͷ���д����");
		}
		return totalNumber;
	}
	
	static class TestTask implements Runnable {
		
		private Random random = new Random();
		private ReentrantReadWriteLockTest test;

		public TestTask(ReentrantReadWriteLockTest test) {
			this.test = test;
		}

		public void run() {
			// �޸�����
			test.setTotalNumber(random.nextInt(10));
			// ��ȡ����
			System.out.println("Thread "
					+ Thread.currentThread().getName() + " : "
					+ test.getTotalNumber());
		}
	}
 	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ReentrantReadWriteLockTest test = new ReentrantReadWriteLockTest();
		// 10���̣߳���ͬ�޸�/����ͬһ��ReentrantReadWriteLockTest����
		Thread[] threads = new Thread[10];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new TestTask(test));
		}
		
		for (Thread thread : threads) {
			thread.start();
		}
		/**
		 ĳ����������
Thread Thread-0 �����д����
Thread Thread-0 �ͷ���д����
Thread Thread-1 �����д����
Thread Thread-1 �ͷ���д����
Thread Thread-3 �����д����
Thread Thread-3 �ͷ���д����
Thread Thread-2 �����д����
Thread Thread-2 �ͷ���д����
Thread Thread-5 �����д����
Thread Thread-5 �ͷ���д����
Thread Thread-6 �����д����
Thread Thread-6 �ͷ���д����
Thread Thread-4 �����д����
Thread Thread-4 �ͷ���д����
Thread Thread-7 �����д����
Thread Thread-7 �ͷ���д����
Thread Thread-8 �����д����
Thread Thread-8 �ͷ���д����
Thread Thread-9 �����д����
Thread Thread-9 �ͷ���д����
Thread Thread-0 ����˶�����
Thread Thread-3 ����˶�����
Thread Thread-5 ����˶�����
Thread Thread-1 ����˶�����
Thread Thread-9 ����˶�����
Thread Thread-1 �ͷ��˶�����
Thread Thread-8 ����˶�����
Thread Thread-5 �ͷ��˶�����
Thread Thread-7 ����˶�����
Thread Thread-4 ����˶�����
Thread Thread-6 ����˶�����
Thread Thread-3 �ͷ��˶�����
Thread Thread-0 �ͷ��˶�����
Thread Thread-2 ����˶�����
Thread Thread-6 �ͷ��˶�����
Thread Thread-4 �ͷ��˶�����
Thread Thread-7 �ͷ��˶�����
Thread Thread-1 : 3.0
Thread Thread-0 : 3.0
Thread Thread-8 �ͷ��˶�����
Thread Thread-9 �ͷ��˶�����
Thread Thread-8 : 3.0
Thread Thread-5 : 3.0
Thread Thread-3 : 3.0
Thread Thread-4 : 3.0
Thread Thread-7 : 3.0
Thread Thread-6 : 3.0
Thread Thread-2 �ͷ��˶�����
Thread Thread-9 : 3.0
Thread Thread-2 : 3.0 
		 */
	}
}