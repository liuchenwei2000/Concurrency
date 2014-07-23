/**
 * 
 */
package thread;

/**
 * 创建新线程有两种方法：
 * <li>实现Runnable接口
 * <li>继承Thread类（不推荐）
 * <p>注：</br>
 * 不论是那种方法都不能直接调用run()方法，否则run()方法会在当前的线程中执行并不会创建新的线程。
 * 应该调用start()方法来启动一个新的线程去执行run()方法。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2007-8-28
 */
public class ThreadDemo {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 通过实现Runnable接口的方法创建一个新线程
		System.out.println("Thread implements Runnable:");
		Thread thread1 = new Thread(new RunnableImpl());
		thread1.start();
		
		// 通过继承Thread类的方法创建一个新线程
		System.out.println("Thread extends Thread:");
		Thread thread2 = new ThreadExtended();
		thread2.start();
		
		try {
			thread1.join();// 当调用线程终止时该方法才返回
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Main Thread Ends.");
	}
}

/**
 * 通过实现Runnable接口创建一个新的线程
 * <p>
 * 必须实现run()方法，它可以调用其他的方法、引用其他类、声明变量，当run()返回时，该线程结束。
 * 建立新的线程后，它并不运行直到调用了它的start()方法，本质上，start()方法执行的是一个对run()的调用。
 */
class RunnableImpl implements Runnable {

	public void run() {
		try {
			for (int i = 5; i > 0; i--) {
				System.out.println("Child Thread(RunnableImpl):" + i);
				Thread.sleep(500);
			}
		} catch (Exception e) {
			System.out.println("Child Thread(RunnableImpl):" + e.getMessage());
		}
		System.out.println("Child Thread(RunnableImpl) Ends.");
	}
}

/**
 * 通过继承Thread类创建一个新线程
 * <p>
 * 当一个类继承Thread类后，它必须重载run()方法。
 * 这个run()方法是新线程的入口，也必须调用start()方法来启动新线程执行。
 */
class ThreadExtended extends Thread {

	public ThreadExtended() {
		super("Child Thread(ThreadExtended)");
	}

	@Override
	public void run() {
		try {
			for (int i = 5; i > 0; i--) {
				System.out.println("Child Thread(ThreadExtended):" + i);
				Thread.sleep(500);
			}
		} catch (Exception e) {
			System.out.println("Child Thread(ThreadExtended):" + e.getMessage());
		}
		System.out.println("Child Thread(ThreadExtended) Ends.");
	}
}