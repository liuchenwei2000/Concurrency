/**
 * 
 */
package concurrency.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * ������ԭ�ӷ�ʽ������Ԫ�ص�����ʾ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��21��
 */
public class AtomicArrayTest {

	/**
	 * ����ͬһ��AtomicIntegerArray�е�Ԫ�أ�ʹ��100���߳̽�����Ԫ��ִ�� +1 ����������100���߳�ִ�� -1 ������
	 * �����AtomicIntegerArray��Ԫ���Ƿ��ǳ�ʼֵ 100�����������100��֤�� AtomicIntegerArray ������ԭ�ӷ�ʽ������Ԫ�ء�
	 */
	public static void main(String[] args) {
		int threadsNumber = 100;
		
		Thread[] threads1 = new Thread[threadsNumber];
		Thread[] threads2 = new Thread[threadsNumber];
		
		// ����ԭ���������飬���⻹�� AtomicLongArray ��ʹ��
		AtomicIntegerArray array = new AtomicIntegerArray(1000);
		for (int i = 0; i < array.length(); i++) {
			array.set(i, 100);// ��������ָ��λ�õ�Ԫ��ֵ
		}
		// �ֱ�����100���߳�ִ�е����͵ݼ�����
		for (int i = 0; i < threadsNumber; i++) {
			threads1[i] = new Thread(new Incrementer(array));
			threads2[i] = new Thread(new Decrementer(array));
			
			threads1[i].start();
			threads2[i].start();
		}
		// �ȴ��߳�ȫ������
		for (int i = 0; i < threadsNumber; i++) {
			try {
				threads1[i].join();
				threads2[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < array.length(); i++) {
			// ��ȡ������ָ��λ�õ�Ԫ��
			if (array.get(i) != 100) {// ���Ԫ��ֵ�����ڳ�ʼֵ��˵�����������ݲ�һ��
				System.out.println("Array[" + i + "] : " + array.get(i));
			}
		}
		
		System.out.println("Main: End of the example");
	}

	/**
	 * ������
	 */
	private static class Incrementer implements Runnable {

		private AtomicIntegerArray array;

		public Incrementer(AtomicIntegerArray array) {
			this.array = array;
		}

		@Override
		public void run() {
			// �������е�ÿһ��Ԫ�ض� +1
			for (int i = 0; i < array.length(); i++) {
				array.getAndIncrement(i);// ��ԭ�ӷ�ʽ������ i ��Ԫ�ؼ� 1
			}
		}
	}

	/**
	 * �ݼ���
	 */
	private static class Decrementer implements Runnable {

		private AtomicIntegerArray array;

		public Decrementer(AtomicIntegerArray array) {
			this.array = array;
		}

		@Override
		public void run() {
			// �������е�ÿһ��Ԫ�ض� -1
			for (int i = 0; i < array.length(); i++) {
				array.getAndDecrement(i);// ��ԭ�ӷ�ʽ������ i ��Ԫ�ؼ� 1
			}
		}
	}
}