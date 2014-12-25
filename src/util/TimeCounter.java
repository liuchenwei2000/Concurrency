/**
 * 
 */
package util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * ��ʱ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2014��8��27��
 */
public class TimeCounter {

	private long startTime;
	private long stopTime;

	/**
	 * ��ʼ��ʱ
	 */
	public void start() {
		this.startTime = System.currentTimeMillis();
	}

	/**
	 * ֹͣ��ʱ
	 */
	public void stop() {
		this.stopTime = System.currentTimeMillis();
	}

	/**
	 * �����ʱ���Ժ���Ϊ��λ
	 */
	public long consume() {
		return stopTime - startTime;
	}

	/**
	 * �����ʱ������Ϊ��λ
	 */
	public double consumeBySecond() {
		return BigDecimal.valueOf(consume() / 1000.0)
				.setScale(3, RoundingMode.HALF_UP).doubleValue();
	}
	
	/**
	 * �����ʱ����X��Y�����ʽչ��
	 */
	public String consumeByMinute() {
		double seconds = consume() / 1000.0;
		int minutes = (int) (seconds / 60);
		long secondsLeft = ((int) seconds) % 60;
		return minutes + "�� " + secondsLeft + "��";
	}
}
