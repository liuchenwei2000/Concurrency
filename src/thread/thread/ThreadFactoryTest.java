/**
 * 
 */
package thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * ThreadFactoryʾ��
 * <p>
 * ����ģʽ���߳��е�ʹ�ã�JDK�еĺܶ�API����ʹ�õ�ThreadFactory�����̶߳�����new��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��7��
 */
public class ThreadFactoryTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyThreadFactory tf = new MyThreadFactory("my");
		
		for (int i = 0; i < 5; i++) {
			tf.newThread(new Task()).start();
		}
		
		System.out.println(tf);
	}

	/**
	 * �Զ���ThreadFactory
	 */
	private static class MyThreadFactory implements ThreadFactory {

		private String basicName;// ��������
		private int counter;// ������
		private List<String> stats;// ͳ����Ϣ
	
		public MyThreadFactory(String basicName) {
			super();
			this.basicName = basicName;
			this.counter=0;
			this.stats = new ArrayList<String>();
		}

		/**
		 * ����ʵ�ֵķ������÷������Կ��ƴ����̵߳Ĺ��̣���������һЩͳ����Ϣ�������߳������ȵȡ�
		 * 
		 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
		 */
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, basicName + "-Thread_" + counter);
			counter++;
			stats.add(String.format("\nCreated thread %d with name %s on %s",
					t.getId(), t.getName(), new Date()));
			return t;
		}

		@Override
		public String toString() {
			return "MyThreadFactory ��stats=" + stats + "\n��";
		}
	}
	
	private static class Task implements Runnable {

		public void run() {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
