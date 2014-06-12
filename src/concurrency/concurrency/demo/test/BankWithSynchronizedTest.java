/**
 * 
 */
package concurrency.demo.test;

import concurrency.demo.Bank;
import concurrency.demo.TransferTest;

/**
 * 使用synchronized控制并发的转账
 * <p>
 * 本例永远不会出现Total balance不等于1000的情况。
 *
 * @author 刘晨伟
 *
 * 创建日期：2013-6-7
 */
public class BankWithSynchronizedTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Bank bank = new BankWithSynchronized(10, 100);
		TransferTest.run(bank);
	}
}