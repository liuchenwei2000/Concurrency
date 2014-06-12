/**
 * 
 */
package thread.adv;

/**
 * 线程的API应用
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2007-8-28
 */
public class ThreadAPITest {
	
	/**
	 * @param args
	 */
	public static void main(String args[]) {
		Thread dt = new Thread(new APITest());
		
		/** 
		 * 守护线程的唯一作用就是为其他线程提供服务，当只剩下守护线程时，JVM就会退出。
		 * 将线程设为守护线程，必须在线程启动前设置。
		 */
		dt.setDaemon(true);
		dt.start();
	}
}

class APITest implements Runnable {
	
	@Override
	public void run() {
		try {
			/** 
			 * 虽然现在已经没有强制终止线程的方法，但可以通过interrupt方法来请求终止一个线程 
			 * 当interrupt方法在一个线程上被调用时，该线程的中断状态将会被置位。
			 * 这是一个boolean标志，存在于每一个线程中，每个线程都应该不时检查这个标志，以判断线程是否应该被中断。
			 */
			Thread.currentThread().interrupt();
			
			/** 
			 * 为了查明中断状态是否被置位，需要调用线程的isInterrupted方法。
			 * 该方法没有副作用，线程的中断状态不受该方法的影响。
			 */
			if (Thread.currentThread().isInterrupted()) {
				System.out.println("Thread after isInterrupted()");
			}
			
			/** 
			 * 判断线程是否已被中断，该方法有副作用，线程的中断状态由该方法清除。
			 */
			if (Thread.interrupted()) {
				System.out.println("Thread after interrupted()");
			}
			if (Thread.interrupted()) {
				System.out.println("Thread after interrupted() again");
			} else {
				/** 
				 * isAlive()方法可用来判断线程在当前是否存活着（可运行或被阻塞）。
				 */
				if (Thread.currentThread().isAlive()) {
					System.out.println("Thread is alive!");
				}
				Thread.sleep(2000);
				System.out.println("Thread is dead!");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}