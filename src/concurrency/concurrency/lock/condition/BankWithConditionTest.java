/**
 * 
 */
package concurrency.lock.condition;

import concurrency.demo.Bank;
import concurrency.demo.TransferTest;

/**
 * ʹ����������������Ʋ�����ת��
 * <p>
 * ������Զ�������Total balance������1000�������
 *
 * @author ����ΰ
 *
 * �������ڣ�2013-6-7
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