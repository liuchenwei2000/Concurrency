/**
 * 
 */
package forkjoin.cancel;

import java.util.Random;

/**
 * 数组生成器
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年12月29日
 */
public class ArrayGenerator {

	/**
	 * 生成指定长度的数组，元素都是赋的随机数值
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
