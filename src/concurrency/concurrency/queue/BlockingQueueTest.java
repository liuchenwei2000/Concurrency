/**
 * 
 */
package concurrency.queue;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * ����������ʾ
 * <p>
 * ���ܣ�����ָ���ļ��У��������ļ��У��µ��ļ��а��������ؼ��ֵ���Ϣ��
 * <p>
 * ��ͨ��һ���߳�ö��ָ���ļ����µ������ļ���Ȼ������10�������߳�ͬʱչ��������
 * ����漰��ö���̺߳������̵߳ĵȴ�����������߳�֮��Ĳ������⣨�����ظ�����ͬһ���ļ�����
 * ʵ����ö���߳�ģ������������̣߳������߳�ģ������������̡߳�
 * <p>
 * �ڱ�ʾ���У�������Ҫ�κ���ʾ���߳�ͬ�������ǽ������������ݽṹ��Ϊһ��ͬ�����ơ�
 *
 * @author ����ΰ
 *
 * �������ڣ�2013-6-12
 */
public class BlockingQueueTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dirPath = "./src";// �ѱ�Project�µ�Դ���ļ�
		String keyword = "java";
		
		// ����Ϊ10����������
		BlockingQueue<File> queue = new ArrayBlockingQueue<File>(10);
		// �������ļ�ö���߳�
		new Thread(new FileListTask(dirPath, queue)).start();
		// ����10�������߳�ͬʱչ������
		for (int i = 0; i < 10; i++) {
			new Thread(new SearchTask(keyword, queue)).start();
		}
	}
}