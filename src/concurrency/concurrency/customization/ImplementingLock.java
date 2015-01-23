/**
 * 
 */
package concurrency.customization;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * ʵ���Զ����Lock��ʾ��
 * <p>
 * ����Java����API�ṩ�Ļ���ͬ������֮һ��
 * ���������Ա����������ٽ�����Ҳ����˵��ĳ��ʱ��ֻ��һ���߳���ִ������ٽ�������顣
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��23��
 */
public class ImplementingLock {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyLock lock = new MyLock();
		
		for (int i = 0; i < 10; i++) {
			new Thread(new ShareLockTask("Task-" + i, lock)).start();
		}
		
		// ���߳�Ҳ���� �� �Ļ�ȡ���ͷ�
		boolean value;
		do {
			try {
				// ʹ��tryLock()�������Ի�ȡ�����ȴ�1�룬����޷���������ӡһ����Ϣ�����³��ԡ�
				value = lock.tryLock(1, TimeUnit.SECONDS);
				if (!value) {
					System.out.printf("Main: Trying to get the Lock\n");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				value = false;
			}
		} while (!value);
		
		System.out.printf("Main: Got the lock\n");
		lock.unlock();// �ͷ���
		
		System.out.printf("Main: End of the program\n");
	}

	/**
	 * �Զ���ͬ����
	 * <p>
	 * Java����API�ṩ�� AbstractQueuedSynchronizer ������ʵ��ӵ�������ź���������ͬ�����ơ�
	 * ����һ�������࣬���ṩ�����������������ƶ��ٽ����ķ��ʡ��������ڵȴ������ٽ����������̶߳��С�
	 * �����������ǻ��������������󷽷���tryAcquire��tryRelease
	 * <p>
	 * AbstractQueuedSynchronizer �������ʵ���඼��˽�е��ڲ��࣬���Բ���ֱ��ʹ�����ǡ�
	 */
	private static class MyQueuedSynchronizer extends AbstractQueuedSynchronizer {

		private static final long serialVersionUID = 1L;
		
		/*
		 * ʹ�� AtomicInteger ԭ�ӱ��������ƶ��ٽ����ķ��ʡ�
		 * ����������ɵģ����������ֵΪ0�������߳̿��Է����ٽ�����
		 * ������������ģ����������ֵΪ1�������̲߳��ܷ�������ٽ�����
		 */
		private AtomicInteger state;

		public MyQueuedSynchronizer() {
			this.state = new AtomicInteger(0);
		}

		/**
		 * �����Է����ٽ���ʱ�������������
		 * <p>
		 * ����������������߳̿��Է����ٽ������˷�������true�����򷵻�false��
		 * 
		 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#tryAcquire(int)
		 */
		@Override
		protected boolean tryAcquire(int arg) {
			// �÷�����ͼ������state��ֵ��0���1������ɹ����򷵻�true�����򷵻�false��
			return state.compareAndSet(0, 1);
		}
		
		/**
		 * �������˳����ٽ����ķ���ʱ�������������
		 * <p>
		 * ����������������߳̿����ͷŶ��ٽ����ķ��ʣ��˷�������true�����򷵻�false��
		 * 
		 * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#tryAcquire(int)
		 */
		@Override
		protected boolean tryRelease(int arg) {
			// �÷�����ͼ������state��ֵ��1���0������ɹ����򷵻�true�����򷵻�false��
			return state.compareAndSet(1, 0);
		}
	}
	
	/**
	 * �Զ������
	 */
	private static class MyLock implements Lock {

		private AbstractQueuedSynchronizer synchronizer;
		
		public MyLock() {
			this.synchronizer = new MyQueuedSynchronizer();
		}

		/**
		 * ����Ҫ����һ���ٽ���ʱ���������������
		 * ������߳����ڷ�������ٽ����������߳̽�������ֱ�����ͷź����Ǳ����ѣ��Ӷ���ȡ���ٽ����ķ���Ȩ��
		 * 
		 * @see java.util.concurrent.locks.Lock#lock()
		 */
		@Override
		public void lock() {
			synchronizer.acquire(1);
		}

		@Override
		public void lockInterruptibly() throws InterruptedException {
			synchronizer.acquireInterruptibly(1);
		}

		@Override
		public boolean tryLock() {
			try {
				return synchronizer.tryAcquireNanos(1, 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		public boolean tryLock(long time, TimeUnit unit)
				throws InterruptedException {
			try {
				return synchronizer.tryAcquireNanos(1, TimeUnit.NANOSECONDS.convert(time, unit));
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}

		/**
		 * ���ٽ�������ʱ��������������Ա��ͷ������������̷߳��ʴ��ٽ�����
		 * 
		 * @see java.util.concurrent.locks.Lock#unlock()
		 */
		@Override
		public void unlock() {
			synchronizer.release(1);
		}

		@Override
		public Condition newCondition() {
			// ���� synchronizer ������ڲ��� ConditionObject ʵ��
			return synchronizer.new ConditionObject();
		}
	}
	
	private static class ShareLockTask implements Runnable {
		
		private MyLock lock;
		private String name;
		
		public ShareLockTask(String name, MyLock lock) {
			this.name = name;
			this.lock = lock;
		}

		/**
		 * �������˯��2��Ȼ���ͷ���
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			lock.lock();
			System.out.printf("Task %s: Take the lock\n", name);
			try {
				TimeUnit.SECONDS.sleep(2);
				System.out.printf("Task %s: Free the lock\n", name);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}
}
