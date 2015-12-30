/**
 * 
 */
package concurrency.bank;

import java.util.Random;

/**
 * 转账线程
 * <p>
 * 从指定账户不断地向随机账户转账随机金额
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2007-12-12
 */
public class TransferThread extends Thread {

	private static final Random RANDOM = new Random();
	
	private Bank bank;// 银行
	private Account from;// 转出账户
	private double amount;// 转出金额
	
	private int totalAcounts;// 银行的所有账户数

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
