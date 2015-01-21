/**
 * 
 */
package concurrency.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ԭ�ӱ���ʾ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��20��
 */
public class AtomicVariableTest {

	/**
	 * һ���̲߳��ϵض�IAccount�е����ִ�м�100��������һ���߳���ִ�м�100����������ִ��10000�Ρ�
	 * ���IAccount �е�����Ƿ��ǳ�ʼֵ1000���������֤��ԭ�ӱ��� AtomicInteger ����Ч�ԡ�
	 */
	public static void main(String[] args) {
		System.out.println("��Test NormalVariableAccount��");
		test(new NormalVariableAccount(1000));

		System.out.println("\n��Test AtomicVariableAccount��");
		test(new AtomicVariableAccount(1000));
	}

	private static void test(IAccount account) {
		System.out.println("Account Initial balance��" + account.getBalance());

		Thread company = new Company(account);
		Thread bank = new Bank(account);

		company.start();
		bank.start();

		try {
			company.join();
			bank.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Account Final balance��" + account.getBalance());
	}

	/**
	 * ʹ�ó������ʵ�ֵ�Account�����ܱ�֤����һ���ԡ�
	 */
	private static class NormalVariableAccount implements IAccount {

		// ��ͨ�� int �ͱ���
		private int balance;

		public NormalVariableAccount(int balance) {
			this.balance = balance;
		}

		public int getBalance() {
			return balance;
		}

		public void addAmount(int amount) {
			// �ⲽ����������ʱ������ü���ָ��
			this.balance += amount;
		}

		public void subtractAmount(int amount) {
			this.balance -= amount;
		}
	}

	/**
	 * ʹ��ԭ�ӱ���ʵ�ֵ�Account���ܹ���֤����һ���ԡ�
	 */
	private static class AtomicVariableAccount implements IAccount {

		// ʹ��ԭ�ӱ��������⻹�� AtomicLong��AtomicBoolean��AtomicReference��ԭ�ӱ���ʵ��
		private AtomicInteger balance;

		public AtomicVariableAccount(int balance) {
			// ʹ�� int ֵ��ʼ��һ�� AtomicInteger
			this.balance = new AtomicInteger(balance);
		}

		public int getBalance() {
			return balance.get();// ����ԭ�ӱ����е�ֵ
		}

		public void addAmount(int amount) {
			// ��ԭ�ӷ�ʽ������ֵ�뵱ǰֵ��Ӳ����ظ��µ�ֵ�� 
			this.balance.addAndGet(amount);
			// ����ķ���Ҳ����֤������ԭ����
//			this.balance.getAndAdd(amount);// ��ԭ�ӷ�ʽ������ֵ�뵱ǰֵ��Ӳ�������ǰ��ֵ 
//			this.balance.incrementAndGet();// ��ԭ�ӷ�ʽ����ǰֵ�� 1
//			this.balance.decrementAndGet();// ��ԭ�ӷ�ʽ����ǰֵ�� 1
//			this.balance.getAndSet(1);// ��ԭ�ӷ�ʽ����Ϊ����ֵ�������ؾ�ֵ
		}

		public void subtractAmount(int amount) {
			this.balance.addAndGet(-amount);
		}
	}
}