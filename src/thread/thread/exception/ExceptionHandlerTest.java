/**
 * 
 */
package thread.exception;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * ExceptionHandlerʾ��
 * <p>
 * ����߳����׳����ܲ��쳣����û��catch�����������˳���
 * Java�ṩ��һ�ֻ��ƿ�����catch��������ܲ��쳣��������������ĳ��֡�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��7��
 */
public class ExceptionHandlerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread t = new Thread(new Task());
		/*
		 * �߳�����ǰ��ͨ������ķ���Ϊ������Ĭ���쳣��������
		 * ���߳����׳�δ��catch�ķ��ܲ��쳣ʱ��JVM������߳��Ƿ�������UncaughtExceptionHandler��
		 * ����еĻ���JVM����ø�UncaughtExceptionHandler����Ӧ��������Thread�����Exception������Ϊ�������롣
		 * 
		 * JVM����Ѱ���߳��Լ����쳣��������û�еĻ���Ѱ���߳������߳�����쳣�����������Ѱ���̹߳������쳣��������
		 */
		t.setUncaughtExceptionHandler(new MyExceptionHandler());
		t.start();
		// Ҳ����ͨ������ķ�ʽΪ���е��߳�����Ĭ�ϵĹ����쳣������
//		Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler());
	}

	/**
	 * ʵ�� UncaughtExceptionHandler �ӿڵ��Զ����쳣������
	 */
	private static class MyExceptionHandler implements UncaughtExceptionHandler {

		/**
		 * �߳��׳�δ��catch�ķ��ܲ��쳣ʱ�ᱻ����
		 * 
		 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
		 */
		@Override
		public void uncaughtException(Thread t, Throwable e) {
			System.out.printf("An exception has been captured\n");
			System.out.printf("Thread: %s\n", t.getId());
			System.out.printf("Exception: %s: %s\n", e.getClass().getName(), e.getMessage());
			System.out.printf("Stack Trace: \n");
			e.printStackTrace(System.out);
			System.out.printf("Thread status: %s\n", t.getState());
		}
	}
	
	/**
	 * ģ��һ�����׳����ܲ��쳣������
	 */
	private static class Task implements Runnable {

		@Override
		public void run() {
			Integer.parseInt("a");
		}
	}
}
