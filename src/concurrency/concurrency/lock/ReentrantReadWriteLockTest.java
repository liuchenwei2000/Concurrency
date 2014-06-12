/**
 * 
 */
package concurrency.lock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock测试
 * <p>
 * ReentrantReadWriteLock是可重入的读写锁。
 * <p>
 * 应用场景：当有很多线程都从某个数据结构中读取数据而很少有线程对其进行修改时。
 * 在这种情况下，允许读取线程共享访问是合适的，但写入线程依然必须是互斥访问的。
 * <p>
 * 此锁被大量使用在缓存中，因为缓存中的对象被共享会有大量的读操作，偶尔才会修改这个对象中的数据。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-8
 */
public class ReentrantReadWriteLockTest {

	// 可重入锁
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	// 读锁，可被多个读操作共用的锁，但会排斥所有写操作
	private Lock readLock = lock.readLock();
	// 写锁，会排斥所有其他的写操作和读操作
	private Lock writeLock = lock.writeLock();
	
	private double totalNumber = 0;
	
	/**
	 * 读取数据
	 */
	public double getTotalNumber() {
		/**
		 * 线程获取读锁的前提条件：
		 * 1，没有其他线程持有写锁。
		 * 2，没有写请求或者有写请求，但调用线程和持有锁的线程是同一个。
		 */
		readLock.lock();
		System.out.println("Thread " + Thread.currentThread().getName() + " 获得了读锁。");
		try {
			return totalNumber;
		} finally {
			readLock.unlock();
			System.out.println("Thread " + Thread.currentThread().getName() + " 释放了读锁。");
		}
	}

	/**
	 * 修改数据
	 */
	public double setTotalNumber(int n) {
		/**
		 * 线程进入写锁的前提条件：
		 * 1，没有其他线程持有读锁。
		 * 2，没有其他线程持有写锁。
		 */
		writeLock.lock();
		System.out.println("Thread " + Thread.currentThread().getName() + " 获得了写锁。");
		try {
			totalNumber = n;
			Thread.sleep(3);// 为了使运行结果更随机，延迟3毫秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			writeLock.unlock();
			System.out.println("Thread " + Thread.currentThread().getName() + " 释放了写锁。");
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
			// 修改数据
			test.setTotalNumber(random.nextInt(10));
			// 读取数据
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
		// 10个线程，共同修改/访问同一个ReentrantReadWriteLockTest对象
		Thread[] threads = new Thread[10];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new TestTask(test));
		}
		
		for (Thread thread : threads) {
			thread.start();
		}
		/**
		 某次输出结果：
Thread Thread-0 获得了写锁。
Thread Thread-0 释放了写锁。
Thread Thread-1 获得了写锁。
Thread Thread-1 释放了写锁。
Thread Thread-3 获得了写锁。
Thread Thread-3 释放了写锁。
Thread Thread-2 获得了写锁。
Thread Thread-2 释放了写锁。
Thread Thread-5 获得了写锁。
Thread Thread-5 释放了写锁。
Thread Thread-6 获得了写锁。
Thread Thread-6 释放了写锁。
Thread Thread-4 获得了写锁。
Thread Thread-4 释放了写锁。
Thread Thread-7 获得了写锁。
Thread Thread-7 释放了写锁。
Thread Thread-8 获得了写锁。
Thread Thread-8 释放了写锁。
Thread Thread-9 获得了写锁。
Thread Thread-9 释放了写锁。
Thread Thread-0 获得了读锁。
Thread Thread-3 获得了读锁。
Thread Thread-5 获得了读锁。
Thread Thread-1 获得了读锁。
Thread Thread-9 获得了读锁。
Thread Thread-1 释放了读锁。
Thread Thread-8 获得了读锁。
Thread Thread-5 释放了读锁。
Thread Thread-7 获得了读锁。
Thread Thread-4 获得了读锁。
Thread Thread-6 获得了读锁。
Thread Thread-3 释放了读锁。
Thread Thread-0 释放了读锁。
Thread Thread-2 获得了读锁。
Thread Thread-6 释放了读锁。
Thread Thread-4 释放了读锁。
Thread Thread-7 释放了读锁。
Thread Thread-1 : 3.0
Thread Thread-0 : 3.0
Thread Thread-8 释放了读锁。
Thread Thread-9 释放了读锁。
Thread Thread-8 : 3.0
Thread Thread-5 : 3.0
Thread Thread-3 : 3.0
Thread Thread-4 : 3.0
Thread Thread-7 : 3.0
Thread Thread-6 : 3.0
Thread Thread-2 释放了读锁。
Thread Thread-9 : 3.0
Thread Thread-2 : 3.0 
		 */
	}
}