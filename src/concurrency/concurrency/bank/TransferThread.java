/**
 * 
 */
package concurrency.bank;

import java.util.Random;

/**
 * ת���߳�
 * <p>
 * ��ָ���˻����ϵ�������˻�ת��������
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2007-12-12
 */
public class TransferThread extends Thread {

	private static final Random RANDOM = new Random();
	
	private Bank bank;// ����
	private Account from;// ת���˻�
	private double amount;// ת�����
	
	private int totalAcounts;// ���е������˻���

	public TransferThread(Bank bank, Account from, double amount) {
		this.bank = bank;
		this.from = from;
		this.amount = amount;
		this.totalAcounts= bank.getAccounts().length;
	}

	@Override
	public void run() {
		try {
			while (this.isAlive()) {
				bank.transfer(from, bank.getAccounts()[RANDOM.nextInt(totalAcounts)], amount);
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}