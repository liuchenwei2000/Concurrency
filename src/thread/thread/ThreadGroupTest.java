/**
 * 
 */
package thread;

/**
 * �߳���
 * <p>
 * ĳЩ����ֻ�к��ٵ��̣߳����ܽ����ǰ����ܽ��й��ཫ�����á�
 * �����������Ϊ������������߳�����ͼ�ӷ������ϻ�ȡͼƬ����ʱ�û����stop��ť���жϵ�ǰ��������
 * ��ôӦ���кܷ���ķ���ͬʱ�ж�������Щ�̣߳��߳��������������������ġ�
 * 
 * @author ����ΰ
 *
 * �������ڣ�2007-12-12
 */
public class ThreadGroupTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// �߳������������һ���߳�ִ��ͳһ����
		ThreadGroup g = new ThreadGroup("ThreadGroup");

		Thread t1 = new Thread(g, new RunnableImpl(), "t1");
		t1.start();

		Thread t2 = new Thread(g, new RunnableImpl(), "t2");
		t2.start();

		// �����߳����д�������״̬���߳���
		System.out.println("now there are " + g.activeCount()
				+ " active threads in ThreadGroup:" + g.getName());

		// �ж��߳���������������߳���
		g.interrupt();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("now there are " + g.activeCount()
				+ " active threads in ThreadGroup:" + g.getName());
	}
}