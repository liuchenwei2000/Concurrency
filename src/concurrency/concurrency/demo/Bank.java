/**
 * 
 */
package concurrency.demo;

/**
 * 银行接口(模拟转账)
 *
 * @author 刘晨伟
 *
 * 创建日期：2013-6-7
 */
public interface Bank {

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
	public void transfer(Account from, Account to, double amount);
	
	/**
	 * 返回所有账户信息
	 */
	public Account[] getAccounts();
}