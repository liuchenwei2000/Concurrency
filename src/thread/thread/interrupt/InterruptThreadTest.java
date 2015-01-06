/**
 * 
 */
package thread.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * �ж��߳�ʾ��
 * <p>
 * ��������Ҫȡ��ĳ�����񣨽�������ĳ���̣߳�ʱ������ʹ���жϻ��ơ�
 * �жϻ��ƿ�������ʾĳ���߳��ѱ�Ҫ����ֹ���У��û��ƶ��صĵط����ڣ�
 * �߳�����������Լ��Ƿ��ѱ��жϣ��������Ƿ���ж�������Ӧ���߳���ȫ���������ж��������ִ����ȥ����
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��6��
 */
public class InterruptThreadTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread thread = new Thread(new PrimeGenerator());
		thread.start();
		
		try {
			TimeUnit.MILLISECONDS.sleep(100);
			/*
			 * �̶߳������һ��boolean������������ʾ�Ƿ��ѱ��жϣ�
			 * ������ interrupt()����ʱ�Ὣ������ֵ��Ϊtrue��
			 * ���� isInterrupted()���������ظ�����ֵ��
			 */
			thread.interrupt();
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Main thread ends.");
	}

	private static class PrimeGenerator implements Runnable {

		private int number = 2;

		@Override
		public void run() {
			while (true) {
				if (isPrime(number)) {
					System.out.println(number);
				}
				// �̼߳���ж�״̬��������Ӧ
				if (Thread.currentThread().isInterrupted()) {
					System.out.println("Thread is interrupted.");
					/*
					 * Thread.interrupted()�������ж��߳��Ƿ��ѱ��жϣ��÷����и����ã��̵߳��ж�״̬�ɸ÷��������
					 * ��thread.isInterrupted()����û�и����ã�����ı��ж�״̬������Ƽ�ʹ������
					 */
					if (Thread.interrupted()) {
						System.out.println("Thread after interrupted()");
					}
					if (Thread.interrupted()) {
						System.out.println("Thread after interrupted() again");
					} else {
						/*
						 * isAlive()�����������ж��߳��ڵ�ǰ�Ƿ����ţ������л���������
						 */
						if (Thread.currentThread().isAlive()) {
							System.out.println("Thread is alive!");
						}
						System.out.println("Thread is dead!");
						return;
					}
				}
				number++;
			}
		}

		private boolean isPrime(int n) {
			int max = (int) Math.sqrt(n);
			for (int i = 2; i <= max; i++) {
				if (n % i == 0) {
					return false;
				}
			}
			return true;
		}
	}
}
