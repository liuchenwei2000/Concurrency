/**
 * 
 */
package concurrency.atomic;

/**
 * 银行
 * <p>
 * 这是会一直扣钱（修改 IAccount 变量）的线程。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月20日
 */
public class Bank extends Thread {

	private IAccount account;

	public Bank(IAccount account) {
		this.account = account;
	}

	@Override
	public void run() {
		// 扣钱 10000 次，每次扣 100
		for (int i = 0; i < 10000; i++) {
			account.subtractAmount(100);
		}
	}
}
