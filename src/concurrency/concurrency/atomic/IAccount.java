/**
 * 
 */
package concurrency.atomic;

/**
 * �����˻�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��20��
 */
public interface IAccount {

	/**
	 * ���˻���Ǯ
	 */
	public void addAmount(int amount);
	
	/**
	 * ���˻���Ǯ
	 */
	public void subtractAmount(int amount);
	
	/**
	 * �����˻����
	 */
	public int getBalance();
}
