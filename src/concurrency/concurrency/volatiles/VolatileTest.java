/**
 * 
 */
package concurrency.volatiles;

/**
 * volatile�ؼ���
 * <p>
 * ��Java�ڴ�ģ���У������ڴ�main memory��ÿ���߳�Ҳ���Լ����ڴ滺����(��Ĵ���)��
 * Ϊ�����ܣ�һ���̻߳����Լ����ڴ滺�����б���Ҫ���ʵı����ĸ�����
 * �����ͻ���֣�
 * ��ĳ��˲�䣬ͬһ���������߳�A�ڴ滺�����е�ֵ�������߳�B�ڴ滺�����е�ֵ�����������ڴ��е�ֵ��һ�µ������ 
 * <p>
 * ʵ����������Ϊvolatile����ζ�������������ʱ�ᱻ�����߳��޸ĵģ���˲��ܽ����������߳��ڴ滺�����С�
 * <p>
 * volatileһ������²��ܴ���������ƣ���Ϊvolatile���ܱ�֤������ԭ���ԡ�
 * volatileֻ�ܱ�֤��ͬ�̲߳�����ʵ������ͬһ���ڴ棬����Ȼ���ܳ���д�������ݵ������
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2007-12-13
 */
public class VolatileTest {

	/**
	 * ʹ�������ƶ����ͬ�����ʽ��п���
	 */
	class UseLock {
		
		private boolean done;
		
		/**
		 * �����һ���߳��Ѿ��Զ�����������������ٱ������̵߳���ʱ��������
		 */
		public synchronized boolean isDone(){
			return done;
		}
		
		public synchronized void setDone(boolean done){
			this.done = done;
		}
	}

	/**
	 * ʹ��volatile�����ͬ�����ʽ��п���
	 */
	class UseVolatile {
		
		// ��������Ϊvolatile��������ᱻ��������/���ʣ���������������������в�������
		private volatile boolean done;
		
		/**
		 * ����һ��volatile�����ȷ���һ��һ�����Ҫ��������Ϊ�̰߳�ȫ�����Ĵ���
		 */
		public boolean isDone(){
			return done;
		}
		
		public void setDone(boolean done){
			this.done = done;
		}
	}
}