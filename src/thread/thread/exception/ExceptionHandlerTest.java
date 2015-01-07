/**
 * 
 */
package thread.exception;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * ExceptionHandler示例
 * <p>
 * 如果线程中抛出非受查异常而又没被catch处理，则程序会退出。
 * Java提供了一种机制可用来catch并处理非受查异常，避免上述情况的出现。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月7日
 */
public class ExceptionHandlerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread t = new Thread(new Task());
		/*
		 * 线程运行前，通过下面的方法为它设置默认异常处理器。
		 * 当线程中抛出未被catch的非受查异常时，JVM会检查该线程是否设置了UncaughtExceptionHandler，
		 * 如果有的话，JVM会调用该UncaughtExceptionHandler的相应方法并把Thread对象和Exception对象作为参数传入。
		 * 
		 * JVM会先寻找线程自己的异常处理器，没有的话再寻找线程所属线程组的异常处理器，最后寻找线程公共的异常处理器。
		 */
		t.setUncaughtExceptionHandler(new MyExceptionHandler());
		t.start();
		// 也可以通过下面的方式为所有的线程设置默认的公共异常处理器
//		Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler());
	}

	/**
	 * 实现 UncaughtExceptionHandler 接口的自定义异常处理器
	 */
	private static class MyExceptionHandler implements UncaughtExceptionHandler {

		/**
		 * 线程抛出未被catch的非受查异常时会被调用
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
	 * 模拟一个会抛出非受查异常的任务
	 */
	private static class Task implements Runnable {

		@Override
		public void run() {
			Integer.parseInt("a");
		}
	}
}
