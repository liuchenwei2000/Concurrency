/**
 * 
 */
package concurrency.atomic;

/**
 * 公司
 * <p>
 * 这是会一直打钱（修改 IAccount 变量）的线程。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月20日
 */
public class Company extends Thread {

	private IAccount account;

	public Company(IAccount account) {
		this.account = account;
	}

	@Override
	public void run() {
		// 打钱 10000 次，每次打 100
		for (int i = 0; i < 10000; i++) {
			account.addAmount(100);
		}
	}
}
