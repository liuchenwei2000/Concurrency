/**
 * 
 */
package concurrency.demo.test;

import concurrency.demo.Bank;
import concurrency.demo.TransferTest;

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