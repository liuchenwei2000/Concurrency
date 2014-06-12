/**
 * 
 */
package thread.synchronizer;

import java.util.concurrent.Semaphore;

/**
 * Semaphoreʾ��
 * <p>
 * Semaphore��ʾһ���ź�������������������֤����ɵ���Ŀ�����޵ģ�������������ͨ�����߳�����
 * ʵ���ϣ�û��ʲô��ɶ���Semaphore��ά��һ����������
 * ���б�Ҫ������ɿ���ǰ������ÿһ��acquire()��Ȼ���ٻ�ȡ����ɡ�
 * ÿ��release()���һ����ɣ��Ӷ������ͷ�һ�����������Ļ�ȡ�ߡ�
 * ���ң���ɲ���Ҫ�ɻ�ȡ�����߳��ͷţ������̶߳������ͷš����������Ҳ������Ǳ�ڵĻ��ҡ�
 * <p>
 * ͨ���������ƿ��Է���ĳЩ��Դ���߳���Ŀ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-28
 */
public class SemaphoreTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ģ��8������ͬһ��������ľ������
		Toilet toilet = new Toilet();
		for (int i = 0; i < 8; i++) {
			new Thread(new ToToiletTask("Person" + (i + 1), toilet)).start();
		}
	}

	/**
	 * ������
	 * <p>
	 * ����һ��ϡȱ����Դ�������һ������ʱ�򣬱�����Ŷ�ʹ�ã�����ģ��������̡�
	 */
	private static class Toilet {

		private static final int MAX = 3;

		// �ź������󣬱�������ֻ��3����λ
		private Semaphore semaphore = new Semaphore(MAX);
		// ��λʹ�����
		private boolean[] used = new boolean[MAX];

		/**
		 * ����һ����λ�����ض�λ���
		 */
		public int in() throws Exception {
			/**
			 * ���ź�����ȡһ����ɣ����ṩһ�����֮ǰ��һֱ�������̡߳�
			 * �����һ����ɽ��������أ����������������1��
			 */
			semaphore.acquire();
			return getUnusedIndex();
		}

		/**
		 * �ź�����װ�����ͬ�����������ƶ�������ķ��ʣ���ͬά�������䱾��һ���������ͬ���Ƿֿ��ġ�
		 * ���仰˵���ź�����ͬ����֤��ͬʱֻ�������޵��̷߳�����Դ(������)��
		 * ����Դ�����ͬ����Ϊ�˱�֤�����޵��߳�֮�侺��(��λ)ʱ������һ���ԡ�
		 * ���û����Դ�����ͬ�������ܻᵼ�����������������ͬһ����λ��
		 */
		private synchronized int getUnusedIndex() {
			for (int i = 0; i < used.length; i++) {
				if (!used[i]) {
					markUsed(i, true);
					return i;
				}
			}
			return -1;// ��Ӧ����ִ�е�����
		}

		/**
		 * �뿪ָ����λ
		 */
		public void out(int index) {
			markUsed(index, false);
			/**
			 * �ͷ�һ����ɣ����ź��������������1��
			 * �������߳���ͼ��ȡ��ɣ���ѡ��һ���̲߳����ո��ͷŵ���ɸ�������
			 */
			semaphore.release();
		}

		private synchronized void markUsed(int index, boolean b) {
			used[index] = b;
		}
	}

	/**
	 * ģ������������Ĺ���
	 */
	private static class ToToiletTask implements Runnable {

		private String person;
		private Toilet toilet;

		public ToToiletTask(String person, Toilet toilet) {
			this.person = person;
			this.toilet = toilet;
		}

		@Override
		public void run() {
			try {
				System.out.println(person + " Ҫ�ϲ�����");
				int index = toilet.in();
				System.out.println(person + " ���˵� " + (index + 1) + " ����λ��");
				Thread.sleep((long) (Math.random() * 2000));// ģ���ϲ���
				System.out.println(person + " ������������ˡ�");
				toilet.out(index);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}