/**
 * 
 */
package thread.synchronizer;

import java.util.concurrent.SynchronousQueue;

/**
 * SynchronousQueueʾ��
 * <p>
 * ͬ��������һ�ֽ������ߺ��������߳���ԵĻ��ơ�
 * ��һ���̵߳���SynchronousQueue�ϵ�put����ʱ����������ֱ����һ���̵߳���take����Ϊֹ����֮��Ȼ��
 * ��Exchanger��ͬ��ͬ�����е�����ֻ��һ�������ϴ��ݣ��������ߵ������ߡ�
 * <p>
 * SynchronousQueue������ һ���������У�����ÿ��put����ȴ�һ��take����֮��Ȼ��
 * ͬ������û���κ��ڲ�������������һ�����е�������û�С�
 * ������ͬ�������Ͻ��� peek����Ϊ������ͼҪȡ��Ԫ��ʱ����Ԫ�زŴ��ڣ�
 * ������һ���߳���ͼ�Ƴ�ĳ��Ԫ�أ�����Ҳ���ܣ�ʹ���κη��������Ԫ�أ�
 * Ҳ���ܵ������У���Ϊ����û��Ԫ�ؿ����ڵ�����
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-27
 */
public class SynchronousQueueTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new App().start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static class App {
		
		private SynchronousQueue<String> squeue = new SynchronousQueue<String>();
		
		public void start(){
			new Thread(new Worker()).start();
			new Thread(new Consumer()).start();
		}
		
		/**
		 * ������
		 */
		private class Worker implements Runnable {

			@Override
			public void run() {
				while (true) {
					try {
						double value = Math.random() * 1000;
						Thread.sleep((long) value);
						System.out.println("worker��put " + value);
						/*
						 * put��ָ��Ԫ����ӵ��˶��У����б�Ҫ��ȴ���һ���߳̽�������
						 */
						squeue.put(value + "");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		/**
		 * ������
		 */
		private class Consumer implements Runnable {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep((long) (Math.random() * 1000));
						/*
						 * take��ȡ���Ƴ��˶��е�ͷ�����б�Ҫ��ȴ���һ���̲߳�������
						 */
						System.out.println("consumer��take " + squeue.take());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}