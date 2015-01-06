/**
 * 
 */
package thread;

import java.util.concurrent.TimeUnit;

/**
 * 守护线程示例
 * <p>
 * 守护线程的优先级非常低，并且只有在其他线程不执行（等待或阻塞状态）时才会被执行。
 * 当程序中只剩下守护线程时，JVM会结束它们并退出。
 * <p>
 * 守护线程通常会作为正常线程的服务提供者（service provider），并且会使用一个无线循环来等待服务请求。
 * 守护线程应该永远不去访问资源，如文件、数据库等，因为它会在任何时候发生中断。
 * 典型的守护线程有计时器、垃圾回收器（GC）。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月6日
 */
public class DaemonThreadTest {
	
	/**
	 * @param args
	 */
	public static void main(String args[]) {
		Thread gc = new GarbageCollector();
		
		// 将线程设为守护线程，必须在线程启动前设置。
		gc.setDaemon(true);
		gc.start();
		
		Thread t1 = new Thread(new Task());
		t1.start();
		
		System.out.println("Main thread ends.");
	}
	
	private static class Task implements Runnable {

		@Override
		public void run() {
			for (int i = 0; i < 5; i++) {
				System.out.println("do something...");
				try {
					TimeUnit.MILLISECONDS.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("task finished...");
		}
	}

	/**
	 * 模拟垃圾回收器
	 */
	private static class GarbageCollector extends Thread {

		@Override
		public void run() {
			while (true) {
				clean();
			}
		}

		private void clean() {
			System.out.println("clean...");
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}