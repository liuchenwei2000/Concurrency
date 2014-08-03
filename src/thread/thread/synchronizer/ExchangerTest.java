/**
 * 
 */
package thread.synchronizer;

import java.util.Stack;
import java.util.concurrent.Exchanger;

/**
 * Exchangerʾ��
 * <p>
 * Exchanger�ṩ��һ��ͬ���㣬�����ͬ���㣬һ���߳̿��Խ������ݡ�
 * ÿ���߳�ͨ��exchange()�����Ĳ����ṩ���ݸ����Ļ���̣߳����������Ļ���߳��ṩ�����ݣ�Ȼ�󷵻ء�
 * �������߳�ͨ��Exchanger�����˶�������������������߳���˵���ǰ�ȫ�ġ� 
 * ���仰˵Exchanger�ṩ����һ��������������ԭ���ԵĽ�������(���)���󣬵�ͬʱֻ��һ�ԲŻ�ɹ���
 * <p>
 * Exchanger�෽����������ͬ�����߳�֮���˫�򽻻���
 * ������������м���Ϊ 2�� CyclicBarrier�����������߳��ڶ���������ʱ���Խ���һЩ״̬��
 * <p>
 * Exchangerͨ������һ���߳���仺�壬����һ���߳���ջ���������
 * �������߳������ϴ�����ʱ�����ǽ������塣
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-26
 */
public class ExchangerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new App().start();
	}

	/**
	 * ����ʹ��Exchanger���̼߳佻�����������������Ҫʱ��
	 * ��仺�������̻߳�ȡһ�����ڿյĻ����������������Ļ��������ݸ��ڿջ��������̡߳�
	 */
	private static class App {
		
		private Exchanger<DataBuffer> exchanger = new Exchanger<DataBuffer>();
		
		private DataBuffer initEmptyBuffer = new DataBuffer();
		private DataBuffer initFullBuffer = new DataBuffer();
		
		public void start(){
			new Thread(new FillingLoop()).start();
			new Thread(new EmptyingLoop()).start();
		}
		
		/**
		 * ��仺��������
		 */
		private class FillingLoop implements Runnable {

			@Override
			public void run() {
				try {
					DataBuffer currentBuffer = initEmptyBuffer;
					while (currentBuffer != null) {
						if (currentBuffer.isFull()) {
							/*
							 * �ȴ���һ���̵߳���˽����㣬Ȼ�󽫸����Ķ���(����DataBuffer)�������̣߳������ܸ��̵߳Ķ���
							 * �����û�������߳��ڽ�����ȴ�����ǰ�̻߳ᴦ������״̬�ȴ���
							 * 
							 * �÷����Ĳ�����Ҫ�����Ķ��󣬷��ص�����һ���߳��ṩ�Ķ���
							 */
							currentBuffer = exchanger.exchange(currentBuffer);
							System.out.println("FillingLoop��buffer is full. exchange an empty one.");
						}
						addToBuffer(currentBuffer);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			private void addToBuffer(DataBuffer currentBuffer) throws InterruptedException {
				double d = Math.random() * 1000;
				Thread.sleep((long) d);
				currentBuffer.add(d + "");
				System.out.println("FillingLoop��add " + d);
			}
		}
		
		/**
		 * �ڿջ���������
		 */
		private class EmptyingLoop implements Runnable {

			@Override
			public void run() {
				try {
					DataBuffer currentBuffer = initFullBuffer;
					while (currentBuffer != null) {
						if (currentBuffer.isEmpty()) {
							currentBuffer = exchanger.exchange(currentBuffer);
							System.out.println("EmptyingLoop��buffer is empty. exchange a full one.");
						}
						takeFromBuffer(currentBuffer);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			private void takeFromBuffer(DataBuffer currentBuffer) throws InterruptedException {
				double d = Math.random() * 1000;
				Thread.sleep((long) d);
				System.out.println("EmptyingLoop��take " + currentBuffer.take());
			}
		}
		
		private static class DataBuffer {

			private Stack<String> buffer = new Stack<String>();

			public void add(String data) {
				buffer.push(data);
			}

			public String take() {
				return buffer.pop();
			}

			public boolean isEmpty() {
				return buffer.size() == 0;
			}

			public boolean isFull() {
				return buffer.size() >= 3;
			}
		}
	}
}