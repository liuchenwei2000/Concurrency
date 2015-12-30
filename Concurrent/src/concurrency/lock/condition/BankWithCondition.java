/**
 * 
 */
package concurrency.lock.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import concurrency.bank.AbstractBank;
import concurrency.bank.Account;

/**
 * 使用锁和条件对象的银行
 * <p>
 * 一个线程进入临界区，却发现它必须等待某个条件满足后才能执行。
 * 此时需要使用一个条件对象(condition)来管理那些已获得了锁却不能开始执行工作的线程。
 * 条件对象允许线程能够掌控锁并检查某种条件是否满足，如果不满足的话，线程将被挂起直到其他线程唤醒它。
 * Condition 提供了一种挂起线程和唤醒挂起线程的机制。
 * <p>
 * 本例中，我们希望账户余额不足时不能立即转账，只能等到最后余额才可以转账。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2007-12-12
 */
public class BankWithCondition extends AbstractBank {

	private ReentrantLock lock = new ReentrantLock();// 可重入锁

	private Condition enoughBalance;// 条件对象

	public BankWithCondition(int accNumber, double initialBalance) {
		super(accNumber, initialBalance);
		// 一个锁对象可以有一个或者多个相关联的条件对象
		this.enoughBalance = lock.newCondition();
	}
	
	/**
	 * 转账操作(带锁和条件对象)
	 * <p>
	 * 如果账户中没有足够的余额，则线程将等待，直到其他线程向次账户存款为止。
	 * 但这个线程刚获得了账户的锁，这种访问是排他性的、互斥的，即其他任何线程都没有机会进行存款操作。
	 * 这就是为什么要使用条件对象的原因。
	 */
	public void transfer(Account from, Account to, double amount){
		if (from.getID().equals(to.getID()))
			return;
		lock.lock();
		try {
			// 下面的while语句块是调用await时典型的方式，要在循环中不断检查条件是否满足
			while (from.getBalance() < amount) {
				try {
					/*
					 * 条件对象的await方法一旦被调用，当前线程就会被阻塞，并且释放锁。
					 * 
					 * 等待获得锁的线程和调用await的线程之间有一个本质的差别：
					 * 一旦一个线程调用了await方法，它就进入了等待该条件集中，当锁可获得时，线程不能立即解除阻塞。
					 * 它维持阻塞状态直到另一个线程调用同一个条件对象上的singalAll方法时为止，
					 * 又或者超过了等待时限（timeout），又或者线程被中断（interrupted）。
					 */
					enoughBalance.await();
					// 条件可以像下面那样设定一个等待时限
//					enoughBalance.await(100, TimeUnit.MILLISECONDS);// 100毫秒
					// 线程只能被唤醒而不能被中断
//					enoughBalance.awaitUninterruptibly();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			processAccount(from, to, amount);
			/*
			 * 条件对象的signalAll方法解除所有等待此条件的线程的阻塞状态。
			 * 
			 * 当线程从等待集中被移走时，它们将再次成为可运行的，线程调度器将再次激活它们。
			 * 此时，它们将试图重新获得锁。一旦锁可获得，它们中的某个线程将从await调用返回，获得锁并从它被阻塞的地方继续执行。
			 * 之后线程应该再次测试条件，因为现在还不能确保条件已经满足。
			 * signalAll方法仅仅是通知等待的线程：现在条件可能满足了，你值得再去检测一下。
			 */
			enoughBalance.signalAll();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 返回银行总余额
	 */
	protected double getTotalBalance() {
		double balance = 0;
		lock.lock();
		try {
			balance = super.getTotalBalance();
		} finally {
			lock.unlock();
		}
		return balance;
	}
}
