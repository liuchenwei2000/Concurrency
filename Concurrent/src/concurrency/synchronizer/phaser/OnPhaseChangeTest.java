/**
 * 
 */
package concurrency.synchronizer.phaser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * 控制Phaser中阶段切换的示例
 * <p>
 * 总共考试3场，只有考完上一场才能继续向下进行，每场考试都会需要发令后统一开始。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2015年1月14日
 */
public class OnPhaseChangeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 这里没有指明参与线程数
		MyPhaser phaser = new MyPhaser();
		
		Student[] students = new Student[3];
		students[0] = new Student("Tom", phaser);
		phaser.register();// 向 phaser 动态注册一个参与者
		students[1] = new Student("Ann", phaser);
		phaser.register();
		students[2] = new Student("Lucy", phaser);
		phaser.register();
		
		Thread[] threads = new Thread[students.length];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(students[i]);
			threads[i].start();
		}
		
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.printf("Main: The phaser has finished: %s.\n", phaser.isTerminated());
	}

	/**
	 * 自定义Phaser实现
	 * <p>
	 * Phaser类提供了一个onAdvance()方法，它会在每次阶段（phase）切换时————上一个阶段执行完毕即将进入下一个阶段前————触发执行。
	 * 通过实现自定义的Phaser类可以控制阶段切换，只要重写onAdvance()方法即可。
	 */
	private static class MyPhaser extends Phaser {

		/**
		 * 该方法会在阶段切换前和所有参与线程被唤醒前（因调用arriveAndAwaitAdvance()方法）被调用。
		 * 
		 * @param phase
		 *            表示当前阶段序号（基于0）
		 * @param registeredParties
		 *            表示注册的参与线程数。
		 * @return {@code true} 表示phaser对象会唤醒所有参与线程，但它本身马上进入 terminated 状态。</br>
		 *         {@code false} 表示phaser对象将会继续执行后面的阶段操作。
		 * @see java.util.concurrent.Phaser#onAdvance(int, int)
		 */
		@Override
		protected boolean onAdvance(int phase, int registeredParties) {
			switch (phase) {
			case 0:
				onStudentsArrived();
				return false;
			case 1:
				onExam1Finished();
				return false;
			case 2:
				onExam2Finished();
				return false;
			case 3:
				onExam3Finished();
				return true;//
			default:
				return true;
			}
		}

		private void onStudentsArrived() {
			System.out.printf("Phaser: The exam are going to start. The students are ready.\n");
			System.out.printf("Phaser: We have %d students.\n", getRegisteredParties());
		}

		private void onExam1Finished() {
			System.out.printf("Phaser: All the students have finished the first exam.\n");
			System.out.printf("Phaser: It's time for the second one.\n");
		}

		private void onExam2Finished() {
			System.out.printf("Phaser: All the students have finished the second exam.\n");
			System.out.printf("Phaser: It's time for the third one.\n");
		}

		private void onExam3Finished() {
			System.out.printf("Phaser: All the students have finished the exam.\n");
			System.out.printf("Phaser: Thank you for your time.\n");
		}
	}
	
	private static class Student implements Runnable {

		private String name;
		private Phaser phaser;
		
		public Student(String name, Phaser phaser) {
			this.name = name;
			this.phaser = phaser;
		}

		@Override
		public void run() {
			// step 0
			System.out.printf("%s%s: Has arrived to do the exam. \n", getDateTime(), name);
			phaser.arriveAndAwaitAdvance();
			// step 1
			System.out.printf("%s%s: Is going to do the first exam. \n", getDateTime(), name);
			doExam1();
			System.out.printf("%s%s: Has done the first exam. \n", getDateTime(), name);
			phaser.arriveAndAwaitAdvance();
			// step 2
			System.out.printf("%s%s: Is going to do the second exercise. \n", getDateTime(), name);
			doExam2();
			System.out.printf("%s%s: Has done the second exercise. \n", getDateTime(), name);
			phaser.arriveAndAwaitAdvance();
			// step 3
			System.out.printf("%s%s: Is going to do the third exercise. \n", getDateTime(), name);
			doExam3();
			System.out.printf("%s%s: Has finished the exam. \n", getDateTime(), name);
			phaser.arriveAndAwaitAdvance();
		}
		
		private String getDateTime(){
			return "【" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "】";
		}
		
		private void doExam1() {
			try {
				long duration = (long) (Math.random() * 10);
				TimeUnit.SECONDS.sleep(duration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		private void doExam2() {
			try {
				long duration = (long) (Math.random() * 10);
				TimeUnit.SECONDS.sleep(duration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		private void doExam3() {
			try {
				long duration = (long) (Math.random() * 10);
				TimeUnit.SECONDS.sleep(duration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
