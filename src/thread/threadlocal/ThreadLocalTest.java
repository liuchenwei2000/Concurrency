/**
 * 
 */
package threadlocal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ThreadLocal(�ֲ߳̾�����)ʾ��
 * <p>
 * �����Ϊһ���������ʵ��ά��һ��������ʵ���������õ���̬���Ա������
 * ��������߳�Ϊ��λά��һ��������ʵ���������õ��ֲ߳̾�����ThreadLocal��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-30
 */
public class ThreadLocalTest {

	/**
	 * ���ݿ����ӹ���1
	 * <p>
	 * �����ʵ��ʹ�������̶߳�����ͬһ��Connection������ΪConnection�����Ƿ��̰߳�ȫ�ģ���������JVM����ĵ����ǲ��׵ġ�
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
	 * ���ݿ����ӹ���2
	 * <p>
	 * �����ʵ��ʹ��ÿһ���̶߳����Լ�ר����Connection���������̼߳���ĵ���ʹ�ö�Connection��ʹ�����̰߳�ȫ�ġ�
	 */
	static class ConnectionFactory2 {

		/**
		 * ʹ���ֲ߳̾��������ñ��������Ǿ�̬�ģ���֤ÿ���̶߳�ֻ��һ���Լ���Connection����
		 */
		private static ThreadLocal<Connection> connections = new ThreadLocal<Connection>() {

			/**
			 * ���ش��ֲ߳̾������ĵ�ǰ�̵߳�"��ʼֵ"��
			 * �̵߳�һ��ʹ�� get()�������ʱ���ʱ�����ô˷�����������߳�֮ǰ������ set(T)�������򲻻�Ը��߳��ٵ��� initialValue������
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
			// �����ֲ߳̾������е�ǰ�߳̿�ʹ�õ�ֵ
			return connections.get();
		}
	}

	/**
	 * ���ݿ����ӹ���3
	 * <p>
	 * ��ConnectionFactory2�Ĺ���һ�£�ֻ��ʹ������һ�ַ�ʽ��
	 */
	static class ConnectionFactory3 {

		/**
		 * ʹ���ֲ߳̾�����
		 */
		private static ThreadLocal<Connection> connections = new ThreadLocal<Connection>();

		public static Connection getConnection() throws SQLException {
			Connection connection = connections.get();
			if (connection == null) {
				// �����ֲ߳̾������е�ǰ�̵߳Ŀ�ʹ�õ�ֵ����Ϊָ��ֵ��
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