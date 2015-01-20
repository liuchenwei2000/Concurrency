/**
 * 
 */
package concurrency.atomic;

/**
 * ����
 * <p>
 * ���ǻ�һֱ��Ǯ���޸� IAccount ���������̡߳�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��20��
 */
public class Bank extends Thread {

	private IAccount account;

	public Bank(IAccount account) {
		this.account = account;
	}

	@Override
	public void run() {
		// ��Ǯ 10000 �Σ�ÿ�ο� 100
		for (int i = 0; i < 10000; i++) {
			account.subtractAmount(100);
		}
	}
}
