/**
 * 
 */
package concurrency.demo.test;

import concurrency.demo.Account;
import concurrency.demo.Bank;

/**
 * 银行基类
 * <p>
 * 只提供一些工具方法的默认实现，不提供转账操作。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2007-12-12
 */
public abstract class AbstractBank implements Bank {

	private Account[] accounts;// 所有的账户

	/**
	 * @param accNumber
	 *            账户数目
	 * @param initialBalance
	 *            账户初始余额
	 */
	public AbstractBank(int accNumber, double initialBalance) {
		this.accounts = new Account[accNumber];
		for (int i = 0; i < accounts.length; i++) {
			accounts[i] = new Account(Integer.toString(i+1), initialBalance);
		}
	}

	/**
	 * 转账操作的账号处理细节
	 */
	protected void processAccount(Account from, Account to, double amount) {
		if (from.getBalance() < amount)
			return;
		from.out(amount);
		to.in(amount);
		System.out.println("从  " + from.getID() +" 转出  " + amount +" 到  " + to.getID());
		System.out.println("Total balance：" + getTotalBalance());
	}

	public Account[] getAccounts() {
		return accounts;
	}

	/**
	 * 返回银行总余额
	 */
	protected double getTotalBalance() {
		double sum = 0;
		for (int i = 0; i < accounts.length; i++) {
			sum += accounts[i].getBalance();
		}
		return sum;
	}
}