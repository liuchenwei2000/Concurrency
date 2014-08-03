/**
 * 
 */
package concurrency.bank;

/**
 * 不控制并发的银行类
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2007-12-12
 */
public class UnsynBank extends AbstractBank {

	public UnsynBank(int accNumber, double initialBalance) {
		super(accNumber, initialBalance);
	}

	/**
	 * 转账操作
	 * 
	 * @param from
	 *            转出账户
	 * @param to
	 *            转入账户
	 * @param amount
	 *            转账金额
	 */
	public void transfer(Account from, Account to, double amount) {
		if (from.getID().equals(to.getID())) {
			return;
		}
		processAccount(from, to, amount);
	}
}