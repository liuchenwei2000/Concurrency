/**
 * 
 */
package testing;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * ��� Phaser ����ʾ��
 * <p>
 * ��ʾ Phaser ���������ṩ��״̬��Ϣ�Լ���λ�ȡ���ǡ�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��26��
 */
public class MonitoringPhaserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Phaser phaser = new Phaser(3);
		
		for (int i = 0; i < phaser.getRegisteredParties(); i++) {
			new Thread(new Task(i + 1, phaser)).start();
		}
		
		for (int i = 0; i < 10; i++) {
			System.out.printf("********************\n");
			// ���� Phaser ����ĵ�ǰ�׶���
			System.out.printf("Main: Phaser: Phase: %d\n", phaser.getPhase());
			// ����ע�ᵽ Phaser �����е�������
			System.out.printf("Main: Phaser: Registered Parties: %d\n", phaser.getRegisteredParties());
			// �����ѽ�����ǰ�׶Σ��ȴ���һ�׶ο�ʼ����������
			System.out.printf("Main: Phaser: Arrived Parties: %d\n", phaser.getArrivedParties());
			// ������δ������ǰ�׶ε�������
			System.out.printf("Main: Phaser: Unarrived Parties: %d\n", phaser.getUnarrivedParties());
			System.out.printf("********************\n");
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * һ��ʹ�� Phaser ������ƽ׶�ͬ��������
	 */
	private static class Task implements Runnable {

		private int time;
		private Phaser phaser;
		
		public Task(int time, Phaser phaser) {
			this.time = time;
			this.phaser = phaser;
		}

		/**
		 * ÿ���׶�˯��ָ��time��������
		 */
		@Override
		public void run() {
			phaser.arrive();
			// phase 1
			System.out.printf("%s: Entering phase 1.\n", Thread.currentThread().getName());
			sleep();
			System.out.printf("%s: Finishing phase 1.\n", Thread.currentThread().getName());
			phaser.arriveAndAwaitAdvance();
			// phase 2
			System.out.printf("%s: Entering phase 2.\n", Thread.currentThread().getName());
			sleep();
			System.out.printf("%s: Finishing phase 2.\n", Thread.currentThread().getName());
			phaser.arriveAndAwaitAdvance();
			// phase 3
			System.out.printf("%s: Entering phase 3.\n", Thread.currentThread().getName());
			sleep();
			System.out.printf("%s: Finishing phase 3.\n", Thread.currentThread().getName());
			phaser.arriveAndDeregister();// ���һ���� Phaser ��ע��
		}
		
		private void sleep(){
			try {
				TimeUnit.SECONDS.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
