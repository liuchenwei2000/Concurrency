/**
 * 
 */
package concurrency;

/**
 * synchronized�ؼ���ʾ��
 * <p>
 * ��Ҫע����ǣ�ÿһ�����������Լ�����������������߳���ͼ����ͬһ���������ͻᴮ�еķ����ڷ��ʡ�
 * ���ǣ���������̷߳��ʲ�ͬ�Ķ�����ôÿһ���̶߳���õ�һ����ͬ���������߶����ᷢ��������
 * �ؼ��Ǹ����synchronized���������ĸ�����
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2007-12-13
 */
public class SynchronizedTest {

	/**
	 * ��synchronized�����������η�ʱ��ʾ������ͬ���ġ�
	 * ���������ǵ������ͬ�������Ķ������������Կɵ������������
	 */
	public synchronized void method1() {
		// ...
	}
	
	/**
	 * method1()�൱����������ͬ����
	 */
	public void method11() {
		synchronized (this) {
			// ...
		}
	}

	/**
	 * synchronized���ӵ���ʽ���ǿ�����ġ�
	 * ���һ���߳��Ѿ�����������������ٴλ������ͬʱ���������ĳ��м�����
	 * ���������ͬ�������е�������һ��ͬ��������������ֱ�ӽ��������Ҫ�ȴ����ͷš�
	 */
	public synchronized void method111() {
		method1();
	}
	
	/**
	 * ����������������ľ���obj�������˭�õ������˭�Ϳ��������������Ƶ��Ƕδ��� ��
	 * ����һ����ȷ�Ķ�����Ϊ��ʱ���Ϳ�������д����
	 */
	public void method2(Object obj) {
		synchronized (obj) {
			// ...
		}
	}
	
	/**
	 * ��û����ȷ�Ķ�����Ϊ����ֻ������һ�δ���ͬ��ʱ�����Դ���һ�������instance����(������һ������)���䵱����
	 */
	// �����ʵ������(�������Ϳ�������)
	private byte[] lock = new byte[0];
	
	public void method3() {
		synchronized (lock) {
			// ...
		}
	}
	
	/**
	 * ��ֹ����߳�ͬʱ����������еľ�̬��������ס����class������������������ʵ���������á�
	 */
	public synchronized static void method4() {
		// ...
	}

	/**
	 * ��synchronized static����������Ч����һ����
	 */
	public static void method44() {
		// ��ס���� class����
		synchronized (SynchronizedTest.class) {
			// ...
		}
	}
}