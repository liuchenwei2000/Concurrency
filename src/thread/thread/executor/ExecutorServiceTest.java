/**
 * 
 */
package thread.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import util.TimeCounter;

/**
 * ExecutorServiceʾ��
 * <p>
 * ExecutorService�ӿڼ̳���Executor�ӿڣ�������Ҫ������������
 * <li>1����Ϊ�̳߳���ʹ�ã���������ִ�е�Ч�ʡ�
 * <li>2����������һ����ص��̣߳�����ȡ����������ȵȡ�
 * <p>
 * ����������<p>
 * ���ܲ��ص��� Thread���Ժδ����� Executor�ӿ�ȱ��������Ա����������ĳ�ֹ���:
 * ���罫ִ����Ҫ�������ݿ�� UI����������ò��������˺ܳ�ʱ�䣬����ϣ���������֮ǰȡ������
 * Ϊ��JDK������һ���������õĳ���(ExecutorService�ӿ�)�������߳�����������ģΪһ���ɼ��п��Ƶķ���
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-25
 */
public class ExecutorServiceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// һ��ExecutorService����
		ExecutorService executorService = Executors.newCachedThreadPool();
		// ����10������
		List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
		tasks.add(new LongTask());
		for (int i = 0; i < 10; i++) {
			tasks.add(TaskFactory.createCallable());
		}
		
		try {
			TimeCounter tc = new TimeCounter();
			tc.start();
			
			/**
			 * invokeAny����
			 * <p>
			 * ִ�и����������κ�(any)һ����������˾ͻ��������ؽ����
			 * ��ˣ��޷�֪�������صľ������ĸ�����Ҳ���������ɵ��Ǹ���
			 * �ڴ���һ������ʱ��������Խ����κν�������Ϳ���ʹ�����������
			 */
			Integer result = executorService.invokeAny(tasks);
			System.out.println("result is " + result);
			
			tc.stop();
			System.out.println("invokeAny��" + tc.consumeBySecond() + "s��");
			
			/**
			 * invokeAll����
			 * <p>
			 * ִ�и�������������(all)����ȫ������˲Ż᷵�ؽ�����������������������������
			 * ���ؽ��������Ԫ�ص� Future.isDone() Ϊ true��
			 */
			tc.start();
			List<Future<Integer>> results = executorService.invokeAll(tasks);
			for (Future<Integer> future : results) {
				System.out.println("result is " + future.get());
			}
			tc.stop();
			System.out.println("invokeAll��" + tc.consumeBySecond() + "s��");
			
			/**
			 * ����������������ɻ�õ�˳�򱣴�������������壬��ͨ�� ExecutorCompletionService ��ʵ�����С�
			 * �÷������һ��Future������������У������ύ֮�����ִ�н��������Щ�����Ϊ����ʱ����
			 */
			tc.start();
			ExecutorCompletionService<Integer> service = new ExecutorCompletionService<Integer>(executorService);
			for (Callable<Integer> task : tasks) {
				service.submit(task);// �ύ����
			}
			for (int i = 0; i < tasks.size(); i++) {
				// take�������Ƴ���һ�����õĽ�������û���κο��ý����������
				System.out.println(service.take().get());
			}
			tc.stop();
			System.out.println("submit/take��" + tc.consumeBySecond() + "s��");
			
			executorService.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class LongTask implements Callable<Integer> {

		@Override
		public Integer call() throws Exception {
			Thread.sleep(5000);
			return 5000;
		}
	}
}