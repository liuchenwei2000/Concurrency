/**
 * 
 */
package concurrency.bank;

/**
 * �����˻�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2007-12-12
 */
public class Account {

	private String id;// �˻�ID
	private double balance;// �˻����

	public Account(String id, double balance) {
		this.id = id;
		this.balance = balance;
	}

	public String getID() {
		return id;
	}

	public double getBalance() {
		return balance;
	}
	
	/**
	 * ��Ǯ����ת�����
	 * 
	 * @param amount
	 *            ת����
	 */
	public void in(double amount) {
		balance += amount;
	}

	/**
	 * ȡǮ����ת������
	 * 
	 * @param amount
	 *            ת�����
	 */
	public void out(double amount) {
		balance -= amount;
	}
}