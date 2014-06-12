/**
 * 
 */
package thread.timer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * util���Ķ�ʱ��(timer)��ʾ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2010-6-10
 */
public class UtilityTimerDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("About to schedule task.(5s)");
		new Reminder(5);// 5���ִ��
		System.out.println("Task scheduled.");
	}
}

class Reminder {

	// ��ʱ��
	private Timer timer;

	public Reminder(int seconds) {
		this.timer = new Timer();
		// ���ڶ�ʱִ��һ��TimerTask���͵Ķ���
		timer.schedule(new RemindTask(), seconds * 1000);
	}

	class RemindTask extends TimerTask {

		@Override
		public void run() {
			System.out.println("Time's up");
			timer.cancel();
		}
	}
}