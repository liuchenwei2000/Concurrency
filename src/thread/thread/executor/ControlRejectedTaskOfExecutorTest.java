/**
 * 
 */
package thread.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ���� Executor �ܾ����յ�����ʾ��
 * <p>
 * �� Executor ������shutdown()���������Ͳ��ٽ����µ�����
 * ����ͨ��Ϊ���趨 RejectedExecutionHandler �������ܾ�������
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��15��
 */
public class ControlRejectedTaskOfExecutorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RejectedTaskController controller = new RejectedTaskController();
		
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		// Ϊ executor ���� RejectedExecutionHandler
		executor.setRejectedExecutionHandler(controller);

		System.out.printf("Main: Starting.\n");
		for (int i = 0; i < 3; i++) {
			Task task = new Task("Task" + i);
			executor.submit(task);
		}

		System.out.printf("Main: Shutting down the Executor.\n");
		executor.shutdown();

		System.out.printf("Main: Sending another Task.\n");
		Task task = new Task("RejectedTask");
		/*
		 * �� Executor ���� shutdown()������ύ���񣬸�����ᱻ�ܾ���
		 * 
		 * ��� Executor �� RejectedExecutionHandler�������񱻾ܾ�ʱ�����rejectedExecution()������
		 * ���û������׳� RejectedExecutionExeption��
		 */
		executor.submit(task);

		System.out.println("Main: End");
	}

	/**
	 * ���ܾ������������
	 */
	private static class RejectedTaskController implements RejectedExecutionHandler {

		/**
		 * ��ĳ������ Executor �ܾ�ʱ�÷����ͻᱻ����
		 * 
		 * @param r
		 *            ���ܾ�������
		 * @param executor
		 *            �ܾ��˸������ִ����
		 */
		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			System.out.printf(
					"RejectedTaskController: The task %s has been rejected\n",
					r.toString());
			System.out.printf("RejectedTaskController: %s\n",
					executor.toString());
			System.out.printf("RejectedTaskController: Terminating: %s\n",
					executor.isTerminating());
			System.out.printf("RejectedTaksController: Terminated: %s\n",
					executor.isTerminated());
		}
	}
	
	/**
	 * ��ͨ������
	 */
	private static class Task implements Runnable {
		
		private String name;

		public Task(String name) {
			this.name = name;
		}

		public void run() {
			System.out.println("Task " + name + ": Starting");
			try {
				long duration = (long) (Math.random() * 10);
				System.out
						.printf("Task %s: ReportGenerator: Generating a report during %d seconds\n",
								name, duration);
				TimeUnit.SECONDS.sleep(duration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.printf("Task %s: Ending\n", name);
		}
	}
}
