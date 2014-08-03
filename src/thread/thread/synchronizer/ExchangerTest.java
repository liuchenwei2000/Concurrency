/**
 * 
 */
package thread.synchronizer;

import java.util.Stack;
import java.util.concurrent.Exchanger;

/**
 * Exchanger示例
 * <p>
 * Exchanger提供了一个同步点，在这个同步点，一对线程可以交换数据。
 * 每个线程通过exchange()方法的参数提供数据给他的伙伴线程，并接收他的伙伴线程提供的数据，然后返回。
 * 当两个线程通过Exchanger交换了对象，这个交换对于两个线程来说都是安全的。 
 * 换句话说Exchanger提供的是一个交换服务，允许原子性的交换两个(多个)对象，但同时只有一对才会成功。
 * <p>
 * Exchanger类方便了两个共同操作线程之间的双向交换。
 * 这样，就像具有计数为 2的 CyclicBarrier，并且两个线程在都到达屏障时可以交换一些状态。
 * <p>
 * Exchanger通常用于一个线程填充缓冲，而另一个线程清空缓冲的情况。
 * 当两个线程在屏障处集合时，它们交换缓冲。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-26
 */
public class ExchangerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new App().start();
	}

	/**
	 * 该类使用Exchanger在线程间交换缓冲区，因此在需要时，
	 * 填充缓冲区的线程获取一个新腾空的缓冲区，并将填满的缓冲区传递给腾空缓冲区的线程。
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
		 * 填充缓冲区任务
		 */
		private class FillingLoop implements Runnable {

			@Override
			public void run() {
				try {
					DataBuffer currentBuffer = initEmptyBuffer;
					while (currentBuffer != null) {
						if (currentBuffer.isFull()) {
							/*
							 * 等待另一个线程到达此交换点，然后将给定的对象(本例DataBuffer)传给该线程，并接受该线程的对象。
							 * 如果还没有其他线程在交换点等待，则当前线程会处于休眠状态等待。
							 * 
							 * 该方法的参数是要交换的对象，返回的是另一个线程提供的对象。
							 */
							currentBuffer = exchanger.exchange(currentBuffer);
							System.out.println("FillingLoop：buffer is full. exchange an empty one.");
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
				System.out.println("FillingLoop：add " + d);
			}
		}
		
		/**
		 * 腾空缓冲区任务
		 */
		private class EmptyingLoop implements Runnable {

			@Override
			public void run() {
				try {
					DataBuffer currentBuffer = initFullBuffer;
					while (currentBuffer != null) {
						if (currentBuffer.isEmpty()) {
							currentBuffer = exchanger.exchange(currentBuffer);
							System.out.println("EmptyingLoop：buffer is empty. exchange a full one.");
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
				System.out.println("EmptyingLoop：take " + currentBuffer.take());
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