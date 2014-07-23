/**
 * 
 */
package thread;

/**
 * �������߳������ַ�����
 * <li>ʵ��Runnable�ӿ�
 * <li>�̳�Thread�ࣨ���Ƽ���
 * <p>ע��</br>
 * ���������ַ���������ֱ�ӵ���run()����������run()�������ڵ�ǰ���߳���ִ�в����ᴴ���µ��̡߳�
 * Ӧ�õ���start()����������һ���µ��߳�ȥִ��run()������
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2007-8-28
 */
public class ThreadDemo {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ͨ��ʵ��Runnable�ӿڵķ�������һ�����߳�
		System.out.println("Thread implements Runnable:");
		Thread thread1 = new Thread(new RunnableImpl());
		thread1.start();
		
		// ͨ���̳�Thread��ķ�������һ�����߳�
		System.out.println("Thread extends Thread:");
		Thread thread2 = new ThreadExtended();
		thread2.start();
		
		try {
			thread1.join();// �������߳���ֹʱ�÷����ŷ���
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Main Thread Ends.");
	}
}

/**
 * ͨ��ʵ��Runnable�ӿڴ���һ���µ��߳�
 * <p>
 * ����ʵ��run()�����������Ե��������ķ��������������ࡢ������������run()����ʱ�����߳̽�����
 * �����µ��̺߳�����������ֱ������������start()�����������ϣ�start()����ִ�е���һ����run()�ĵ��á�
 */
class RunnableImpl implements Runnable {

	public void run() {
		try {
			for (int i = 5; i > 0; i--) {
				System.out.println("Child Thread(RunnableImpl):" + i);
				Thread.sleep(500);
			}
		} catch (Exception e) {
			System.out.println("Child Thread(RunnableImpl):" + e.getMessage());
		}
		System.out.println("Child Thread(RunnableImpl) Ends.");
	}
}

/**
 * ͨ���̳�Thread�ഴ��һ�����߳�
 * <p>
 * ��һ����̳�Thread�������������run()������
 * ���run()���������̵߳���ڣ�Ҳ�������start()�������������߳�ִ�С�
 */
class ThreadExtended extends Thread {

	public ThreadExtended() {
		super("Child Thread(ThreadExtended)");
	}

	@Override
	public void run() {
		try {
			for (int i = 5; i > 0; i--) {
				System.out.println("Child Thread(ThreadExtended):" + i);
				Thread.sleep(500);
			}
		} catch (Exception e) {
			System.out.println("Child Thread(ThreadExtended):" + e.getMessage());
		}
		System.out.println("Child Thread(ThreadExtended) Ends.");
	}
}