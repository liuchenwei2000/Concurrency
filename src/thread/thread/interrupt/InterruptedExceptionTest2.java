/**
 * 
 */
package thread.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * InterruptedException��ʾ
 * <p>
 * ���һ���̱߳������ˣ������޷�����ж�״̬����ʱ���׳�InterruptedException��
 * ����һ�����������߳��ϵ���interrupt����ʱ���������ã�����sleep��wait���ᱻInterruptedException�����жϡ�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-6
 */
public class InterruptedExceptionTest2 {

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
					TimeUnit.SECONDS.sleep(10);
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
		t1.interrupt();
	}
}