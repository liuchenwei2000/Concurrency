/**
 * 
 */
package concurrency.lock;

/**
 * 客户端锁定(Clinet-side locking)示例
 * <p>
 * 使用一个对象的锁来实现额外的原子操作，称为客户端锁定。
 * 而该对象未必会给出承诺说自己的所有可修改方法都是用其自身的内部锁，如本例Person2类。
 * 所以，客户端锁定是非常脆弱的，不推荐使用。
 * <p>
 * 锁和条件是线程同步的强大工具，但它们不是面向对象的。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年7月26日
 */
@SuppressWarnings("unused")
public class ClientSideLockingTest {

	/**
	 * 该方法想控制 Person1对象 setFirstName、setLastName两个方法的原子操作。
	 */
	synchronized static void setName1(Person1 p1, String firstName,
			String lastName) {
		/* 
		 * 这里采用了对Person1对象加锁来实现两个方法的原子操作。
		 * 因为Person1对象内部确实是使用自身持有的锁进行加锁操作，所以这种方式能够达到原子操作的目的。
		 */
		synchronized (p1) {
			p1.setFirstName(firstName);
			p1.setLastName(lastName);
		}
	}

	/**
	 * 该方法想控制 Person2对象 setFirstName、setLastName两个方法的原子操作。
	 */
	synchronized static void setName2(Person1 p2, String firstName,
			String lastName) {
		/* 
		 * 这里采用了对Person2对象加锁来实现两个方法的原子操作。
		 * 因为Person2对象内部没有使用自身持有的锁进行加锁操作（而是采用一个实例对象的锁），所以这种方式不能够达到原子操作的目的。
		 */
		synchronized (p2) {
			p2.setFirstName(firstName);
			p2.setLastName(lastName);
		}
	}

	/**
	 * 这个类的对象使用了其自身持有的锁进行锁定操作。
	 */
	static class Person1 {

		private String firstName;
		private String lastName;

		public synchronized void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public void setLastName(String lastName) {
			synchronized (this) {
				this.lastName = lastName;
			}
		}
	}

	/**
	 * 这个类的对象使用了一个实例对象持有的锁进行锁定操作。
	 */
	static class Person2 {

		// lock对象被创建只是为了使用其持有的内部锁
		private Object lock = new Object();

		private String firstName;
		private String lastName;

		public void setFirstName(String firstName) {
			synchronized (lock) {
				this.firstName = firstName;
			}
		}

		public void setLastName(String lastName) {
			synchronized (lock) {
				this.lastName = lastName;
			}
		}
	}
}
