/**
 * 
 */
package collections;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * ConcurrentLinkedDeque ʾ��
 * <p>
 * ����List�������߳�ͬʱ���ӻ�ɾ��Ԫ�ض�����������ݲ�һ�¡�
 * Java7����� ConcurrentLinkedDeque ʵ���˷���������List���ܡ�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��16��
 */
public class ConcurrentLinkedDequeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConcurrentLinkedDeque<String> list = new ConcurrentLinkedDeque<>();
		
		// ��ʹ�� 100 ���߳����Ԫ��
		Thread[] addTasks = new Thread[100];
		for (int i = 0; i < addTasks.length; i++) {
			addTasks[i] = new Thread(new AddTask(list));
			addTasks[i].start();
		}
		System.out.printf("Main: %d AddTask threads have been launched\n", addTasks.length);
		
		// �ȴ��߳�ȫ������
		for (Thread thread : addTasks) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		/*
		 * ��ȡListԪ����������ͨ�� size() ��������Ҫ��ס�÷����ķ���ֵ��������ȫ׼ȷ�ģ��ر���
		 * �����ø÷���ʱ���������߳����ӻ�ɾ��Ԫ�ء�����ֻ����ȷ��û�������߳��޸�Listʱ���÷����Ż᷵��׼ȷ��ֵ��
		 */
		System.out.printf("Main: Size of the List: %d\n", list.size());// ����Ӧ�÷��� 10000
		
		// ���� 100 ���߳�ɾ��Ԫ��
		Thread[] pollTasks = new Thread[100];
		for (int i = 0; i < pollTasks.length; i++) {
			pollTasks[i] = new Thread(new PollTask(list));
			pollTasks[i].start();
		}
		System.out.printf("Main: %d PollTask threads have been launched\n", pollTasks.length);
		
		// �ȴ��߳�ȫ������
		for (Thread thread : pollTasks) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("Main: Size of the List: %d\n", list.size());// ����Ӧ�÷��� 0
		
	}

	/**
	 * ���Ԫ�ص�����
	 */
	private static class AddTask implements Runnable {

		private ConcurrentLinkedDeque<String> list;
		
		public AddTask(ConcurrentLinkedDeque<String> list) {
			this.list = list;
		}

		@Override
		public void run() {
			String name = Thread.currentThread().getName();
			for (int i = 0; i < 100; i++) {
				list.add(name + "-" + i);
			}
		}
	}
	
	/**
	 * ɾ��Ԫ�ص�����
	 */
	private static class PollTask implements Runnable {

		private ConcurrentLinkedDeque<String> list;
		
		public PollTask(ConcurrentLinkedDeque<String> list) {
			this.list = list;
		}

		@Override
		public void run() {
			for (int i = 0; i < 50; i++) {
				// ����ķ������Ի�ȡԪ�ز�ɾ��������б�Ϊ���򷵻� null
				list.pollFirst();// �׸�Ԫ��
				list.pollLast();// ����Ԫ��
				
				// ����ķ�������ֻ��ȡԪ�ض�������������������б�Ϊ�����׳� NoSuchElementExcpetion
//				list.getFirst();
//				list.getLast();
				
				// ����ķ�������ֻ��ȡԪ�ض�������������������б�Ϊ���򷵻� null
//				list.peekFirst();
//				list.peekLast();
				
				// ����ķ������Ի�ȡԪ�ز�ɾ��������б�Ϊ�����׳� NoSuchElementExcpetion
//				list.removeFirst();
//				list.removeLast();
			}
		}
	}
}
