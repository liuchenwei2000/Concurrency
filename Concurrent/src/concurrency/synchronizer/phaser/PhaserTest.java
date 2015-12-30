/**
 * 
 */
package concurrency.synchronizer.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * Phaser示例
 * <p>
 * 当一些并发任务可以分解成几个步骤（step）时，Phaser 类会很有用。
 * Phaser 提供了这样一种机制：它可以让执行并发任务的线程在每个步骤（step）的结束点保持同步，
 * 也就是说直到所有的线程都执行完上一步（step）操作后线程才会开始执行下一步操作。
 * <p>
 * 可以在创建 Phaser 对象时指定参与同步操作的线程数，但也可以动态的增加或减少这个数目。
 * 这个数也表示 Phaser对象在切换到下阶段操作前各个参与线程需要调用arriveAndAwaitAdvance()方法的次数，
 * 调用次数一旦达到， Phaser将唤醒所有正在休眠等待的线程，进入下一阶段操作的执行。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月9日
 */
public class PhaserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 5个参与者
		Phaser phaser = new Phaser(5);
		// 获取 Phaser 中注册的线程数
		Thread[] threads = new Thread[phaser.getRegisteredParties()];
		
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new StepByStepTask("Task_" + i, phaser));
			threads[i].start();
		}
		
		try {
			for (Thread thread : threads) {
				thread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/* 
		 * 当 Phaser 中没有参与者线程时，它就进入Termination状态，下面的方法会返回true。
		 * 在这个状态下，对 Phaser 对象的任何同步方法（如 awaitAdvance()、arriveAndAwaitAdvance()）
		 * 调用都会立即返回（返回值是一个负数，非Termination状态则会返回一个正数）而不做任何同步操作。
		 */
		System.out.println("Terminated: "+ phaser.isTerminated());
		// 强制 Phaser对象（独立于各参与者）进入 Termination状态，这在某个参与者出现错误会有用
//		phaser.forceTermination();
	}

	/**
	 * 多步骤的任务
	 */
	private static class StepByStepTask implements Runnable {

		private String name;
		private Phaser phaser;
		
		public StepByStepTask(String name, Phaser phaser) {
			this.name = name;
			this.phaser = phaser;
		}

		@Override
		public void run() {
			/*
			 * 当某个线程调用arriveAndAwaitAdvance()方法时，Phaser 对象会将此阶段参与线程的总数减一，
			 * 然后该线程将休眠直到所有其他参与线程完成了这阶段工作。
			 */
			// 这里表示所有的线程会同时开始执行
			phaser.arriveAndAwaitAdvance();
			step1();
			// 这里表示所有的线程完成step1()之后，才会继续执行下面的操作
			phaser.arriveAndAwaitAdvance();
			boolean result = step2();
			if (result) {
				System.out.println(name + "：no result after step " + phaser.getPhase());
				/*
				 * 当某个线程调用arriveAndDeregister()方法时，Phaser 对象会将此阶段参与线程的总数减一，
				 * 这表示线程已经完成了该阶段操作，但是不必参与剩下阶段的操作，所以 Phaser 对象在继续向下执行前也不会再等待该线程。
				 */
				// 简单地说，相当于在 Phaser 中注销了自己，动态减少了 Phaser 中的线程数。
				phaser.arriveAndDeregister();
				System.out.println(name + "：I have gone. " + phaser.getRegisteredParties() + " threads left.");
				return;
			} 
			phaser.arriveAndAwaitAdvance();
			step3();
			phaser.arriveAndDeregister();
			System.out.println(name + "：Work completed.");
		}

		private void step1() {
			try {
				TimeUnit.MILLISECONDS.sleep((long) (Math.random()*1000));
				System.out.println(name + "：step1 complete.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		private boolean step2() {
			long ms = (long) (Math.random()*1000);
			try {
				TimeUnit.MILLISECONDS.sleep(ms);
				System.out.println(name + "：step2 complete.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return ms > 500;
		}

		private void step3() {
			try {
				TimeUnit.MILLISECONDS.sleep((long) (Math.random()*1000));
				System.out.println(name + "：step3 complete.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
