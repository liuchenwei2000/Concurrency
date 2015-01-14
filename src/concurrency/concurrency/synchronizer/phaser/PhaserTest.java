/**
 * 
 */
package concurrency.synchronizer.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * Phaserʾ��
 * <p>
 * ��һЩ����������Էֽ�ɼ������裨step��ʱ��Phaser �������á�
 * Phaser �ṩ������һ�ֻ��ƣ���������ִ�в���������߳���ÿ�����裨step���Ľ����㱣��ͬ����
 * Ҳ����˵ֱ�����е��̶߳�ִ������һ����step���������̲߳ŻῪʼִ����һ��������
 * <p>
 * �����ڴ��� Phaser ����ʱָ������ͬ���������߳�������Ҳ���Զ�̬�����ӻ���������Ŀ��
 * �����Ҳ��ʾ Phaser�������л����½׶β���ǰ���������߳���Ҫ����arriveAndAwaitAdvance()�����Ĵ�����
 * ���ô���һ���ﵽ�� Phaser�����������������ߵȴ����̣߳�������һ�׶β�����ִ�С�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2015��1��9��
 */
public class PhaserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 5��������
		Phaser phaser = new Phaser(5);
		// ��ȡ Phaser ��ע����߳���
		Thread[] threads = new Thread[phaser.getRegisteredParties()];
		
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new StepByStepTask("Task_" + i, phaser));
			threads[i].start();
		}
		
		try {
			for (Thread thread : threads) {
				thread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/* 
		 * �� Phaser ��û�в������߳�ʱ�����ͽ���Termination״̬������ķ����᷵��true��
		 * �����״̬�£��� Phaser ������κ�ͬ���������� awaitAdvance()��arriveAndAwaitAdvance()��
		 * ���ö����������أ�����ֵ��һ����������Termination״̬��᷵��һ���������������κ�ͬ��������
		 */
		System.out.println("Terminated: "+ phaser.isTerminated());
		// ǿ�� Phaser���󣨶����ڸ������ߣ����� Termination״̬������ĳ�������߳��ִ��������
//		phaser.forceTermination();
	}

	/**
	 * �ಽ�������
	 */
	private static class StepByStepTask implements Runnable {

		private String name;
		private Phaser phaser;
		
		public StepByStepTask(String name, Phaser phaser) {
			this.name = name;
			this.phaser = phaser;
		}

		@Override
		public void run() {
			/*
			 * ��ĳ���̵߳���arriveAndAwaitAdvance()����ʱ��Phaser ����Ὣ�˽׶β����̵߳�������һ��
			 * Ȼ����߳̽�����ֱ���������������߳��������׶ι�����
			 */
			// �����ʾ���е��̻߳�ͬʱ��ʼִ��
			phaser.arriveAndAwaitAdvance();
			step1();
			// �����ʾ���е��߳����step1()֮�󣬲Ż����ִ������Ĳ���
			phaser.arriveAndAwaitAdvance();
			boolean result = step2();
			if (result) {
				System.out.println(name + "��no result after step " + phaser.getPhase());
				/*
				 * ��ĳ���̵߳���arriveAndDeregister()����ʱ��Phaser ����Ὣ�˽׶β����̵߳�������һ��
				 * ���ʾ�߳��Ѿ�����˸ý׶β��������ǲ��ز���ʣ�½׶εĲ��������� Phaser �����ڼ�������ִ��ǰҲ�����ٵȴ����̡߳�
				 */
				// �򵥵�˵���൱���� Phaser ��ע�����Լ�����̬������ Phaser �е��߳�����
				phaser.arriveAndDeregister();
				System.out.println(name + "��I have gone. " + phaser.getRegisteredParties() + " threads left.");
				return;
			} 
			phaser.arriveAndAwaitAdvance();
			step3();
			phaser.arriveAndDeregister();
			System.out.println(name + "��Work completed.");
		}

		private void step1() {
			try {
				TimeUnit.MILLISECONDS.sleep((long) (Math.random()*1000));
				System.out.println(name + "��step1 complete.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		private boolean step2() {
			long ms = (long) (Math.random()*1000);
			try {
				TimeUnit.MILLISECONDS.sleep(ms);
				System.out.println(name + "��step2 complete.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return ms > 500;
		}

		private void step3() {
			try {
				TimeUnit.MILLISECONDS.sleep((long) (Math.random()*1000));
				System.out.println(name + "��step3 complete.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
