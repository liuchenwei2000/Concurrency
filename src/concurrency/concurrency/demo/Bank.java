/**
 * 
 */
package concurrency.demo;

/**
 * ���нӿ�(ģ��ת��)
 *
 * @author ����ΰ
 *
 * �������ڣ�2013-6-7
 */
public interface Bank {

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
	public void transfer(Account from, Account to, double amount);
	
	/**
	 * ���������˻���Ϣ
	 */
	public Account[] getAccounts();
}