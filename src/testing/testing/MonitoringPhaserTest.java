/**
 * 
 */
package testing;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * 监控 Phaser 对象示例
 * <p>
 * 演示 Phaser 对象所能提供的状态信息以及如何获取它们。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月26日
 */
public class MonitoringPhaserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Phaser phaser = new Phaser(3);
		
		for (int i = 0; i < phaser.getRegisteredParties(); i++) {
			new Thread(new Task(i + 1, phaser)).start();
		}
		
		for (int i = 0; i < 10; i++) {
			System.out.printf("********************\n");
			// 返回 Phaser 对象的当前阶段数
			System.out.printf("Main: Phaser: Phase: %d\n", phaser.getPhase());
			// 返回注册到 Phaser 对象中的任务数
			System.out.printf("Main: Phaser: Registered Parties: %d\n", phaser.getRegisteredParties());
			// 返回已结束当前阶段（等待下一阶段开始）的任务数
			System.out.printf("Main: Phaser: Arrived Parties: %d\n", phaser.getArrivedParties());
			// 返回尚未结束当前阶段的任务数
			System.out.printf("Main: Phaser: Unarrived Parties: %d\n", phaser.getUnarrivedParties());
			System.out.printf("********************\n");
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 一个使用 Phaser 对象控制阶段同步的任务
	 */
	private static class Task implements Runnable {

		private int time;
		private Phaser phaser;
		
		public Task(int time, Phaser phaser) {
			this.time = time;
			this.phaser = phaser;
		}

		/**
		 * 每个阶段睡眠指定time，共三次
		 */
		@Override
		public void run() {
			phaser.arrive();
			// phase 1
			System.out.printf("%s: Entering phase 1.\n", Thread.currentThread().getName());
			sleep();
			System.out.printf("%s: Finishing phase 1.\n", Thread.currentThread().getName());
			phaser.arriveAndAwaitAdvance();
			// phase 2
			System.out.printf("%s: Entering phase 2.\n", Thread.currentThread().getName());
			sleep();
			System.out.printf("%s: Finishing phase 2.\n", Thread.currentThread().getName());
			phaser.arriveAndAwaitAdvance();
			// phase 3
			System.out.printf("%s: Entering phase 3.\n", Thread.currentThread().getName());
			sleep();
			System.out.printf("%s: Finishing phase 3.\n", Thread.currentThread().getName());
			phaser.arriveAndDeregister();// 最后一次在 Phaser 中注销
		}
		
		private void sleep(){
			try {
				TimeUnit.SECONDS.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
