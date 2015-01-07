/**
 * 
 */
package concurrency;

/**
 * ͬ������
 * <p>
 * <li>1��<p>
 * �����������߳�ͬʱ����ĳһ�������е�synchronized(this)ͬ�������ʱ��
 * ֻ����һ���̵߳õ�ִ�У���һ���̱߳���ȴ���ǰ�߳�ִ����˴��������ִ�С� 
 * <li>2��<p>
 * ��һ���̷߳��ʶ����һ��synchronized(this)ͬ�������ʱ��
 * ��һ���߳���Ȼ���Է��ʸö����еķ�synchronized(this)ͬ������顣
 * <li>3��<p>
 * ��һ���̷߳��ʶ����һ��synchronized(this)ͬ�������ʱ��
 * �����̶߳Դ˶�������������synchronized(this)ͬ�������ķ��ʽ���������  
 * <li>4��<p>
 * ���Ϲ����������������δ�طǵ���this�Ķ�������ͬ�����á�
 *
 * @author ����ΰ
 *
 * �������ڣ�2013-6-9
 */
public class SynBlockTest {

	/**
	 * ������
	 * 
	 * ����JDK5.0֮ǰ���÷������ĳ������Ҫ����һ������ʱ��������Ҫ�������������ϲ�������Ը��ԵĲ�����������ʱ����
	 * ���������ַ�ʽ������������󣬶�����ʹ�õ�ǰ���������
	 */ 
	private Object lock1 = new Object();
	
	private Object lock2 = new Object();
	
	/**
	 * synchronized����Ҳ��JDK5.0֮ǰ��д����5.0֮��Ҳ��ʹ��Lock/Condition���Ƽ�����
	 */
	public void method1() {
		// ʹ�õ�ǰ������
		synchronized (this) {

		}
		// ����synchronized���飬�ͻ��ͷ���

		// ʹ��lock1��
		synchronized (lock1) {

		}

		// ʹ��lock2��
		synchronized (lock2) {

		}
	}
}