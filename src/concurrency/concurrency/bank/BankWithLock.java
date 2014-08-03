/**
 * 
 */
package concurrency.bank;

import java.util.concurrent.locks.ReentrantLock;


/**
 * ʹ����������
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2007-12-12
 */
public class BankWithLock extends AbstractBank {

	private ReentrantLock lock = new ReentrantLock();// �����������

	public BankWithLock(int accNumber, double initialBalance) {
		super(accNumber, initialBalance);
	}
	
	/**
	 * ת�˲���(����)
	 * 
	 * �����Ĳ���һ������ģ�����ݿ��е�ԭ���Բ���
	 */
	public void transfer(Account from, Account to, double amount) {
		if (from.getID().equals(to.getID()))
			return;
		/*
		 * �̻߳�ȡ���� 
		 * 
		 * ���������û�б���һ���̱߳��֣���ǰ�̻߳�ȡ���������������أ��������ı��ּ�������Ϊ1��
		 * �����ǰ�߳��Ѿ����ָ��������򽫱��ּ����� 1�����Ҹ÷����������� ��
		 * �������������һ���̱߳��֣�������̵߳��ȵ�Ŀ�ģ����õ�ǰ�̣߳�
		 * �����ڻ������֮ǰ�����߳̽�һֱ��������״̬����ʱ�������ּ���������Ϊ1��
		 */
		lock.lock();
		try {
			processAccount(from, to, amount);
		} finally {
			lock.unlock();// ����
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