/**
 * 
 */
package concurrency.lock.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import concurrency.bank.AbstractBank;
import concurrency.bank.Account;

/**
 * ʹ�������������������
 * <p>
 * һ���߳̽����ٽ�����ȴ����������ȴ�ĳ��������������ִ�С�
 * ��ʱ��Ҫʹ��һ����������(condition)��������Щ�ѻ������ȴ���ܿ�ʼִ�й������̡߳�
 * <p>
 * �����У�����ϣ���˻�����ʱ��������ת�ˣ�ֻ�ܵȵ�������ſ���ת�ˡ�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2007-12-12
 */
public class BankWithCondition extends AbstractBank {

	private ReentrantLock lock = new ReentrantLock();// �����������

	private Condition enoughBalance;// ��������

	public BankWithCondition(int accNumber, double initialBalance) {
		super(accNumber, initialBalance);
		// һ�������������һ�����߶�����������������
		this.enoughBalance = lock.newCondition();
	}
	
	/**
	 * ת�˲���(��������������)
	 * <p>
	 * ����˻���û���㹻�������߳̽��ȴ���ֱ�������߳�����˻����Ϊֹ��
	 * ������̸߳ջ�����˻����������ַ����������Եġ�����ģ��������κ��̶߳�û�л�����д�������
	 * �����ΪʲôҪʹ�����������ԭ��
	 */
	public void transfer(Account from, Account to, double amount){
		if (from.getID().equals(to.getID()))
			return;
		lock.lock();
		try {
			// �����while�����ǵ���awaitʱ���͵ķ�ʽ��Ҫ��ѭ���в��ϼ�������Ƿ�����
			while (from.getBalance() < amount) {
				try {
					/*
					 * ���������await����һ�������ã���ǰ�߳̾ͻᱻ���������ҷ���������
					 * 
					 * �ȴ���������̺߳͵���await���߳�֮����һ�����ʵĲ��
					 * һ��һ���̵߳�����await���������ͽ����˵ȴ����������У������ɻ��ʱ���̲߳����������������
					 * ��ά������״ֱ̬����һ���̵߳���ͬһ�����������ϵ�singalAll����ʱΪֹ��
					 * �ֻ��߳����˵ȴ�ʱ�ޣ��ֻ����̱߳��жϡ�
					 */
					enoughBalance.await();
					// �������������������趨һ���ȴ�ʱ��
//					enoughBalance.await(100, TimeUnit.MILLISECONDS);// 100����
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			processAccount(from, to, amount);
			/*
			 * ���������signalAll����������еȴ����������̵߳�����״̬��
			 * 
			 * ���̴߳ӵȴ����б�����ʱ�����ǽ��ٴγ�Ϊ�����еģ��̵߳��������ٴμ������ǡ�
			 * ��ʱ�����ǽ���ͼ���½������
			 * һ�����ɻ�ã������е�ĳ���߳̽���await���÷��أ�������������������ĵط�����ִ�С�
			 * ��ʱ���߳�Ӧ���ٴβ�����������Ϊ���ڻ�����ȷ�������Ѿ����㡣
			 * signalAll����������֪ͨ�ȴ����̣߳������������������ˣ���ֵ����ȥ���һ�¡�
			 */
			enoughBalance.signalAll();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * �������������
	 */
	protected double getTotalBalance() {
		double balance = 0;
		lock.lock();
		try {
			balance = super.getTotalBalance();
		} finally {
			lock.unlock();
		}
		return balance;
	}
}