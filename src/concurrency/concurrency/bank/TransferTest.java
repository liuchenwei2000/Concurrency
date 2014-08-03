/**
 * 
 */
package concurrency.bank;

import java.util.Random;

/**
 * ����ת�˲�����
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2007-12-12
 */
public class TransferTest {
	
	private static final Random RANDOM = new Random();
	
	public static void run(Bank bank) {
		Account[] accounts = bank.getAccounts();
		for (int i = 0; i < accounts.length; i++) {
			/*
			 * ÿһ���˻���������һ���߳��������˻�ת��
			 * 
			 * ����ͬ�˻���ͬһ���˻�ת��ʱ������ͬһ���˻��Ȳ���ת���ֲ���ת��ʱ�����п��ܷ�����ͻ
			 */
			TransferThread t = new TransferThread(bank, accounts[i], RANDOM.nextInt(100));
			t.start();
		}
	}
}