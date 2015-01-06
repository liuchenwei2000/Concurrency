/**
 * 
 */
package thread;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * join()����ʾ��
 * <p>
 * ĳЩ�����£���Ҫ�ȴ��߳�����������ܼ�������ִ�У������ʼ��ĳЩ��Դ����ܽ����Ժ�Ĳ�����
 * ��ʱ����ʹ��thread.join()������ʹ���ø÷������̵߳ȴ�Ŀ���߳�thread��ɣ�Ȼ��������ִ�С�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��6��
 */
public class JoinTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread t1 = new Thread(new DataSourceLoader());
		t1.start();

		try {
			t1.join();// ���̻߳�ȴ� t1 �߳�ִ����Ż��������ִ��
			// �趨һ����ʱʱ�ޣ�������ʱ�޾Ͳ����ˣ���ʱ���� t1 �߳�ִ����������߳���������ִ��
//			t1.join(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.printf("Main: Configuration has been loaded: %s\n", new Date());
	}

	/**
	 * ģ���ʱ����Դ��ʼ��
	 */
	private static class DataSourceLoader implements Runnable {

		@Override
		public void run() {
			System.out.printf("Beginning data sources loading: %s\n", new Date());
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.printf("Data sources loading has finished: %s\n", new Date());
		}
	}
}
