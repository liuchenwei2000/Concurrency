/**
 * 
 */
package concurrency.synchronizer;

import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrierʾ��
 * <p>
 * CyclicBarrierʵ����һ����Ϊդ���ļ��ϵ㡣
 * <p>
 * CyclicBarrier ������һ��ָ����ֵ��ʼ��������Ϊ��Ҫ�ڼ��ϵ�ȴ����߳�������
 * ������һ���̵߳��Ｏ�ϵ�ʱ����Ҫ���� CyclicBarrier ��await()�������ȴ������̵߳ĵ�������CyclicBarrier 
 * �Ὣ���߳�����ֱ�������������߳�ȫ�������������һ���̵߳��� CyclicBarrier ��await()���������ỽ�����еȴ��������̡߳�
 * <p>
 * ���� CyclicBarrier ����ʹ��һ������� Runnable ������Ϊ��ʼ���������������̵߳��Ｏ�ϵ�󣬸� Runnable 
 * ���󽫱��ŵ�ĳ���߳���ִ�С��������ʹ�� CyclicBarrier �ǳ��ʺ�ʹ�÷ֶ���֮��divide and conquer�������Ĳ�������
 * ������һ�μ����У���Ҫ�����߳����м���Ĳ�ͬ���֡������в��ֶ�׼����ʱ�������Ҫ�����ϲſ��á�
 * ��һ���߳���������ǲ�������󣬾��������е�դ������һ�������̶߳����������դ����դ���ͻᱻ�����������߳̾Ϳ��Լ������С�
 * <p>
 * ��ΪCyclicBarrier���ͷŵȴ��̺߳�������ã����Գ���Ϊѭ����barrier��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-25
 */
public class CyclicBarrierTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Computer(5).doWork();
	}
}

/**
 * ������࣬ģ�⽫ĳ����ֽ�ɶ������������
 */
class Computer {
	
	// դ��
	private CyclicBarrier barrier;
	
	public Computer(int subtask) {
		/*
		 * ����һ��CyclicBarrier�������ڸ�������(����subtask)�Ĳ������̴߳��ڵȴ�״̬ʱ������
		 * ���⻹֧��һ����ѡ��Runnable����(����MainTask)����һ���߳������һ���̵߳���֮��
		 * (�����ͷ������߳�֮ǰ)����(�ò����������һ������ barrier���߳�ִ��)��
		 * ��Runnable����ֻ��ÿ�����ϵ�����һ�Σ��������ͷ����в����߳�֮ǰ ���¹���״̬�������ϲ��������á�
		 */
		 this.barrier = new CyclicBarrier(subtask, new MainTask());
	}
	
	public void doWork() {
		// getParties()����Ҫ��������barrier�Ĳ�������Ŀ
		int total = barrier.getParties();
		// ������ֽ�ɶ����������������ٶ�
		for (int i = 0; i < total; i++) {
			new Thread(new SubTask("task " + (i + 1))).start();
		}
	}

	/**
	 * ������
	 */
	class MainTask implements Runnable {

		@Override
		public void run() {
			System.out.println("��������ִ����ϣ���ʼִ��������......");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("������ִ�����......");
		}
	}

	/**
	 * ������
	 */
	class SubTask implements Runnable {

		private String name;
		
		public SubTask(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			try {
				Thread.sleep((long) (Math.random() * 5000));
				// getNumberWaiting() ���ص�ǰ�����ϴ��ȴ��Ĳ�������Ŀ
				System.out.println("���� " + (barrier.getNumberWaiting() + 1) + " ���̵߳���դ���ڵȴ�");
				/*
				 * ���߳̽�����barrier�ϵȴ���ֱ�����в������̶߳�����barrier(��ȫ��������await����)
				 * <p>
				 * �������һ����դ�����ȴ����߳��뿪��(���糬ʱ���߱��ж�)����ôդ���ͱ��ƻ��ˡ�
				 * �����Ļ������������̵߳���await���������׳�һ��BrokenBarrierException��
				 */
				int index = barrier.await();// ���ص�ǰ�����̵߳�������
				
				if (index == 0) {// 0��ʾ���һ������
					System.out.println(name + " �����һ������");
					// ���� CyclicBarrier �Ա����ʹ��
					barrier.reset();
				}
				// barrier.getParties() - 1 ��ʾ��һ������
				if (index == barrier.getParties() - 1) {
					System.out.println(name + " �ǵ�һ������");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(name + " ���������.");
		}
	}
}