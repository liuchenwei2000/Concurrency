/**
 * 
 */
package concurrency.bank.test;

import concurrency.bank.Bank;
import concurrency.bank.TransferTest;
import concurrency.bank.UnsynBank;

/**
 * û�п��Ʋ�����ת��
 * <p>
 * ����������һ��ʱ��󣬻������ػ����Total balance������1000�������
 *
 * @author ����ΰ
 *
 * �������ڣ�2013-6-7
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