/**
 * 
 */
package thread;

/**
 * InterruptedException��ʾ
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-6
 */
public class InterruptedExceptionTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("sleep..zzzzZ");
					// ����10s
					Thread.sleep(10000);
					System.out.println("wake up.");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		// ��main�߳�������t1�߳�
		t1.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/** 
		 * interrupt������������������ֹ�̡߳�
		 * <p>
		 * ����һ���̵߳���interrupt����ʱ���̵߳��ж�״̬������λ������ÿһ���̶߳����е�boolean��־��
		 * ÿ���̶߳�Ӧ�ò�ʱ�ؼ�������־���������CanInterrupt�������ж��߳��Ƿ��жϡ�
		 * �����һ���̱߳������ˣ������޷�����ж�״̬����ʱ���׳�InterruptedException��
		 * ����һ�����������߳��ϵ���interrupt����ʱ���������ã�����sleep��wait���ͻᱻInterruptedException����ֹ��
		 */
		t1.interrupt();
	}
	
	/**
	 * û���κ����Է��������Ҫ��һ�����жϵ��߳�Ӧ����ֹ�� 
	 * �ж�һ���̲߳�������������ע�⣬���жϵ��߳̿��Ծ��������Ӧ�жϡ�
	 */
	static class CanInterrupt implements Runnable {

		@Override
		public void run() {
			int counter = 0;
			while ((!Thread.currentThread().isInterrupted()) && (counter < 100)) {
				System.out.println("still alive" + (counter--));
			}
		}
	}
}