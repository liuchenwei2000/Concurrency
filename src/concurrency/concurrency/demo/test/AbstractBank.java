/**
 * 
 */
package concurrency.demo.test;

import concurrency.demo.Account;
import concurrency.demo.Bank;

/**
 * ���л���
 * <p>
 * ֻ�ṩһЩ���߷�����Ĭ��ʵ�֣����ṩת�˲�����
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2007-12-12
 */
public abstract class AbstractBank implements Bank {

	private Account[] accounts;// ���е��˻�

	/**
	 * @param accNumber
	 *            �˻���Ŀ
	 * @param initialBalance
	 *            �˻���ʼ���
	 */
	public AbstractBank(int accNumber, double initialBalance) {
		this.accounts = new Account[accNumber];
		for (int i = 0; i < accounts.length; i++) {
			accounts[i] = new Account(Integer.toString(i+1), initialBalance);
		}
	}

	/**
	 * ת�˲������˺Ŵ���ϸ��
	 */
	protected void processAccount(Account from, Account to, double amount) {
		if (from.getBalance() < amount)
			return;
		from.out(amount);
		to.in(amount);
		System.out.println("��  " + from.getID() +" ת��  " + amount +" ��  " + to.getID());
		System.out.println("Total balance��" + getTotalBalance());
	}

	public Account[] getAccounts() {
		return accounts;
	}

	/**
	 * �������������
	 */
	protected double getTotalBalance() {
		double sum = 0;
		for (int i = 0; i < accounts.length; i++) {
			sum += accounts[i].getBalance();
		}
		return sum;
	}
}