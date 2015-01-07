/**
 * 
 */
package thread;

/**
 * �߳����ȼ�
 * <p>
 * ÿһ���̶߳���һ�����ȼ��������̵߳��������ԣ������л�����ѡһ���µ��߳�����ʱ���ͻ��ȿ��Ǹ����ȼ����̡߳�
 * ���ǣ��߳����ȼ��Ǹ߶���������ϵͳ�ġ��������������������ƽ̨���߳�ʵ�ֻ���ʱ��
 * Java�߳����ȼ���ӳ�䵽������ƽ̨�ϵ����ȼ���������ƽ̨�����ȼ�������ܸ�Java�ﲻͬ��
 * <p>
 * ����Windowsϵͳ��7�����ȼ����𣬶�SunΪLinux�ṩ��Java������У��߳����ȼ�����ȫ���ԣ������߳̾�����ͬ�����ȼ���
 * ��ˣ���ý����߳����ȼ������̵߳�������һ���ο����أ����ɽ������ܵ���ȷ�����������ȼ���
 * <p>
 * ���ĳ���̵߳����ȼ�̫�ͣ������������ȼ����߳��ֺ��ٷ�����������ô�����ȼ����߳̿�����Զ�޷�������ִ�У�����ǡ���������
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-7
 */
public class ThreadPriorityTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		showThreadPriority();
		/*
		 * Ĭ������£�һ���̼̳߳����ĸ��̵߳����ȼ������߳̾������������Ǹ��̡߳�
		 */
		Thread t1 = new Thread(new PriorityWorker(), "t1");
		t1.start();
		/*
		 * ����ͨ��setPriority�����趨�̵߳����ȼ�������ʮ������Thread���е�**_PRIORITY������ʾ��
		 */
		Thread t2 = new Thread(new PriorityWorker(), "t2");
		t2.setPriority(Thread.MAX_PRIORITY);// ������ȼ���10��
		t2.start();

		Thread t3 = new Thread(new PriorityWorker(), "t3");
		t3.setPriority(Thread.MIN_PRIORITY);// ������ȼ���1��
		t3.start();
	}
	
	private static void showThreadPriority(){
		Thread t = Thread.currentThread();
		int priority = t.getPriority();
		System.out.println("Thread " + t.getName() + " priority is " + priority);
	}
	
	private static class PriorityWorker implements Runnable {

		@Override
		public void run() {
			showThreadPriority();
		}
	}
}