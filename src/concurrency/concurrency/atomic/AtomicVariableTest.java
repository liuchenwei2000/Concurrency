/**
 * 
 */
package concurrency.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 原子变量示例
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月20日
 */
public class AtomicVariableTest {

	/**
	 * 一个线程不断地对IAccount中的余额执行加100操作，另一个线程则执行减100操作，各自执行10000次。
	 * 最后看IAccount 中的余额是否是初始值1000，如果是则证明原子变量 AtomicInteger 的有效性。
	 */
	public static void main(String[] args) {
		System.out.println("【Test NormalVariableAccount】");
		test(new NormalVariableAccount(1000));

		System.out.println("\n【Test AtomicVariableAccount】");
		test(new AtomicVariableAccount(1000));
	}

	private static void test(IAccount account) {
		System.out.println("Account Initial balance：" + account.getBalance());

		Thread company = new Company(account);
		Thread bank = new Bank(account);

		company.start();
		bank.start();

		try {
			company.join();
			bank.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Account Final balance：" + account.getBalance());
	}

	/**
	 * 使用常规变量实现的Account，不能保证数据一致性。
	 */
	private static class NormalVariableAccount implements IAccount {

		// 普通的 int 型变量
		private int balance;

		public NormalVariableAccount(int balance) {
			this.balance = balance;
		}

		public int getBalance() {
			return balance;
		}

		public void addAmount(int amount) {
			// 这步操作被编译时会产生好几条指令
			this.balance += amount;
		}

		public void subtractAmount(int amount) {
			this.balance -= amount;
		}
	}

	/**
	 * 使用原子变量实现的Account，能够保证数据一致性。
	 */
	private static class AtomicVariableAccount implements IAccount {

		// 使用原子变量，另外还有 AtomicLong、AtomicBoolean、AtomicReference等原子变量实现
		private AtomicInteger balance;

		public AtomicVariableAccount(int balance) {
			// 使用 int 值初始化一个 AtomicInteger
			this.balance = new AtomicInteger(balance);
		}

		public int getBalance() {
			return balance.get();// 返回原子变量中的值
		}

		public void addAmount(int amount) {
			// 以原子方式将给定值与当前值相加并返回更新的值。 
			this.balance.addAndGet(amount);
			// 下面的方法也都保证操作的原子性
//			this.balance.getAndAdd(amount);// 以原子方式将给定值与当前值相加并返回以前的值 
//			this.balance.incrementAndGet();// 以原子方式将当前值加 1
//			this.balance.decrementAndGet();// 以原子方式将当前值减 1
//			this.balance.getAndSet(1);// 以原子方式设置为给定值，并返回旧值
		}

		public void subtractAmount(int amount) {
			this.balance.addAndGet(-amount);
		}
	}
}