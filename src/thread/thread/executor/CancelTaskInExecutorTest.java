/**
 * 
 */
package thread.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * ȡ�� Executor �е�����ʾ��
 * <p>
 * ����ʹ�� Future �����cancel()������ȡ��Executor�е�����
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��15��
 */
public class CancelTaskInExecutorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool();
		
		System.out.printf("Main: Executing the Task\n");
		// �ύ��������ִ��
		Future<String> result = executor.submit(new Task());
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("Main: Canceling the Task\n");
		/*
		 * ȡ������
		 * 
		 * 1����������Ѿ��������������ѱ�ȡ����������Ϊ����ԭ���ܱ�ȡ����������������false�������񲻻ᱻȡ����
		 * 2������������� Executor �еȴ������̣߳�����������δ����ǰ�ͱ�ȡ����
		 * ��������Ѿ��������У����Ƿ�ᱻȡ��ȡ���ڱ������Ĳ�����
		 * true������������������Ҳһ���ᱻȡ����
		 * false������������������򲻻ᱻȡ����
		 */
		result.cancel(true);
		
		System.out.printf("Main: Canceled: %s\n", result.isCancelled());// �Ƿ�ȡ��
		System.out.printf("Main: Done: %s\n", result.isDone());// �Ƿ��ѽ���

		executor.shutdown();
		System.out.printf("Main: The executor has finished\n");
	}

	private static class Task implements Callable<String> {

		@Override
		public String call() throws Exception {
			while (true) {
				System.out.printf("Task: Test\n");
				TimeUnit.MILLISECONDS.sleep(100);
			}
		}
	}
}
