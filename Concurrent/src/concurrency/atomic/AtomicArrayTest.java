/**
 * 
 */
package concurrency.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 可以用原子方式更新其元素的数组示例
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月21日
 */
public class AtomicArrayTest {

	/**
	 * 对于同一个AtomicIntegerArray中的元素，使用100个线程将其内元素执行 +1 操作，另外100个线程执行 -1 操作。
	 * 看最后AtomicIntegerArray的元素是否还是初始值 100，如果都还是100则证明 AtomicIntegerArray 可以用原子方式更新其元素。
	 */
	public static void main(String[] args) {
		int threadsNumber = 100;
		
		Thread[] threads1 = new Thread[threadsNumber];
		Thread[] threads2 = new Thread[threadsNumber];
		
		// 创建原子整形数组，另外还有 AtomicLongArray 供使用
		AtomicIntegerArray array = new AtomicIntegerArray(1000);
		for (int i = 0; i < array.length(); i++) {
			array.set(i, 100);// 设置数组指定位置的元素值
		}
		// 分别启动100个线程执行递增和递减操作
		for (int i = 0; i < threadsNumber; i++) {
			threads1[i] = new Thread(new Incrementer(array));
			threads2[i] = new Thread(new Decrementer(array));
			
			threads1[i].start();
			threads2[i].start();
		}
		// 等待线程全部结束
		for (int i = 0; i < threadsNumber; i++) {
			try {
				threads1[i].join();
				threads2[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < array.length(); i++) {
			// 获取数组中指定位置的元素
			if (array.get(i) != 100) {// 如果元素值不等于初始值则说明出现了数据不一致
				System.out.println("Array[" + i + "] : " + array.get(i));
			}
		}
		
		System.out.println("Main: End of the example");
	}

	/**
	 * 递增器
	 */
	private static class Incrementer implements Runnable {

		private AtomicIntegerArray array;

		public Incrementer(AtomicIntegerArray array) {
			this.array = array;
		}

		@Override
		public void run() {
			// 对数组中的每一个元素都 +1
			for (int i = 0; i < array.length(); i++) {
				array.getAndIncrement(i);// 以原子方式将索引 i 的元素加 1
			}
		}
	}

	/**
	 * 递减器
	 */
	private static class Decrementer implements Runnable {

		private AtomicIntegerArray array;

		public Decrementer(AtomicIntegerArray array) {
			this.array = array;
		}

		@Override
		public void run() {
			// 对数组中的每一个元素都 -1
			for (int i = 0; i < array.length(); i++) {
				array.getAndDecrement(i);// 以原子方式将索引 i 的元素减 1
			}
		}
	}
}
