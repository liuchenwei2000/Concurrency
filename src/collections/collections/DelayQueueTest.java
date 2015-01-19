/**
 * 
 */
package collections;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue ʾ��
 * <p>
 * DelayQueue ���� Delayed Ԫ�ص�һ���޽��������У�ֻ�����ӳ�����ʱ���ܴ�����ȡ��Ԫ�ء�
 * �������е���ط����������Щ�ӳٵĶ���ֱ����Щ������Ϊ���������
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��16��
 */
public class DelayQueueTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DelayQueue<Event> queue = new DelayQueue<>();

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

		System.out.printf("Main: Queue Size: %d\n", queue.size());
		
		// ÿ�� 500ms ��ӡһ�ζ����ڵ�Ԫ��
		do {
			int counter = 0;
			Event event;
			do {
				// ��ȡ���Ƴ��˶��е�ͷ������˶��в����������ѵ����ӳ�ʱ���Ԫ�أ��򷵻� null��
				event = queue.poll();
				// ��ȡ���Ƴ��˶��е�ͷ�����ڿɴӴ˶��л�õ����ӳٵ�Ԫ��֮ǰһֱ�ȴ������б�Ҫ����
//				event = queue.take();
				// ��ȡ�����Ƴ��˶��е�ͷ��������˶���Ϊ�գ��򷵻� null��
//				event = queue.peek();
				if (event != null) {
					counter++;
				}
			} while (event != null);
			System.out.printf("At %s you have read %d events\n", new Date(), counter);
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (queue.size() > 0);// ע�� size() ��������������Ԫ������������ʱ����ͷ���ʱ����

		// ��ȫ�Ƴ��˶����е�����Ԫ�ء�
//		queue.clear();
		System.out.printf("Main: End of the program\n");
	}
	
	/**
	 * ���� DelayQueue ��Ԫ����Ҫʵ�� Delayed �ӿڣ��ýӿ����������ЩӦ���ڸ����ӳ�ʱ��֮��ִ�еĶ���
	 */
	private static class Event implements Delayed {

		private Date activationDate;// ���󼤻�����

		public Event(Date activationDate) {
			this.activationDate = activationDate;
		}

		/**
		 * ������˶�����ص�ʣ���ӳ�ʱ�䣬�Ը�����ʱ�䵥λ��ʾ��
		 * 
		 * @see java.util.concurrent.Delayed#getDelay(java.util.concurrent.TimeUnit)
		 */
		@Override
		public long getDelay(TimeUnit unit) {
			Date now = new Date();
			long diff = activationDate.getTime() - now.getTime();
			return unit.convert(diff, TimeUnit.MILLISECONDS);
		}
		
		/**
		 * Delayed �ӿڼ̳��� Comparable �ӿڣ�����Ҫʵ������ıȽϷ���
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(Delayed o) {
			long result = this.getDelay(TimeUnit.NANOSECONDS)
					- o.getDelay(TimeUnit.NANOSECONDS);
			if (result < 0) {
				return -1;
			} else if (result > 0) {
				return 1;
			}
			return 0;
		}
	}

	/**
	 * ���Ԫ�ص�����
	 */
	private static class Task implements Runnable {

		private int id;
		private DelayQueue<Event> queue;
		
		public Task(int id, DelayQueue<Event> list) {
			this.id = id;
			this.queue = list;
		}

		@Override
		public void run() {
			Date now = new Date();
			Date delay = new Date(now.getTime() + (id * 1000));
			System.out.printf("Thread %s: %s\n", id, delay);
			// ��� 100 ����ʱ����
			for (int i = 0; i < 100; i++) {
				queue.add(new Event(delay));
				// ��ָ��Ԫ�ز�����ӳٶ��С� 
//				events.put(new Event(id, i));
			}
		}
	}
}
