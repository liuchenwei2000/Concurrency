/**
 * 
 */
package forkjoin.cancel;

import java.util.Random;

/**
 * ����������
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2014��12��29��
 */
public class ArrayGenerator {

	/**
	 * ����ָ�����ȵ����飬Ԫ�ض��Ǹ��������ֵ
	 */
	public int[] generate(int size) {
		int[] array = new int[size];
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			array[i] = random.nextInt(size / 10);
		}
		return array;
	}
}
