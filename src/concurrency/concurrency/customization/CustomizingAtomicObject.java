/**
 * 
 */
package concurrency.customization;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义原子对象类示例
 * <p>
 * 通过继承原子对象类来实现自定义原子对象，从而保证某些操作可以以原子的方式完成。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月22日
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
	 * 停车计数器，这是个自定义原子对象类
	 */
	private static class ParkingCounter extends AtomicInteger {
		
		private static final long serialVersionUID = 1L;
		
		private int maxNumber;// 最大停车数

		public ParkingCounter(int maxNumber) {
			this.maxNumber = maxNumber;
			set(0);// 计数器初始值
		}
		
		/**
		 * 汽车进入，要保证本操作是原子的
		 * <p>
		 * 通过比较停车场中实际停车数和最大停车数，如果它们相等则无法再让车进入，方法返回false；否则使用如下的结构来保证操作原子性：
		 * 1，用一个本地变量获取原子对象的值。
		 * 2，用另一个本地变量存储新值。
		 * 3，使用compareAndSet()方法尝试将旧值替换成新值。如果这个方法返回true，表明作为参数传入的旧值还是这个变量的值，
		 * 新值将会被赋给变量。随着carIn()方法返回true值，这个操作将以原子方式完成。
		 * 如果compareAndSet()方法返回false，表明作为参数传入的旧值不再是这个变量的值（其他线程已修改了它），
		 * 所以这个操作不能以原子方式完成。这个操作将重新开始，直到它可以以原子方式完成。
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
		 * 汽车离开，要保证本操作是原子的
		 * <p>
		 * 原理同 carIn() 方法。
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
	 * 传感器1，指挥多个车的进出
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
	 * 传感器2，指挥多个车的进出
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
