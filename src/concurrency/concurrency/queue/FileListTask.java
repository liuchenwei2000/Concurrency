/**
 * 
 */
package concurrency.queue;

import java.io.File;
import java.util.concurrent.BlockingQueue;

/**
 * �ļ�ö���߳�
 *
 * @author ����ΰ
 *
 * �������ڣ�2013-6-12
 */
public class FileListTask implements Runnable {

	// �����ļ���־
	public static final File END = new File("");
	
	private String dirPath;// ��ʼĿ¼
	private BlockingQueue<File> queue;
	
	public FileListTask(String dirPath, BlockingQueue<File> queue) {
		this.dirPath = dirPath;
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			listFile(new File(dirPath));
			queue.put(END);// �������ض��Ľ�����־������������
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void listFile(File dir) throws InterruptedException {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				listFile(file);
			} else {
				// ���ϵؽ��м���������������
				queue.put(file);
			}
		}
	}
}