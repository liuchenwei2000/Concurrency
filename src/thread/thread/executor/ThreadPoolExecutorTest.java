/**
 * 
 */
package thread.executor;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolExecutor ʾ��
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
public class ThreadPoolExecutorTest {

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
		 * ���ڹ̶������̳߳أ�����ύ�����������ڿ����߳�������ô�ò�����������񽫻�ȴ���
		 * ������������ɺ����˿����̣߳����Ǿ��������ˡ�
		 */
		ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
		
		/**
		 * �ύһ��Runnable����
		 * <p>
		 * ����һ��Future�������ڲ鿴����ִ��״̬����get���������ʱ�᷵��null��
		 */
		pool.execute(TaskFactory.createRunnable());
		
		// ͨ������ķ������� �̳߳ص���Ϣ
		System.out.printf("Pool Size��%d\n", pool.getPoolSize());// �����������߳���
		System.out.printf("Active Count��%d\n", pool.getActiveCount());// ������������������߳���
		System.out.printf("Completed Task Count��%d\n", pool.getCompletedTaskCount());// ����ɵ�������
		
		/**
		 * ������һ�����ӳغ�Ҫ��������ķ����ر����ӳء�
		 * ���رյ�ִ�������ٽ����µ����񣬵�����������ɺ󣬳��е��߳̾������ˡ�
		 */
		pool.shutdown();
		
		/**
		 * Ҳ���Ե�������ķ������ػ�ȡ�����л�û��ʼ��������ͼ�ж��������е��̡߳�
		 * ����ֵ����δ���е������б�
		 */
		List<Runnable> unRunTask = pool.shutdownNow();
		System.out.println(unRunTask.size());
		
		// ���ѵ��� shutdown()�� shutdownNow()�򷵻� true
		System.out.println(pool.isTerminated());
		// ���ѵ��� shutdown()�򷵻� true
		System.out.println(pool.isShutdown());
		
		// ���������߳�ֱ����ʱ������������������
		pool.awaitTermination(1, TimeUnit.DAYS);
	}
}