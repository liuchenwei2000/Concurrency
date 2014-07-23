/**
 * 
 */
package thread;

/**
 * �̵߳�APIӦ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2007-8-28
 */
public class Others {
	
	/**
	 * @param args
	 */
	public static void main(String args[]) {
		Thread dt = new Thread(new APITest());
		
		/** 
		 * �ػ��̵߳�Ψһ���þ���Ϊ�����߳��ṩ���񣬵�ֻʣ���ػ��߳�ʱ��JVM�ͻ��˳�����ʱ���߳̾���һ�����ӡ�
		 * ���߳���Ϊ�ػ��̣߳��������߳�����ǰ���á�
		 * 
		 * �ػ��߳�Ӧ����Զ��ȥ���ʹ�����Դ�����ļ������ݿ�ȣ���Ϊ�������κ�ʱ�����жϡ�
		 */
		dt.setDaemon(true);
		dt.start();
	}
}

class APITest implements Runnable {
	
	@Override
	public void run() {
		try {
			/** 
			 * ��Ȼ�����Ѿ�û��ǿ����ֹ�̵߳ķ�����������ͨ��interrupt������������ֹһ���߳� 
			 */
			Thread.currentThread().interrupt();
			
			/** 
			 * Ϊ�˲����ж�״̬�Ƿ���λ����Ҫ�����̵߳�isInterrupted������
			 * �÷���û�и����ã��̵߳��ж�״̬���ܸ÷�����Ӱ�졣
			 */
			if (Thread.currentThread().isInterrupted()) {
				System.out.println("Thread after isInterrupted()");
			}
			
			/** 
			 * �ж��߳��Ƿ��ѱ��жϣ��÷����и����ã��̵߳��ж�״̬�ɸ÷��������
			 */
			if (Thread.interrupted()) {
				System.out.println("Thread after interrupted()");
			}
			if (Thread.interrupted()) {
				System.out.println("Thread after interrupted() again");
			} else {
				/** 
				 * isAlive()�����������ж��߳��ڵ�ǰ�Ƿ����ţ������л���������
				 */
				if (Thread.currentThread().isAlive()) {
					System.out.println("Thread is alive!");
				}
				Thread.sleep(2000);
				System.out.println("Thread is dead!");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}