/**
 * 
 */
package concurrency.bank.test;

import concurrency.bank.Bank;
import concurrency.bank.BankWithLock;
import concurrency.bank.TransferTest;

/**
 * ʹ�������Ʋ�����ת��
 * <p>
 * ������Զ�������Total balance������1000�������
 *
 * @author ����ΰ
 *
 * �������ڣ�2013-6-7
 */
public class BankWithLockTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Bank bank = new BankWithLock(10, 100);
		TransferTest.run(bank);
	}
}