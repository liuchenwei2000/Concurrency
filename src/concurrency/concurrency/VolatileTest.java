/**
 * 
 */
package concurrency;

/**
 * volatile�ؼ���
 * <p>
 * volatile�ؼ���Ϊһ������ʵ�������ͬ�������ṩ��һ���������ơ�
 * <p>
 * ���������������£���һ����Ĳ��������ǰ�ȫ�ģ�
 * <li>����volatile���εġ�
 * <li>����ķ�������������
 * <li>����final�ģ������ڹ�����������ɺ󱻷��ʡ�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2007-12-13
 */
public class VolatileTest {

	/**
	 * ʹ�������ƶ����ͬ�����ʽ��п���
	 * <p>
	 * ����ʹ��synchronized�ؼ��֣���ʹ��Lock���������һ����Ч��
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