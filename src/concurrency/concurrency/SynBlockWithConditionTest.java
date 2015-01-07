/**
 * 
 */
package concurrency;

/**
 * ��synchronized����ʹ������ʾ��
 * <p>
 * Object���е�wait()��notify()��notifyAll()�������������ͬ�����е��������⡣
 * ������������ֻ����ͬ�����е��ã�������׳��쳣��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��7��
 */
public class SynBlockWithConditionTest {
	
	private static final int MAX_BALANCE = 1000;
	private static final int MIN_BALANCE = 10;

	private int balance;

	public SynBlockWithConditionTest(int balance) {
		this.balance = balance;
	}
	
	public void add(int amount) {
		synchronized (this) {
			// ����ʱ�����жϵĵ��ʹ���ṹ
			while (balance + amount > MAX_BALANCE) {
				try {
					// JVM�Ὣ��ǰ�̹߳���ֱ����ʱ�򱻻��ѣ����ͷ������еĶ��������Ӷ����������߳�ִ�иö����ͬ������롣
					// ���̱߳�����ʱ�������̵߳�����notify()��notifyAll()����ʱ������������ȡ��ö�������Ȼ���ټ�������Ƿ����㡣
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			balance += amount;
			// �������������̣߳�notify()�������������ĳһ���߳�
			notifyAll();
		}
	}

	public void sub(int amount) {
		synchronized (this) {
			while (balance - amount < MIN_BALANCE) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			balance -= amount;
			notifyAll();
		}
	}
}
