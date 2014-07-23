/**
 * 
 */
package thread;

/**
 * InterruptedException演示
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-6
 */
public class InterruptedExceptionTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("sleep..zzzzZ");
					// 休眠10s
					Thread.sleep(10000);
					System.out.println("wake up.");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		// 在main线程中启动t1线程
		t1.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/** 
		 * interrupt方法可以用来请求终止线程。
		 * <p>
		 * 当对一个线程调用interrupt方法时，线程的中断状态将被置位。这是每一个线程都具有的boolean标志。
		 * 每个线程都应该不时地检查这个标志（如下面的CanInterrupt），以判断线程是否被中断。
		 * 但如果一个线程被阻塞了，它就无法检查中断状态，此时会抛出InterruptedException。
		 * 当在一个被阻塞的线程上调用interrupt方法时，阻塞调用（例如sleep或wait）就会被InterruptedException所终止。
		 */
		t1.interrupt();
	}
	
	/**
	 * 没有任何语言方面的需求要求一个被中断的线程应该终止！ 
	 * 中断一个线程不过是引起它的注意，被中断的线程可以决定如何响应中断。
	 */
	static class CanInterrupt implements Runnable {

		@Override
		public void run() {
			int counter = 0;
			while ((!Thread.currentThread().isInterrupted()) && (counter < 100)) {
				System.out.println("still alive" + (counter--));
			}
		}
	}
}