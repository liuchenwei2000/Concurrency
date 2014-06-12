/**
 * 
 */
package thread.synchronizer;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatchʾ��
 * <p>
 * CountDownLatchʵ���˵������������Ĺ��ܣ�ֻ��������������Ĳ�����ԭ�Ӳ�����
 * ͬʱֻ����һ���߳�ȥ���������������Ҳ����ͬʱֻ����һ���߳�ȥ����������������ֵ��
 * <p>
 * �����һ�����������߳���ִ�еĲ���֮ǰ��������һ�������߳�һֱ�ȴ���
 * <p>
 * �ø����ļ�����ʼ��CountDownLatchʵ��������countDown()�����Ὣ������1��
 * �ڵ�ǰ�����ﵽ0֮ǰ��await������һֱ������֮�����������ͷ����еȴ����̣߳�await�����к������ö����������ء�
 * ��������ֻ����һ�Σ������޷������ã�����Ҫ���ü������ɿ���ʹ��CyclicBarrier��
 * <p>
 * ���⣬CountDownLatch��CyclicBarrier�����𻹿��Դ�����ĽǶ���⣺
 * <li>CountDownLatch:һ���߳�(���߶��)�� �ȴ�����N���߳����ĳ������֮�����ִ�С�
 * <li>CyclicBarrier: N���߳��໥�ȴ����κ�һ���߳����֮ǰ�����е��̶߳�����ȴ���
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-26
 */
public class CountDownLatchTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Team().doWork();
	}
}

/**
 * ģ��ĳ���Ŷ�
 * <p>
 * ����Leader��������Ȼ����Ա�ǿ�ʼ�������ȹ���ȫ����ɺ�Leader�����չ�����
 */
class Team {

	/**
	 * ����ʼ�źţ���Leader��������ź�
	 * <p>
	 * ������1��ʼ����CountDownLatch����һ���򵥵Ŀ�/��������(������)��
	 * ��ͨ������countDown()���̴߳����ǰ�����е���await���߳̽�һֱ����ڴ��ȴ���
	 */
	private CountDownLatch startSignal = new CountDownLatch(1);
	
	/**
	 * ��������źţ���4����Ա��������ź�
	 * <p>
	 * �ü���N��ʼ����CountDownLatch����ʹһ���߳���N���߳����ĳ�����֮ǰһֱ�ȴ���
	 * ����ʹ����ĳ��������N��֮ǰһֱ�ȴ���
	 */
	private CountDownLatch doneSignal = new CountDownLatch(4);

	/**
	 * �Ŷӿ�ʼ����
	 */
	public void doWork() {
		// ÿ���˴���һ���������߳�
		new Thread(new Leader()).start();
		for (int i = 0; i < 4; i++) {
			new Thread(new Worker("worker" + i)).start();
		}
	}

	/**
	 * Team Leader
	 */
	class Leader implements Runnable {

		@Override
		public void run() {
			try {
				Thread.sleep(1000);
				System.out.println("Leader����ʼ��������...");
				/**
				 * �ݼ��������ļ����������������0�����ͷ����еȴ����̣߳��������Ѿ���0���򲻷����κβ�����
				 * <p>
				 * ����countDown()�������̲߳���Ҫ�ȴ���ֻ����Щ����await�������߳���Ҫ�ȴ���
				 */
				startSignal.countDown();
				System.out.println("Leader�����������ˣ��ֵ��ǿ�ʼ�ɰɣ�ȫ�������������գ�");
				/**
				 * ʹ��ǰ�߳�����������������0֮ǰһֱ�ȴ��������̱߳��жϣ��������Ѿ���0�����������ء�
				 */
				doneSignal.await();// ��Ҫ�ȴ�������Ա�������
				System.out.println("Leader�����������ˣ��ֵ��Ǹɵĺܺã�");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ��Ա
	 */
	class Worker implements Runnable {

		private String name;

		public Worker(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			try {
				System.out.println(name + " ���� Leader ��������...");
				startSignal.await();// ��Ҫ�ȴ�Leader��������
				System.out.println(name + " ��ʼ�ɻ");
				Thread.sleep((long) (Math.random() * 10000));
				doneSignal.countDown();
				System.out.println(name + " ������ˣ�");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}