/**
 * 
 */
package concurrency.collections.bank;

/**
 * ת������
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2014��8��3��
 */
public class TransferCommand {

	private int from;// ת���˻�
	private int to;// ת���˻�
	private double amount;// ת������

	public TransferCommand(int from, int to, double amount) {
		this.from = from;
		this.to = to;
		this.amount = amount;
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	public double getAmount() {
		return amount;
	}
}
