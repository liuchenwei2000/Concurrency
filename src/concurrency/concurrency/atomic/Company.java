/**
 * 
 */
package concurrency.atomic;

/**
 * ��˾
 * <p>
 * ���ǻ�һֱ��Ǯ���޸� IAccount ���������̡߳�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��20��
 */
public class Company extends Thread {

	private IAccount account;

	public Company(IAccount account) {
		this.account = account;
	}

	@Override
	public void run() {
		// ��Ǯ 10000 �Σ�ÿ�δ� 100
		for (int i = 0; i < 10000; i++) {
			account.addAmount(100);
		}
	}
}
