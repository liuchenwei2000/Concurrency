/**
 * 
 */
package util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 计时器
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年8月27日
 */
public class TimeCounter {

	private long startTime;
	private long stopTime;

	/**
	 * 开始计时
	 */
	public void start() {
		this.startTime = System.currentTimeMillis();
	}

	/**
	 * 停止计时
	 */
	public void stop() {
		this.stopTime = System.currentTimeMillis();
	}

	/**
	 * 计算耗时，以毫秒为单位
	 */
	public long consume() {
		return stopTime - startTime;
	}

	/**
	 * 计算耗时，以秒为单位
	 */
	public double consumeBySecond() {
		return BigDecimal.valueOf(consume() / 1000.0)
				.setScale(3, RoundingMode.HALF_UP).doubleValue();
	}
	
	/**
	 * 计算耗时，以X分Y秒的形式展现
	 */
	public String consumeByMinute() {
		double seconds = consume() / 1000.0;
		int minutes = (int) (seconds / 60);
		long secondsLeft = ((int) seconds) % 60;
		return minutes + "分 " + secondsLeft + "秒";
	}
}
