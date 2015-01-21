/**
 * 
 */
package concurrency.collections.bank;

/**
 * 转账命令
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年8月3日
 */
public class TransferCommand {

	private int from;// 转出账户
	private int to;// 转入账户
	private double amount;// 转账数量

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
