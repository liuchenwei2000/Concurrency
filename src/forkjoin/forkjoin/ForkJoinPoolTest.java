/**
 * 
 */
package forkjoin;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * ForkJoinPoolʾ��
 * <p>
 * ������Ҫ����ForkJoinPool������ExecutorService�Ĳ�֮ͬ����
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2014��12��25��
 */
public class ForkJoinPoolTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ͨ��ָ������������ForkJoinPoolʵ��
		ForkJoinPool pool = new ForkJoinPool(5);
		// ִ��ForkJoinTask����ʹ�ù�����ȡ�㷨
		pool.execute(new MyForkJoinTask());
		// ִ��Runnable������ʹ�ù�����ȡ�㷨������������ExecutorService���ơ�
		pool.execute(new MyRunnableTask());
		
		// �� execute(ForkJoinTask)��ͬ���ǣ�invoke(ForkJoinTask)��ͬ�����ã�ֱ��ForkJoinTask���н���invoke�����Ż᷵�ء�
		pool.invoke(new MyForkJoinTask());
		
		try {
			// ��ΪForkJoinPoolʵ����ExecutorService�ӿڣ�����Ҳʵ�����������������������������Ҳ����ʹ�ù�����ȡ�㷨��
			pool.invokeAll(Arrays.asList(new MyCallableTask()));
			pool.invokeAny(Arrays.asList(new MyCallableTask()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ����ʹ��ForkJoinTask.adapt��������ͳ��Runnable��Callable���������ForkJoinTask
		pool.execute(ForkJoinTask.adapt(new MyRunnableTask()));
		pool.execute(ForkJoinTask.adapt(new MyCallableTask()));
	}
	
	private static class MyForkJoinTask extends RecursiveAction {

		private static final long serialVersionUID = 1L;

		@Override
		protected void compute() {
		}
	}
	
	private static class MyRunnableTask implements Runnable {

		@Override
		public void run() {
		}
	}
	
	private static class MyCallableTask implements Callable<Object> {

		@Override
		public Object call() throws Exception {
			return null;
		}
	}
}
