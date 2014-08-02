/**
 * 
 */
package thread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * �̳߳�ʾ��
 * <p>
 * ����һ�����̵߳Ĵ��ۻ��Ǻܸߵģ���Ϊ�漰�����ϵͳ�Ľ�����
 * ������򴴽����������ں̵ܶ��̣߳��Ǿ�Ӧ��ʹ���̳߳أ�Thread Pool����
 * <p>
 * һ���̳߳ذ�������׼�����еĿ����̣߳���һ��Runnable�����ύ���̳߳أ��̳߳��е�һ���߳̾ͻ����run������
 * ��run�����˳�ʱ���̲߳������������Ǽ����ڳ���׼��Ϊ��һ�������ṩ����
 * <p>
 * ����ʹ���̳߳ؿ��Լ��ٲ����̵߳������������������̻߳ή����������ʹ�����������
 * �������ᴴ������̣߳��Ǿ�Ӧ��ʹ��һ���߳����̶����̳߳������Ʋ����̵߳�������
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-17
 */
public class ThreadPoolTest {

	/**
	 * ʹ���̳߳ص�һ�㲽�裺
	 * 1��ʹ��Excutors�ഴ����Ӧ���̳߳ء�
	 * 2������submit�ύһ��Runnable��Callable����
	 * 3�����ϣ��ȡ����������ύ��һ��Callable�����Ǿͱ����submit���ص�Future����
	 * 4�����������ύ�κ�����ʱ����shutdown��
	 */
	public static void main(String[] args) throws Exception {
		/**
		 * ʹ�ù̶������̳߳�������
		 * <p>
		 * ���ڹ̶������̳߳أ�����ύ����������ڿ����߳�������ô�ò�����������񽫻�ȴ���
		 * ������������ɺ����˿����̣߳����Ǿ��������ˡ�
		 */
		ExecutorService pool = Executors.newFixedThreadPool(10);
		
		// ����ʹ���������ַ�����һ��Runnable����Callable�����ύ���̳߳أ�
		/**
		 * �ύһ��Runnable����
		 * <p>
		 * ����һ��Future�������ڲ鿴����ִ��״̬����get���������ʱ�᷵��null��
		 */
		Future<?> future1 = pool.submit(TaskFactory.createRunnable());
		System.out.println("future1=" + future1.get());
		
		/**
		 * �ύһ��Runnable�����ָ���������
		 * <p>
		 * ����һ��Future�������ڲ鿴����ִ��״̬��get���������ʱ�᷵�ز�������Ķ���
		 */
		Future<Object> future2 = pool.submit(TaskFactory.createRunnable(), new Object());
		System.out.println("future2=" + future2.get());
		
		/**
		 * �ύһ��Callable����
		 * <p>
		 * ����һ��Future�������ڲ鿴����ִ��״̬��get���������ʱ�᷵�ؼ�������
		 */
		Future<Integer> future3 = pool.submit(TaskFactory.createCallable());
		System.out.println("future3=" + future3.get());
		
		/**
		 * ������һ�����ӳغ�Ҫ��������ķ����ر����ӳء�
		 * ���رյ�ִ�������ٽ����µ����񣬵�����������ɺ󣬳��е��߳̾������ˡ�
		 */
		pool.shutdown();
		
		/**
		 * Ҳ���Ե�������ķ������ػ�ȡ�����л�û��ʼ��������ͼ�ж��������е��̡߳�
		 */
		pool.shutdownNow();
	}
}