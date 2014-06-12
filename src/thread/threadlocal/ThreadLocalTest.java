/**
 * 
 */
package threadlocal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ThreadLocal(线程局部变量)示例
 * <p>
 * 如果想为一个类的所有实例维持一个变量的实例，将会用到静态类成员变量。
 * 如果想以线程为单位维持一个变量的实例，将会用到线程局部变量ThreadLocal。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-30
 */
public class ThreadLocalTest {

	/**
	 * 数据库连接工厂1
	 * <p>
	 * 本类的实现使得所有线程都共享同一个Connection对象，因为Connection本身是非线程安全的，所以这种JVM级别的单例是不妥的。
	 */
	static class ConnectionFactory1 {

		private static final Connection INSTANCE = createConnection();

		public static Connection getConnection() {
			return INSTANCE;
		}

		private static Connection createConnection() {
			try {
				return DriverManager
						.getConnection("jdbc:mysql://localhost:3306/db1",
								"user1", "password1");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * 数据库连接工厂2
	 * <p>
	 * 本类的实现使得每一个线程都有自己专属的Connection对象，这种线程级别的单例使得对Connection的使用是线程安全的。
	 */
	static class ConnectionFactory2 {

		/**
		 * 使用线程局部变量，该变量往往是静态的，保证每个线程都只有一个自己的Connection对象
		 */
		private static ThreadLocal<Connection> connections = new ThreadLocal<Connection>() {

			/**
			 * 返回此线程局部变量的当前线程的"初始值"。
			 * 线程第一次使用 get()方法访问变量时将调用此方法，但如果线程之前调用了 set(T)方法，则不会对该线程再调用 initialValue方法。
			 * 
			 * @see java.lang.ThreadLocal#initialValue()
			 */
			@Override
			protected Connection initialValue() {
				try {
					return DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/db1", "user1",
							"password1");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}
		};

		public static Connection getConnection() {
			// 返回线程局部变量中当前线程可使用的值
			return connections.get();
		}
	}

	/**
	 * 数据库连接工厂3
	 * <p>
	 * 和ConnectionFactory2的功能一致，只是使用了另一种方式。
	 */
	static class ConnectionFactory3 {

		/**
		 * 使用线程局部变量
		 */
		private static ThreadLocal<Connection> connections = new ThreadLocal<Connection>();

		public static Connection getConnection() throws SQLException {
			Connection connection = connections.get();
			if (connection == null) {
				// 将此线程局部变量中当前线程的可使用的值设置为指定值。
				connections.set(createConnection());
				connection = connections.get();
			}
			return connection;
		}

		private static Connection createConnection() throws SQLException {
			return DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/db1", "user1", "password1");
		}
	}
}