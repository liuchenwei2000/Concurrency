/**
 * 
 */
package threadlocal;

import java.util.concurrent.ThreadLocalRandom;

/**
 * ThreadLocalRandomʾ��
 * <p>
 * ThreadLocalRandom ��Java7��ʼ���ṩ�Ĺ����࣬�����ڲ�����������������������������ֲ߳̾�������
 * ÿ����������������߳̽������һ���Լ���������������Щ��������ThreadLocalRandom ��ͳһ��������Կͻ�����͸���ġ�
 * �ڶ��̲߳������ʵ�����£�ʹ��ThreadLocalRandom ��ʹ��Math.random()���Լ��ٲ����߳�֮��ľ������Ӷ���ø��õ����ܡ�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2014��12��30��
 */
public class ThreadLocalRandomTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 3;
		for (int i = 0; i < n; i++) {
			new Thread(new Task()).start();
		}
	}

	private static class Task implements Runnable {
		
		public Task() {
			// ��ʼ�����߳�ʹ�õ������������
			ThreadLocalRandom.current();
		}

		@Override
		public void run() {
			String name = Thread.currentThread().getName();
			for (int i = 0; i < 10; i++) {
				// ����10���ڵ������
				int randomInt = ThreadLocalRandom.current().nextInt(10);
				System.out.printf("%s: %d\n", name, randomInt);
			}
		}
	}
}
