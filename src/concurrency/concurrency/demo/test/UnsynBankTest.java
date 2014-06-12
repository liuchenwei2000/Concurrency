/**
 * 
 */
package concurrency.demo.test;

import concurrency.demo.Bank;
import concurrency.demo.TransferTest;

/**
 * 没有控制并发的转账
 * <p>
 * 本例在运行一段时间后，或早或晚必会出现Total balance不等于1000的情况。
 *
 * @author 刘晨伟
 *
 * 创建日期：2013-6-7
 */
public class UnsynBankTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Bank bank = new UnsynBank(10, 100);
		TransferTest.run(bank);
	}
}