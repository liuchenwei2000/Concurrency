/**
 * 
 */
package concurrency.bank;

import java.util.Random;

/**
 * 银行转账测试器
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2007-12-12
 */
public class TransferTest {
	
	private static final Random RANDOM = new Random();
	
	public static void run(Bank bank) {
		Account[] accounts = bank.getAccounts();
		for (int i = 0; i < accounts.length; i++) {
			/*
			 * 每一个账户都会启动一个线程向其他账户转账
			 * 
			 * 当不同账户向同一个账户转账时，或者同一个账户既参与转出又参与转入时，就有可能发生冲突
			 */
			TransferThread t = new TransferThread(bank, accounts[i], RANDOM.nextInt(100));
			t.start();
		}
	}
}