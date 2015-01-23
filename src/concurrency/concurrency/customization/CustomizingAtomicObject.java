/**
 * 
 */
package concurrency.customization;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * �Զ���ԭ�Ӷ�����ʾ��
 * <p>
 * ͨ���̳�ԭ�Ӷ�������ʵ���Զ���ԭ�Ӷ��󣬴Ӷ���֤ĳЩ����������ԭ�ӵķ�ʽ��ɡ�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��22��
 */
public class CustomizingAtomicObject {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ParkingCounter counter = new ParkingCounter(3);

		Sensor1 sensor1 = new Sensor1(counter);
		Sensor2 sensor2 = new Sensor2(counter);

		Thread thread1 = new Thread(sensor1);
		Thread thread2 = new Thread(sensor2);

		thread1.start();
		thread2.start();

		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.printf("Main: Number of cars: %d\n", counter.get());
		System.out.printf("Main: End of the program.\n");
	}

	/**
	 * ͣ�������������Ǹ��Զ���ԭ�Ӷ�����
	 */
	private static class ParkingCounter extends AtomicInteger {
		
		private static final long serialVersionUID = 1L;
		
		private int maxNumber;// ���ͣ����

		public ParkingCounter(int maxNumber) {
			this.maxNumber = maxNumber;
			set(0);// ��������ʼֵ
		}
		
		/**
		 * �������룬Ҫ��֤��������ԭ�ӵ�
		 * <p>
		 * ͨ���Ƚ�ͣ������ʵ��ͣ���������ͣ�������������������޷����ó����룬��������false������ʹ�����µĽṹ����֤����ԭ���ԣ�
		 * 1����һ�����ر�����ȡԭ�Ӷ����ֵ��
		 * 2������һ�����ر����洢��ֵ��
		 * 3��ʹ��compareAndSet()�������Խ���ֵ�滻����ֵ����������������true��������Ϊ��������ľ�ֵ�������������ֵ��
		 * ��ֵ���ᱻ��������������carIn()��������trueֵ�������������ԭ�ӷ�ʽ��ɡ�
		 * ���compareAndSet()��������false��������Ϊ��������ľ�ֵ���������������ֵ�������߳����޸���������
		 * �����������������ԭ�ӷ�ʽ��ɡ�������������¿�ʼ��ֱ����������ԭ�ӷ�ʽ��ɡ�
		 */
		public boolean carIn() {
			int value = get();// 1
			while (true) {
				if (value == maxNumber) {
					System.out.println("ParkingCounter: The parking lot is full.");
					return false;
				} else {
					int newValue = value + 1;// 2
					boolean changed = compareAndSet(value, newValue);// 3
					if (changed) {
						System.out.println("ParkingCounter: A car has entered. "
										+ (maxNumber - newValue) + " left.");
						return true;
					}
				}
			}
		}

		/**
		 * �����뿪��Ҫ��֤��������ԭ�ӵ�
		 * <p>
		 * ԭ��ͬ carIn() ������
		 */
		public boolean carOut() {
			int value = get();
			while (true) {
				if (value == 0) {
					System.out.println("ParkingCounter: The parking lot is empty.");
					return false;
				} else {
					int newValue = value - 1;
					boolean changed = compareAndSet(value, newValue);
					if (changed) {
						System.out.println("ParkingCounter: A car has gone out." 
										+ (maxNumber - newValue) + " left.");
						return true;
					}
				}
			}
		}
	}
	
	/**
	 * ������1��ָ�Ӷ�����Ľ���
	 */
	private static class Sensor1 implements Runnable {

		private ParkingCounter counter;
		
		public Sensor1(ParkingCounter counter) {
			this.counter = counter;
		}

		@Override
		public void run() {
			counter.carIn();
			counter.carIn();
			counter.carIn();
			counter.carIn();
			counter.carOut();
			counter.carOut();
			counter.carOut();
			counter.carIn();
			counter.carIn();
			counter.carIn();
		}
	}
	
	/**
	 * ������2��ָ�Ӷ�����Ľ���
	 */
	private static class Sensor2 implements Runnable {

		private ParkingCounter counter;
		
		public Sensor2(ParkingCounter counter) {
			this.counter = counter;
		}

		@Override
		public void run() {
			counter.carIn();
			counter.carOut();
			counter.carOut();
			counter.carIn();
			counter.carIn();
			counter.carIn();
			counter.carIn();
			counter.carIn();
			counter.carIn();
		}
	}
}
