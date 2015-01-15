/**
 * 
 */
package thread.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * ���� Executor ��ִ����ϵ�����ʾ��
 * <p>
 * FutureTask ���ṩ�� done()�������������� Executor �����е������������ִ��ĳЩ�ض����롣
 * ��������ִ��һЩ�����������������ͳ�Ʊ��桢ʹ��Email���ͽ�����ͷ�ĳЩ��Դ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��15��
 */
public class ControlTaskFinishingInExecutorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool();
		
		ResultTask[] resultTasks = new ResultTask[5];

		for (int i = 0; i < 5; i++) {
			ExecutableTask executableTask = new ExecutableTask("Task " + i);
			// ʹ�� ResultTask ���� ExecutableTask
			resultTasks[i] = new ResultTask(executableTask);
			executor.submit(resultTasks[i]);
		}
		
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// ǿ��ȡ������
		for (int i = 0; i < resultTasks.length; i++) {
			resultTasks[i].cancel(true);
		}
		// ��������ɵ���������ӡ����
		for (int i = 0; i < resultTasks.length; i++) {
			try {
				if (!resultTasks[i].isCancelled()) {
					System.out.printf("%s\n", resultTasks[i].get());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		executor.shutdown();
	}

	/**
	 * ��ͨ�� Callable ����
	 */
	private static class ExecutableTask implements Callable<String> {
		
		private String name;
		
		public ExecutableTask(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}

		@Override
		public String call() throws Exception {
			try {
				long duration = (long) (Math.random() * 10);
				System.out.printf("%s: Waiting %d seconds for results.\n", this.name, duration);
				TimeUnit.SECONDS.sleep(duration);
			} catch (InterruptedException e) {
			}
			return "Hello, world. I'm " + name;
		}
	}
	
	/**
	 * FutureTask ʵ��ʾ������������ ExecutableTask ����ʱ��Ϊ��
	 * <p>
	 * �� FutureTask ���Ƶ��������н�����done()�����ᱻ FutureTask �������ڲ��Զ����ã�׼ȷ��˵��
	 * ����������״̬�л��� isDone ֮�󱻵��ã������������������������Ǳ�ȡ����
	 */
	private static class ResultTask extends FutureTask<String> {

		private String name;

		public ResultTask(Callable<String> callable) {
			super(callable);
			this.name = ((ExecutableTask) callable).getName();
		}

		/**
		 * done() ������Ĭ��ʵ���ǿյģ�����ͨ�����Ǹ÷�����ʵ���ض���Ϊ��
		 */
		@Override
		protected void done() {
			if (isCancelled()) {
				System.out.printf("%s: Has been canceled\n", name);
			} else {
				System.out.printf("%s: Has finished\n", name);
			}
		}
	}
}
