/**
 * 
 */
package thread.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * ScheduledExecutorServiceʾ��
 * <p>
 * ScheduledExecutorService�ӿھ���ΪԤ�����ظ�ִ���������Ƶķ�����
 * ����һ�������̻߳��Ƶ�java.util.Timer�ķ�����
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-17
 */
public class ScheduledExecutorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// ΪԤ��ִ�ж������ĵ��̳߳�
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.shutdownNow();
		// ΪԤ��ִ�ж������Ĺ̶��̳߳�
		service = Executors.newScheduledThreadPool(10);
		
		// ������ִ���ڸ����ӳٺ����õ�һ���Բ���Runnable(������1000����)
		service.schedule(TaskFactory.createRunnable(), 1000, TimeUnit.MILLISECONDS);
		// ������ִ���ڸ����ӳٺ����õ�һ���Բ���Callable(������1000����)
		ScheduledFuture<Integer> result = service.schedule(TaskFactory.createCallable(), 1000, TimeUnit.MILLISECONDS);
		System.out.println(result.get());
		/**
		 * ������ִ��һ���ڸ�����ʼ�ӳٺ��״����õ������Բ����������������и��������ڡ�
		 * Ҳ���ǽ���initialDelay(����1000ms)��ʼִ�У�Ȼ����initialDelay+period(������2000ms)��ִ�У�
		 * ������initialDelay + 2*period ��ִ�У��������ơ�
		 */
		service.scheduleAtFixedRate(TaskFactory.createRunnable(), 1000, 2000, TimeUnit.MILLISECONDS);
		
		/**
		 * ������ִ��һ���ڸ�����ʼ�ӳ�(����1000ms)���״����õĶ��ڲ�����
		 * �����ÿһ��ִ����ֹ����һ��ִ�п�ʼ֮�䶼���ڸ������ӳ�(����2000ms)��
		 */
		service.scheduleWithFixedDelay(TaskFactory.createRunnable(), 1000, 2000, TimeUnit.MILLISECONDS);
		
		Thread.sleep(10000);
		service.shutdownNow();
	}
}