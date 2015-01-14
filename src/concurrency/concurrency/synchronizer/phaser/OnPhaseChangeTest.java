/**
 * 
 */
package concurrency.synchronizer.phaser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * ����Phaser�н׶��л���ʾ��
 * <p>
 * �ܹ�����3����ֻ�п�����һ�����ܼ������½��У�ÿ�����Զ�����Ҫ�����ͳһ��ʼ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��14��
 */
public class OnPhaseChangeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ����û��ָ�������߳���
		MyPhaser phaser = new MyPhaser();
		
		Student[] students = new Student[3];
		students[0] = new Student("Tom", phaser);
		phaser.register();// �� phaser ��̬ע��һ��������
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
	 * �Զ���Phaserʵ��
	 * <p>
	 * Phaser���ṩ��һ��onAdvance()������������ÿ�ν׶Σ�phase���л�ʱ����������һ���׶�ִ����ϼ���������һ���׶�ǰ������������ִ�С�
	 * ͨ��ʵ���Զ����Phaser����Կ��ƽ׶��л���ֻҪ��дonAdvance()�������ɡ�
	 */
	private static class MyPhaser extends Phaser {

		/**
		 * �÷������ڽ׶��л�ǰ�����в����̱߳�����ǰ�������arriveAndAwaitAdvance()�����������á�
		 * 
		 * @param phase
		 *            ��ʾ��ǰ�׶���ţ�����0��
		 * @param registeredParties
		 *            ��ʾע��Ĳ����߳�����
		 * @return {@code true} ��ʾphaser����ỽ�����в����̣߳������������Ͻ��� terminated ״̬��</br>
		 *         {@code false} ��ʾphaser���󽫻����ִ�к���Ľ׶β�����
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
			return "��" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "��";
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
