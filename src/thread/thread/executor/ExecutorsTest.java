/**
 * 
 */
package thread.executor;

import java.util.concurrent.Executors;

/**
 * Executors��ʾ��
 * <p>
 * Executors����Executor��ExecutorService��ScheduledExecutorService��ThreadFactory�� Callable��Ĺ�����
 * �ṩ��һϵ��ʵ�õķ��������紴���̳߳ض���
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-17
 */
public class ExecutorsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ����Ҫʱ�������̣߳������̻߳ᱻ����60����̳߳�
		Executors.newCachedThreadPool();
		// ���а����̶��������̣߳������̻߳ᱻһֱ�������̳߳�
		// �������������߳����඼Ҳ�����ٴ������̣߳������ȴ�ֱ�����߳̿��������Ż�ִ�С�
		Executors.newFixedThreadPool(10);
		// ֻ��һ���̵߳��̳߳أ�����̻߳ᰴ˳��ִ��ÿһ���ύ����������
		Executors.newSingleThreadExecutor();
		// ΪԤ��ִ�ж������Ĺ̶��̳߳�
		Executors.newScheduledThreadPool(10);
		// ΪԤ��ִ�ж������ĵ��̳߳�
		Executors.newSingleThreadScheduledExecutor();
	}
}