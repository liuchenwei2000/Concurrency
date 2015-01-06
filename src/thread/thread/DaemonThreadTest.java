/**
 * 
 */
package thread;

import java.util.concurrent.TimeUnit;

/**
 * �ػ��߳�ʾ��
 * <p>
 * �ػ��̵߳����ȼ��ǳ��ͣ�����ֻ���������̲߳�ִ�У��ȴ�������״̬��ʱ�Żᱻִ�С�
 * ��������ֻʣ���ػ��߳�ʱ��JVM��������ǲ��˳���
 * <p>
 * �ػ��߳�ͨ������Ϊ�����̵߳ķ����ṩ�ߣ�service provider�������һ�ʹ��һ������ѭ�����ȴ���������
 * �ػ��߳�Ӧ����Զ��ȥ������Դ�����ļ������ݿ�ȣ���Ϊ�������κ�ʱ�����жϡ�
 * ���͵��ػ��߳��м�ʱ����������������GC����
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��6��
 */
public class DaemonThreadTest {
	
	/**
	 * @param args
	 */
	public static void main(String args[]) {
		Thread gc = new GarbageCollector();
		
		// ���߳���Ϊ�ػ��̣߳��������߳�����ǰ���á�
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
	 * ģ������������
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