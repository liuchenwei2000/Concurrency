/**
 * 
 */
package concurrency.bank;

/**
 * 银行账户
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2007-12-12
 */
public class Account {

	private String id;// 账户ID
	private double balance;// 账户余额

	public Account(String id, double balance) {
		this.id = id;
		this.balance = balance;
	}

	public String getID() {
		return id;
	}

	public double getBalance() {
		return balance;
	}
	
	/**
	 * 存钱或者转入操作
	 * 
	 * @param amount
	 *            转入金额
	 */
	public void in(double amount) {
		balance += amount;
	}

	/**
	 * 取钱或者转出操作
	 * 
	 * @param amount
	 *            转出金额
	 */
	public void out(double amount) {
		balance -= amount;
	}
}