/**
 * 
 */
package concurrency.lock;

/**
 * �ͻ�������(Clinet-side locking)ʾ��
 * <p>
 * ʹ��һ�����������ʵ�ֶ����ԭ�Ӳ�������Ϊ�ͻ���������
 * ���ö���δ�ػ������ŵ˵�Լ������п��޸ķ�����������������ڲ������籾��Person2�ࡣ
 * ���ԣ��ͻ��������Ƿǳ������ģ����Ƽ�ʹ�á�
 * <p>
 * �����������߳�ͬ����ǿ�󹤾ߣ������ǲ����������ġ�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2014��7��26��
 */
@SuppressWarnings("unused")
public class ClientSideLockingTest {

	/**
	 * �÷�������� Person1���� setFirstName��setLastName����������ԭ�Ӳ�����
	 */
	synchronized static void setName1(Person1 p1, String firstName,
			String lastName) {
		/* 
		 * ��������˶�Person1���������ʵ������������ԭ�Ӳ�����
		 * ��ΪPerson1�����ڲ�ȷʵ��ʹ��������е������м����������������ַ�ʽ�ܹ��ﵽԭ�Ӳ�����Ŀ�ġ�
		 */
		synchronized (p1) {
			p1.setFirstName(firstName);
			p1.setLastName(lastName);
		}
	}

	/**
	 * �÷�������� Person2���� setFirstName��setLastName����������ԭ�Ӳ�����
	 */
	synchronized static void setName2(Person1 p2, String firstName,
			String lastName) {
		/* 
		 * ��������˶�Person2���������ʵ������������ԭ�Ӳ�����
		 * ��ΪPerson2�����ڲ�û��ʹ��������е������м������������ǲ���һ��ʵ��������������������ַ�ʽ���ܹ��ﵽԭ�Ӳ�����Ŀ�ġ�
		 */
		synchronized (p2) {
			p2.setFirstName(firstName);
			p2.setLastName(lastName);
		}
	}

	/**
	 * �����Ķ���ʹ������������е�����������������
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
	 * �����Ķ���ʹ����һ��ʵ��������е�����������������
	 */
	static class Person2 {

		// lock���󱻴���ֻ��Ϊ��ʹ������е��ڲ���
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
