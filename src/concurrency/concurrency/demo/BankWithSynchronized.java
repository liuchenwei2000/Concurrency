/**
 * 
 */
package concurrency.demo;


/**
 * ʹ��synchronized�ؼ��ֿ��Ʋ���������
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2007-12-12
 */
public class BankWithSynchronized extends AbstractBank {

	public BankWithSynchronized(int accNumber, double initialBalance) {
		super(accNumber, initialBalance);
	}
	
	/**
	 * ת�˲���(ʹ��synchronized�ؼ���ʵ����)
	 */
	public synchronized void transfer(Account from, Account to, double amount) {
		if (from.getID().equals(to.getID()))
			return;
		while (from.getBalance() < amount) {
			try {
				// �����̷߳ŵ��ȴ����У��÷���ֻ����һ��ͬ���ķ����б�����
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		processAccount(from, to, amount);
		// ����ڸö����ϵ���wait�Ľ��̵�����״̬��ֻ����ͬ�������ڵ���
		notifyAll();
	}

	/**
	 * �������������
	 */
	protected synchronized double getTotalBalance() {
		return super.getTotalBalance();
	}
}