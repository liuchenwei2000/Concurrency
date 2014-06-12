/**
 * 
 */
package thread.adv;

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
		 * 如果一个线程被阻塞了，它就无法检查中断状态，此时会抛出InterruptedException。
		 * 当在一个被阻塞的线程上调用interrupt方法时，阻塞调用（例如sleep或wait）就会被InterruptedException所终止。
		 */
		t1.interrupt();
	}
}