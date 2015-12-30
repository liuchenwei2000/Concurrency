/**
 * 
 */
package concurrency.atomic;

/**
 * 银行账户
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月20日
 */
public interface IAccount {

	/**
	 * 向账户打钱
	 */
	public void addAmount(int amount);
	
	/**
	 * 从账户扣钱
	 */
	public void subtractAmount(int amount);
	
	/**
	 * 返回账户余额
	 */
	public int getBalance();
}
