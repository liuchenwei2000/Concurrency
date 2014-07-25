/**
 * 
 */
package concurrency.demo;


/**
 * 使用synchronized关键字控制并发的银行
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2007-12-12
 */
public class BankWithSynchronized extends AbstractBank {

	public BankWithSynchronized(int accNumber, double initialBalance) {
		super(accNumber, initialBalance);
	}
	
	/**
	 * 转账操作(使用synchronized关键字实现锁)
	 */
	public synchronized void transfer(Account from, Account to, double amount) {
		if (from.getID().equals(to.getID()))
			return;
		while (from.getBalance() < amount) {
			try {
				// 将该线程放到等待集中，该方法只能在一个同步的方法中被调用
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		processAccount(from, to, amount);
		// 解除在该对象上调用wait的进程的阻塞状态，只能在同步方法内调用
		notifyAll();
	}

	/**
	 * 返回银行总余额
	 */
	protected synchronized double getTotalBalance() {
		return super.getTotalBalance();
	}
}