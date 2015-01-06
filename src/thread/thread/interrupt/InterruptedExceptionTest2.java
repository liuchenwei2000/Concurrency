/**
 * 
 */
package thread.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * InterruptedException演示
 * <p>
 * 如果一个线程被阻塞了，它就无法检查中断状态，此时会抛出InterruptedException。
 * 当在一个被阻塞的线程上调用interrupt方法时，阻塞调用（例如sleep或wait）会被InterruptedException立即中断。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-6
 */
public class InterruptedExceptionTest2 {

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
					TimeUnit.SECONDS.sleep(10);
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
		t1.interrupt();
	}
}