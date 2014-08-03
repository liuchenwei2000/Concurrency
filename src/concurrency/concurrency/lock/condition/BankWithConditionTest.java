/**
 * 
 */
package concurrency.lock.condition;

import concurrency.bank.Bank;
import concurrency.bank.TransferTest;

/**
 * 使用锁和条件对象控制并发的转账
 * <p>
 * 本例永远不会出现Total balance不等于1000的情况。
 *
 * @author 刘晨伟
 *
 * 创建日期：2013-6-7
 */
public class BankWithConditionTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Bank bank = new BankWithCondition(10, 100);
		TransferTest.run(bank);
	}
}