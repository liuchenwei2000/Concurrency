/**
 * 
 */
package thread.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Callable�����ʾ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-17
 */
public class CallableTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ����һ��Runnable����ķ�ʽ
		new Thread(new Calculation()).start();
		
		// ����һ��Callable����ķ�ʽ
		/**
		 * FutureTask��װ����һ���ܷ���Ľ�Callableת����Future��Runnable�Ļ��ƣ���Ϊ��ͬʱʵ�����������ӿڡ�
		 */
		FutureTask<Integer> futureTask = new FutureTask<Integer>(new IntegerCalculation());
		// ���ｫFutureTask������Ϊһ��Runnable
		new Thread(futureTask).start();
		try {
			/**
			 * ���ｫFutureTask������Ϊһ��Futrue
			 * <p>
			 * Future�����첽����Ľ������ʹ����ʱ��������һ�����㣬�Ѽ���Ľ����һ��Future���󣩽���ĳ�̡߳�
			 * Ȼ�����߳̾Ϳ���ȥ���������飬�ȵ���������֮��Ϳ��Եõ�����
			 */
			// �ж������Ƿ��Ѿ����
			System.out.println("main:Future task is done? " + futureTask.isDone());
			// �ж������Ƿ��Ѿ�ȡ��
			System.out.println("main:Future task is cancelled? " + futureTask.isCancelled());
			// get()�����������ؼ����������������δ��ɣ��÷���������ֱ��������ɡ�
			Integer result = futureTask.get();
			System.out.println("main:Toatl number is " + result);
			/**
			 * ȡ������
			 * <p>
			 * ���������δ��ʼ������ȡ����Ͳ����ٿ�ʼ�ˡ�
			 * ����������ڽ��У���������true�����ͻᱻǿ���жϡ�
			 */
			futureTask.cancel(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Runnable��װһ���첽���е�������һ��û���κβ����ͷ���ֵ���첽������
	 */
	private static class Calculation implements Runnable {

		@Override
		public void run() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int result = 10000000;
			System.out.println("Runnable:Toatl number is " + result);
		}
	}

	/**
	 * Callable��Runnable���ƣ�Ҳ��װ��һ���첽���е����񣬵��з���ֵ��
	 */
	private static class IntegerCalculation implements Callable<Integer> {

		@Override
		public Integer call() throws Exception {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int result = 10000000;
			System.out.println("Callable:Toatl number is " + result);
			return result;
		}
	}
}