/**
 * 
 */
package concurrency;

/**
 * 在synchronized块中使用条件示例
 * <p>
 * Object类中的wait()、notify()、notifyAll()方法被用来解决同步块中的条件问题。
 * 上述三个方法只能在同步块中调用，否则会抛出异常。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月7日
 */
public class SynBlockWithConditionTest {
	
	private static final int MAX_BALANCE = 1000;
	private static final int MIN_BALANCE = 10;

	private int balance;

	public SynBlockWithConditionTest(int balance) {
		this.balance = balance;
	}
	
	public void add(int amount) {
		synchronized (this) {
			// 并发时条件判断的典型代码结构
			while (balance + amount > MAX_BALANCE) {
				try {
					// JVM会将当前线程挂起（直到超时或被唤醒）并释放它持有的对象锁，从而允许其他线程执行该对象的同步块代码。
					// 当线程被唤醒时（其他线程调用了notify()或notifyAll()方法时），它会先争取获得对象锁，然后再检查条件是否满足。
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			balance += amount;
			// 唤醒其他所有线程，notify()方法会随机唤醒某一个线程
			notifyAll();
		}
	}

	public void sub(int amount) {
		synchronized (this) {
			while (balance - amount < MIN_BALANCE) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			balance -= amount;
			notifyAll();
		}
	}
}
