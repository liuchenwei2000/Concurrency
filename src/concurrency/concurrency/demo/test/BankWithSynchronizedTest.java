/**
 * 
 */
package concurrency.demo.test;

import concurrency.demo.Bank;
import concurrency.demo.TransferTest;

/**
 * ʹ��synchronized���Ʋ�����ת��
 * <p>
 * ������Զ�������Total balance������1000�������
 *
 * @author ����ΰ
 *
 * �������ڣ�2013-6-7
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