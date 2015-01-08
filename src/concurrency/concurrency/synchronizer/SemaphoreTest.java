/**
 * 
 */
package concurrency.synchronizer;

import java.util.concurrent.Semaphore;

/**
 * Semaphoreʾ��
 * <p>
 * �ź�����һ��������������������һ����������������Դ�ķ��ʡ�
 * Semaphore��ʾһ���ź�������������������֤����ɵ���Ŀ�����޵ģ�������������ͨ�����߳�����
 * ʵ���ϣ�û��ʲô��ɶ���Semaphore��ά��һ����������
 * <p>
 * ��һ���߳���Ҫ���ʹ�����Դʱ��������Ҫ���ź�����ȡ��ɣ�����ź������ڲ���������0
 * ����ζ���п�����Դ�ɹ�ʹ�ã��򽫼�����һ��������̷߳��ʹ�����Դ��
 * �������0����ζ��û�п�����Դ����Ὣ���߳�����ֱ���ڲ���������0��
 * ���̷߳��ʹ�����Դ����ʱ�������ͷų��е��ź�����ɣ������ź������ڲ��������һ��
 * <p>
 * �������ź�����һ����������������Ψһ������Դ�ķ��ʣ��ڲ�����ֻ��0��1����ֵ��
 * <p>
 * ͨ���������ƿ��Է���ĳЩ��Դ���߳���Ŀ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-28
 */
public class SemaphoreTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ģ��8������ͬһ��������ľ������
		Toilet toilet = new Toilet();
		for (int i = 0; i < 8; i++) {
			new Thread(new ToToiletTask("Person" + (i + 1), toilet)).start();
		}
	}

	/**
	 * ������
	 * <p>
	 * ����һ��ϡȱ����Դ�������һ������ʱ�򣬱�����Ŷ�ʹ�ã�����ģ��������̡�
	 */
	private static class Toilet {

		private static final int MAX = 3;

		// �ź������󣬱�������ֻ��3����λ
		private final Semaphore semaphore = new Semaphore(MAX);
		// ��λʹ�����
		private boolean[] used = new boolean[MAX];

		/**
		 * ʹ�ò���
		 */
		public void use() {
			/* 
			 * �����try-catch-finally�ṹ��ʹ��Semaphore�ĵ����÷���
			 * 
			 * 1������acquire()������ȡSemaphore����ɡ�
			 * 2���Թ�����Դ���з��ʡ�
			 * 3������release()�����ͷų��е�Semaphore��ɡ�
			 */
			try {
				/*
				 * ���ź�����ȡһ����ɣ����ṩһ�����֮ǰ��һֱ�������̡߳�
				 * �����һ����ɽ��������أ����������������1��
				 */
				semaphore.acquire();
				// ���ź���û����ɶ������߳������ȴ�ʱ�����ܻᷢ���жϣ�ʹ������ķ�ʽ��������ж��쳣��
//				semaphore.acquireUninterruptibly();
				// ����ķ���������ܷ�����ɣ�������򷵻�true��ͬʱ�߳̽�����һ����ɣ�����������������false�������ǵȴ������߳��ͷ���ɡ�
//				boolean result = semaphore.tryAcquire();
				int index = getUnusedIndex();
				System.out.println("�� " + (index + 1) + " ����λʹ���С���");
				Thread.sleep((long) (Math.random() * 2000));// ģ���ϲ���
				markUsed(index, false);
				System.out.println("�� " + (index + 1) + " ����λʹ�����!!!");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				/*
				 * �ͷ�һ����ɣ����ź��������������1�� 
				 * �������߳���ͼ��ȡ��ɣ���ѡ��һ���̲߳����ո��ͷŵ���ɸ�������
				 */
				semaphore.release();
			}
		}

		/**
		 * �ź�����װ�����ͬ�����������ƶ�������ķ��ʣ���ͬά�������䱾��һ���������ͬ���Ƿֿ��ġ�
		 * ���仰˵���ź�����ͬ����֤��ͬʱֻ�������޵��̷߳�����Դ(������)��
		 * ����Դ�����ͬ����Ϊ�˱�֤�����޵��߳�֮�侺��(��λ)ʱ������һ���ԡ�
		 * ���û����Դ�����ͬ�������ܻᵼ�����������������ͬһ����λ��
		 */
		private synchronized int getUnusedIndex() {
			for (int i = 0; i < used.length; i++) {
				if (!used[i]) {
					markUsed(i, true);
					return i;
				}
			}
			return -1;// ��Ӧ����ִ�е�����
		}

		private synchronized void markUsed(int index, boolean b) {
			used[index] = b;
		}
	}

	/**
	 * ģ������������Ĺ���
	 */
	private static class ToToiletTask implements Runnable {

		private String person;
		private Toilet toilet;

		public ToToiletTask(String person, Toilet toilet) {
			this.person = person;
			this.toilet = toilet;
		}

		@Override
		public void run() {
			try {
				System.out.println(person + " Ҫ�ϲ�����");
				toilet.use();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}