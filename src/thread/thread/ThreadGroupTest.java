/**
 * 
 */
package thread;

/**
 * 线程组
 * <p>
 * 某些程序只有很少的线程，若能将它们按功能进行归类将很有用。
 * 以网络浏览器为例，如果大量线程正试图从服务器上获取图片，此时用户点击stop按钮来中断当前界面载入
 * 那么应该有很方便的方法同时中断所有这些线程，线程组就是用来干这种事情的。
 * 
 * @author 刘晨伟
 *
 * 创建日期：2007-12-12
 */
public class ThreadGroupTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 线程组可以用来对一组线程执行统一操作
		ThreadGroup g = new ThreadGroup("ThreadGroup");

		Thread t1 = new Thread(g, new RunnableImpl(), "t1");
		t1.start();

		Thread t2 = new Thread(g, new RunnableImpl(), "t2");
		t2.start();

		// 返回线程组中处于运行状态的线程数
		System.out.println("now there are " + g.activeCount()
				+ " active threads in ThreadGroup:" + g.getName());

		// 中断线程组和它的所有子线程组
		g.interrupt();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("now there are " + g.activeCount()
				+ " active threads in ThreadGroup:" + g.getName());
	}
}