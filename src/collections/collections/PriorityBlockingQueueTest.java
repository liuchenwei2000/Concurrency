/**
 * 
 */
package collections;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * PriorityBlockingQueue ʾ��
 * <p>
 * PriorityBlockingQueue �ǲ���������ʹ�õ������޽磩�б�����Ԫ����Ҫʵ�� Comparable �ӿڡ�
 * ������Ԫ��ʱ��PriorityBlockingQueue �����Ԫ��ʵ�ֵ� compareTo() �������������Ĳ���λ�ã�Խ���Ԫ��Խ����
 * ���⣬PriorityBlockingQueue ���������У����ĳЩ�������������Ϊ��ʱʹ��poll()����
 * ��ȡ��ɾ��һ��Ԫ�أ���������ִ���򽫻������߳�ֱ��������ɡ�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��16��
 */
public class PriorityBlockingQueueTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PriorityBlockingQueue<Event> queue = new PriorityBlockingQueue<>();
		
		// ��ʹ�� 5 ���߳����Ԫ��
		Thread[] threads = new Thread[5];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Task(i, queue));
			threads[i].start();
		}
		
		// �ȴ��߳�ȫ������
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.printf("Main: Queue Size: %d\n",queue.size());
		// ���߳̿�ʼ�� PriorityBlockingQueue ȡ��Ԫ��
		for (int i = 0; i < threads.length * 100; i++) {
			// ��ȡ���Ƴ��˶��е�ͷ������˶���Ϊ�գ��򷵻� null��
			Event event = queue.poll();
			System.out.println(event);
		}
		
		System.out.println("501 = " + queue.poll());
		System.out.printf("Main: Queue Size: %d\n", queue.size());
		
		try {
			// ��ȡ���Ƴ��˶��е�ͷ����ָ���ĵȴ�ʱ��ǰ�ȴ����õ�Ԫ�أ�����б�Ҫ����
			Event event = queue.poll(5, TimeUnit.SECONDS);
			System.out.println(event == null ? "time out" : event);
			// ��ȡ���Ƴ��˶��е�ͷ������Ԫ�ر�ÿ���֮ǰһֱ�ȴ�������б�Ҫ����
//			event = queue.take();
			// ��ȡ�����Ƴ��˶��е�ͷ������˶���Ϊ�գ��򷵻� null��
//			event = queue.peek();
			// ��ȫ�Ƴ��˶����е�����Ԫ�ء�
//			queue.clear();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Main: End of the program\n");
	}
	
	private static class Event implements Comparable<Event> {

		private int thread;
		private int priority;
		
		public Event(int thread, int priority) {
			this.thread = thread;
			this.priority = priority;
		}

		/**
		 * ʵ�ֱȽϷ���
		 * <p>
		 * ����ֵ˵����<p>
		 * 1����ʾthis����С�ڲ�������
		 * 0����ʾthis������ڲ�������
		 * -1����ʾthis������ڲ�������
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(Event o) {
			if (this.priority > o.getPriority()) {
				return -1;
			} else if (this.priority < o.getPriority()) {
				return 1;
			}
			return 0;
		}

		public int getPriority() {
			return priority;
		}

		@Override
		public String toString() {
			return "Event [thread=" + thread + ", priority=" + priority + "]";
		}
	}

	/**
	 * ���Ԫ�ص�����
	 */
	private static class Task implements Runnable {

		private int id;
		private PriorityBlockingQueue<Event> events;
		
		public Task(int id, PriorityBlockingQueue<Event> list) {
			this.id = id;
			this.events = list;
		}

		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {
				events.add(new Event(id, i));
				// ��ָ��Ԫ�ز�������ȼ����С��ö������޽�ģ����Դ˷������������� 
//				events.put(new Event(id, i));
			}
		}
	}
}
