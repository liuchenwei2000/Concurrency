/**
 * 
 */
package concurrency;

/**
 * volatileʾ��
 * <p>
 * volatile���ε����ڱ��̷߳���ʱ��ÿ�ζ���ǿ�ƴӹ����ڴ����ض��������µ�ֵ��
 * ���ң������ֵ�����仯ʱ��Ҳ��ǿ���߳̽��仯ֵ��д�������ڴ档
 * �������κ�ʱ�̣�������ͬ���߳����ǿ���ĳ�����ͬһ��ֵ��
 * <p>
 * volatileһ������²��ܴ���sychronized����Ϊvolatile���ܱ�֤������ԭ���ԡ�
 * ��������ֵ���������һ��ֵ����ʱ����n=n+1��n++�ȣ�volatile�ؼ��ֽ�ʧЧ��
 * ֻ�е�������ֵ��������һ��ֵ�޹�ʱ�Ըñ����Ĳ�������ԭ�Ӽ���ģ���n = m + 1
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2007-12-13
 */
public class VolatilePuzzler {

	public static int n = 0;

	static class Task implements Runnable {

		public void run() {
			try {
				for (int i = 0; i < 10; i++) {
					n++; // n=n+1;Ҳһ��
					Thread.sleep(3); // Ϊ��ʹ���н����������ӳ�3����
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread threads[] = new Thread[100];
		// ����100���߳�
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Task());
		}
		// ���иղŽ�����100���߳�
		for (int i = 0; i < threads.length; i++) { 
			threads[i].start();
		}
		// ȷ��100���̶߳�ִ����
		for (int i = 0; i < threads.length; i++) { 
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// n ������ֵӦ����1000����ʵ�ʽ��������
		System.out.println("n = " + VolatilePuzzler.n);
	}
}