/**
 * 
 */
package thread.timer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * util包的定时器(timer)演示类
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2010-6-10
 */
public class UtilityTimerDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("About to schedule task.(5s)");
		new Reminder(5);// 5秒后执行
		System.out.println("Task scheduled.");
	}
}

class Reminder {

	// 定时器
	private Timer timer;

	public Reminder(int seconds) {
		this.timer = new Timer();
		// 用于定时执行一个TimerTask类型的对象
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