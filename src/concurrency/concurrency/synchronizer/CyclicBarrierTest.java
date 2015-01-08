/**
 * 
 */
package concurrency.synchronizer;

import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier示例
 * <p>
 * CyclicBarrier实现了一个称为栅栏的集合点。
 * <p>
 * CyclicBarrier 可以由一个指定数值初始化，来作为需要在集合点等待的线程总数。
 * 当其中一个线程到达集合点时，需要调用 CyclicBarrier 的await()方法来等待其他线程的到来，而CyclicBarrier 
 * 会将该线程阻塞直到所需数量的线程全部到来。当最后一个线程调用 CyclicBarrier 的await()方法后，它会唤醒所有等待的其他线程。
 * <p>
 * 另外 CyclicBarrier 可以使用一个额外的 Runnable 对象作为初始化参数，当所有线程到达集合点后，该 Runnable 
 * 对象将被放到某个线程中执行。这个特性使得 CyclicBarrier 非常适合使用分而治之（divide and conquer）技术的并行任务。
 * 比如在一次计算中，需要大量线程运行计算的不同部分。当所有部分都准备好时，结果需要被整合才可用。
 * 当一个线程完成了它那部分任务后，就让它运行到栅栏处。一旦所有线程都到达了这个栅栏，栅栏就会被撤销，所有线程就可以继续运行。
 * <p>
 * 因为CyclicBarrier在释放等待线程后可以重用，所以称它为循环的barrier。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-25
 */
public class CyclicBarrierTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Computer(5).doWork();
	}
}

/**
 * 计算机类，模拟将某任务分解成多任务进行运算
 */
class Computer {
	
	// 栅栏
	private CyclicBarrier barrier;
	
	public Computer(int subtask) {
		/*
		 * 创建一个CyclicBarrier，它将在给定数量(本例subtask)的参与者线程处于等待状态时启动。
		 * 另外还支持一个可选的Runnable命令(本例MainTask)，在一组线程中最后一个线程到达之后
		 * (但在释放所有线程之前)运行(该操作将由最后一个进入 barrier的线程执行)。
		 * 该Runnable命令只在每个屏障点运行一次，若想在释放所有参与线程之前 更新共享状态，此屏障操作很有用。
		 */
		 this.barrier = new CyclicBarrier(subtask, new MainTask());
	}
	
	public void doWork() {
		// getParties()返回要求启动此barrier的参与者数目
		int total = barrier.getParties();
		// 将任务分解成多个子任务，提高运算速度
		for (int i = 0; i < total; i++) {
			new Thread(new SubTask("task " + (i + 1))).start();
		}
	}

	/**
	 * 主任务
	 */
	class MainTask implements Runnable {

		@Override
		public void run() {
			System.out.println("各子任务执行完毕，开始执行主任务......");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("主任务执行完毕......");
		}
	}

	/**
	 * 子任务
	 */
	class SubTask implements Runnable {

		private String name;
		
		public SubTask(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			try {
				Thread.sleep((long) (Math.random() * 5000));
				// getNumberWaiting() 返回当前在屏障处等待的参与者数目
				System.out.println("共有 " + (barrier.getNumberWaiting() + 1) + " 个线程到达栅栏在等待");
				/*
				 * 本线程将会在barrier上等待，直到所有参与者线程都到达barrier(即全都调用了await方法)
				 * <p>
				 * 如果任意一个在栅栏处等待的线程离开了(比如超时或者被中断)，那么栅栏就被破坏了。
				 * 那样的话，所有其他线程调用await方法都将抛出一个BrokenBarrierException。
				 */
				int index = barrier.await();// 返回当前到达线程的索引。
				
				if (index == 0) {// 0表示最后一个到达
					System.out.println(name + " 是最后一个来的");
					// 重置 CyclicBarrier 以便继续使用
					barrier.reset();
				}
				// barrier.getParties() - 1 表示第一个到达
				if (index == barrier.getParties() - 1) {
					System.out.println(name + " 是第一个来的");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(name + " 子任务结束.");
		}
	}
}