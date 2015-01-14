/**
 * 
 */
package thread.executor;

import java.util.concurrent.Executor;

/**
 * Executorִ����ʾ��
 * <p>
 * Executor��ִ�����ύ Runnable����Ķ���
 * �˽ӿ��ṩһ�ֽ������ύ��ÿ������������еĻ���(�����߳�ʹ�õ�ϸ�ڡ����ȵ�)���뿪���ķ�����
 * <p>
 * ͨ��ʹ�� Executor��������ʽ�ش����̣߳�Executors���Ǵ���ִ�����Ĺ����࣬���ThreadPoolTest��
 * <p>
 * ����������<p>
 * ֱ�Ӵ��� Thread������Խ��һЩ���⡣
 * ��һЩJVM�У����� Thread��һ�������͵Ĳ������������� Thread�ȴ������߳�Ҫ���׵öࡣ
 * ������һЩ JVM�У���������෴��Thread�������͵ģ���������Ҫʱ�����׵��½�һ���̡߳�
 * JDK������Executor�ӿڣ����ǶԴ������̵߳�һ�ֳ��󣬿�����Ա����ֱ�Ӵ��� Thread��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-17
 */
public class ExecutorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ���ʹ��ִ��������Runnable
		Executor executor = new DirectExecutor();
		executor.execute(new Task1());// �������ж��Runnable
		executor.execute(new Task2());
		
		executor = new NewThreadExecutor();
		executor.execute(new Task1());
		executor.execute(new Task2());
	}
	
	/**
	 * ֱ��ִ����
	 * <p>
	 * Executor �ӿڲ�û���ϸ��Ҫ��ִ�����첽�ģ�ִ������ȫ�����ڵ����ߵ��߳����������ύ������ 
	 */
	private static class DirectExecutor implements Executor {
		
	     public void execute(Runnable r) {
	         r.run();
	     }
	 }
	
	/**
	 * ���߳�ִ����
	 * <p>
	 * ����һ���µ��߳�ȥִ��ÿ������
	 */
	private static class NewThreadExecutor implements Executor {
		
	     public void execute(Runnable r) {
	        new Thread(r).start();
	     }
	 }
	
	private static class Task1 implements Runnable {

		public void run() {
			try {
				Thread.sleep(1000);
				System.out.println("task1 complete.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class Task2 implements Runnable {

		public void run() {
			try {
				Thread.sleep(1000);
				System.out.println("task2 complete.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}