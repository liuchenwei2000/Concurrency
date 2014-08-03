/**
 * 
 */
package concurrency.bank;

/**
 * �����Ʋ�����������
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2007-12-12
 */
public class UnsynBank extends AbstractBank {

	public UnsynBank(int accNumber, double initialBalance) {
		super(accNumber, initialBalance);
	}

	/**
	 * ת�˲���
	 * 
	 * @param from
	 *            ת���˻�
	 * @param to
	 *            ת���˻�
	 * @param amount
	 *            ת�˽��
	 */
	public void transfer(Account from, Account to, double amount) {
		if (from.getID().equals(to.getID())) {
			return;
		}
		processAccount(from, to, amount);
	}
}