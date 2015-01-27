/**
 * 
 */
package testing;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * ���ForkJoinPoolʾ��
 * <p>
 * ��ʾ ForkJoinPool ���������ṩ����Ϣ�Լ���λ�ȡ���ǡ�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��27��
 */
public class MonitoringForkJoinPoolTest {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		ForkJoinPool pool = new ForkJoinPool();
		
		int array[] = new int[1000];
		
		Task task = new Task(array, 0, array.length);
		pool.execute(task);

		// �����������ڼ䣬ÿ��1���ӡһ�� ForkJoinPool ��״̬��Ϣ
		while (!task.isDone()) {
			showLog(pool);
			TimeUnit.SECONDS.sleep(1);
		}

		pool.shutdown();
		pool.awaitTermination(1, TimeUnit.DAYS);

		showLog(pool);
		System.out.printf("Main: End of the program.\n");
	}
	
	private static void showLog(ForkJoinPool pool) {
		System.out.printf("**********************\n");
		System.out.printf("Main: Fork/Join Pool log\n");
		System.out.printf("Main: Fork/Join Pool: Parallelism: %d\n",
				pool.getParallelism());// ���سصĲ��м��Σ�parallelism level��
		System.out.printf("Main: Fork/Join Pool: Pool Size: %d\n",
				pool.getPoolSize());// ���س��еĹ����߳���
		System.out.printf("Main: Fork/Join Pool: Active Thread Count: %d\n",
				pool.getActiveThreadCount());// ��������ִ������Ĺ����߳���
		System.out.printf("Main: Fork/Join Pool: Running Thread Count: %d\n",
				pool.getRunningThreadCount());// ����û�б��κ�ͬ�������������߳���
		System.out.printf("Main: Fork/Join Pool: Queued Submission: %d\n",
				pool.getQueuedSubmissionCount());// �������ύ�����е���δִ�е�������
		System.out.printf("Main: Fork/Join Pool: Queued Tasks: %d\n",
				pool.getQueuedTaskCount());// �����Ѿ���ʼִ�е�������
		System.out.printf("Main: Fork/Join Pool: Queued Submissions: %s\n",
				pool.hasQueuedSubmissions());// �ж��Ƿ������ύ�����е���δִ�е�����
		System.out.printf("Main: Fork/Join Pool: Steal Count: %d\n",
				pool.getStealCount());// �̼߳䷢��������ȡ�Ĵ���
		System.out.printf("Main: Fork/Join Pool: Terminated : %s\n",
				pool.isTerminated());// �жϳ��Ƿ��ѽ�������
		System.out.printf("**********************\n");
	}
	
	/**
	 * �Զ��� RecursiveAction
	 */
	private static class Task extends RecursiveAction {

		private static final long serialVersionUID = 1L;

		private int[] array;

		private int start;
		private int end;

		public Task(int array[], int start, int end) {
			this.array = array;
			this.start = start;
			this.end = end;
		}

		@Override
		protected void compute() {
			if (end - start > 100) {
				int mid = (start + end) / 2;
				Task task1 = new Task(array, start, mid);
				Task task2 = new Task(array, mid, end);
				task1.fork();
				task2.fork();
				task1.join();
				task2.join();
			} else {
				for (int i = start; i < end; i++) {
					array[i]++;
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
