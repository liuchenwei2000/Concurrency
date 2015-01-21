/**
 * 
 */
package concurrency.collections;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
public class ArrayBlockingQueueTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dirPath = "./src";// �ѱ�Project�µ�Դ���ļ�
		String keyword = "Test";
		
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

/**
 * �ļ�ö������
 */
class FileListTask implements Runnable {

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

/**
 * ��������
 */
class SearchTask implements Runnable {

	private String keyword;
	private BlockingQueue<File> queue;
	
	public SearchTask(String keyword, BlockingQueue<File> queue) {
		this.keyword = keyword;
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			boolean complete = false;// �Ƿ������ļ����������
			// ���ϵĴ������������ó��ļ�����������ֱ��ȫ���ļ��������ꡣ
			while (!complete) {
				File file = queue.take();
				if (FileListTask.END == file) {// ͨ���ض���ʶ�ж��Ƿ�ȫ���������
					queue.put(file);// ����Ҫ�ѱ�ʶ�ٷŽ����У��Ա������߳�֪��
					complete = true;
				} else {
					search(file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void search(File file) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String temp = null;
		int line = 0;
		while((temp = br.readLine())!=null){
			line++;
			if(temp.contains(keyword)){
				System.out.print("file path=" + file.getPath());
				System.out.print("  line number=" + line);
				System.out.println("  content=" + temp);
			}
		};
		br.close();
	}
}