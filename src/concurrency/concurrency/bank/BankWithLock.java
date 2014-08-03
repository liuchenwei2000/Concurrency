/**
 * 
 */
package concurrency.bank;

import java.util.concurrent.locks.ReentrantLock;


/**
 * 使用锁的银行
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2007-12-12
 */
public class BankWithLock extends AbstractBank {

	private ReentrantLock lock = new ReentrantLock();// 可重入对象锁

	public BankWithLock(int accNumber, double initialBalance) {
		super(accNumber, initialBalance);
	}
	
	/**
	 * 转账操作(带锁)
	 * 
	 * 带锁的操作一般用来模拟数据库中的原子性操作
	 */
	public void transfer(Account from, Account to, double amount) {
		if (from.getID().equals(to.getID()))
			return;
		/*
		 * 线程获取锁定 
		 * 
		 * 如果该锁定没有被另一个线程保持，则当前线程获取该锁定并立即返回，将锁定的保持计数设置为1。
		 * 如果当前线程已经保持该锁定，则将保持计数加 1，并且该方法立即返回 。
		 * 如果该锁定被另一个线程保持，则出于线程调度的目的，禁用当前线程，
		 * 并且在获得锁定之前，该线程将一直处于休眠状态，此时锁定保持计数被设置为1。
		 */
		lock.lock();
		try {
			processAccount(from, to, amount);
		} finally {
			lock.unlock();// 解锁
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